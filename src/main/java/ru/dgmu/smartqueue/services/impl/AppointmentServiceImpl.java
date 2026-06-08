package ru.dgmu.smartqueue.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.AppointmentDto;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsExistingValidationRequestDto;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.dtos.CalendarAppointmentsResponseDto;
import ru.dgmu.smartqueue.dtos.SlotsWithPinsDto;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.enums.Resource;
import ru.dgmu.smartqueue.exceptions.IncorrectVerificationCode;
import ru.dgmu.smartqueue.exceptions.ResourceNotFoundException;
import ru.dgmu.smartqueue.exceptions.SlotExpired;
import ru.dgmu.smartqueue.exceptions.SlotOverflowed;
import ru.dgmu.smartqueue.repositories.AppointmentRepository;
import ru.dgmu.smartqueue.repositories.SlotRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.AppointmentService;
import ru.dgmu.smartqueue.services.MobileVerificationSenderService;
import ru.dgmu.smartqueue.services.impl.OneTimeTokenGenerator.VerificationCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

  @Value("${app.appointments.ttl-minutes:15}")
  private long ttlMinutes;

  private final SlotRepository slotRepository;
  private final AppointmentRepository appointmentRepository;
  private final MobileVerificationSenderService mobileVerificationSenderService;
  private final AdminSettingService adminSettingService;

  @Override
  @Transactional
  public Long bookSlot(AppointmentsRequestDto requestDto) {
    validateBookingDateExpiring(requestDto.date());
    Slot slot = slotRepository.getSlotByStartTimeAtAndDegreeProgram_Id(
            LocalDateTime.of(requestDto.date(),
                requestDto.time()).atZone(ZoneOffset.UTC).toLocalDateTime(), requestDto.degreeId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Ресурс: 'Слот' для уровня образования с идентификатором: %s и на время %s не найден".formatted(
                requestDto.degreeId(), requestDto.date())));
    VerificationCode verificationCode = OneTimeTokenGenerator.generateCode();
    appointmentRepository.deleteByDegreeIdAndPhone(requestDto.degreeId(), requestDto.phone());
    Appointment appointment = buildEntity(requestDto, verificationCode, slot);
    Appointment notVerifiedAppointment = appointmentRepository.save(appointment);
    if (isSlotAlreadyOverflowed(appointment)) {
      throw new SlotOverflowed("Данный слот уже занят");
    }
    mobileVerificationSenderService.sendVerificationCode(notVerifiedAppointment.getPhone(),
        notVerifiedAppointment.getPin());
    return notVerifiedAppointment.getId();
  }

  private void validateBookingDateExpiring(LocalDate date) {
    if (date.isBefore(LocalDate.now())) {
      throw new SlotExpired(
          "Вы не можете записаться в слот с временем начала ранее текущего времени");
    }
  }

  private boolean isSlotAlreadyOverflowed(Appointment appointment) {
    var slotSettings = adminSettingService.getSlotSettings(
        appointment.getSlot().getDegreeProgram().getId());
    List<Appointment> appointments = appointment.getSlot().getAppointments();
    return appointments.size() >= slotSettings.capacityPerSlot();
  }

  @Override
  @Transactional
  public AppointmentDto verifyAppointment(AppointmentVerificationRequest verificationRequest) {
    Appointment appointment = appointmentRepository.findById(verificationRequest.id())
        .orElseThrow(
            () -> new ResourceNotFoundException(Resource.APPOINTMENT, verificationRequest.id()));
    if (appointment.getPin().equals(verificationRequest.verificationCode())) {
      appointment.setIsVerified(Boolean.TRUE);
    } else {
      log.error("Incorrect verification code");
      throw new IncorrectVerificationCode("Неверный код верификации");
    }
    appointmentRepository.deleteById(verificationRequest.id());
    return AppointmentDto.fromEntity(appointmentRepository.saveAndFlush(appointment));
  }

  @Override
  public AppointmentDto getTets(Long id) {
    return AppointmentDto.fromEntity(appointmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.APPOINTMENT, id)));
  }

  @Override
  @Transactional(readOnly = true)
  public CalendarAppointmentsResponseDto getAllByFilter(LocalDate from, LocalDate to,
      Long degreeId) {
    List<Appointment> allByInterval = appointmentRepository.findAllByInterval(from, to, degreeId);
    List<SlotsWithPinsDto> slotsWithPins = allByInterval.stream()
        .collect(Collectors.groupingBy(Appointment::getSlot)).entrySet().stream()
        .map(SlotsWithPinsDto::build)
        .sorted()
        .toList();
    return new CalendarAppointmentsResponseDto(adminSettingService.getSlotSettings(degreeId),
        slotsWithPins);
  }

  @Override
  public long countByDegreeAndDate(Long degreeId, LocalDate date) {
    return appointmentRepository.countByDateAndDegreeProgramId(date, degreeId);
  }

  @Override
  public boolean checkExisting(AppointmentsExistingValidationRequestDto requestDto) {
    return appointmentRepository.existsBySlot_DegreeProgram_IdAndPhone(requestDto.degreeId(),
        requestDto.phone());
  }

  @Scheduled(cron = "0 * * * * *")
  @Transactional
  public void cleanupExpiredAppointments() {
    try {
      log.info("Запуск шедуллера очистки просроченных броней...");
      LocalDateTime cutoffTime = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(ttlMinutes);
      int deletedCount = appointmentRepository.deleteExpiredUnverifiedAppointments(cutoffTime);
      if (deletedCount > 0) {
        log.info("Шедуллер успешно удалил {} неверифицированных записей, созданных до {}",
            deletedCount, cutoffTime);
      } else {
        log.debug("Просроченных неверифицированных записей не обнаружено");
      }
    } catch (Exception e) {
      log.error("Ошибка при очистке просроченных броней", e);
    }
  }

  private Appointment buildEntity(AppointmentsRequestDto requestDto,
      VerificationCode verificationCode, Slot slot) {
    return new Appointment(null, verificationCode.value(), requestDto.phone(), Boolean.FALSE,
        LocalDateTime.now(ZoneOffset.UTC), slot);
  }
}
