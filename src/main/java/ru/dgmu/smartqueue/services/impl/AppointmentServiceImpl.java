package ru.dgmu.smartqueue.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.AppointmentDto;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsExistingValidationRequestDto;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.dtos.CalendarAppointmentsResponseDto;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.enums.Resource;
import ru.dgmu.smartqueue.exception.IncorrectVerificationCode;
import ru.dgmu.smartqueue.exception.ResourceNotFoundException;
import ru.dgmu.smartqueue.exception.SlotExpired;
import ru.dgmu.smartqueue.exception.SlotOverflowed;
import ru.dgmu.smartqueue.repositories.AppointmentRepository;
import ru.dgmu.smartqueue.repositories.SlotRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.AppointmentService;
import ru.dgmu.smartqueue.services.MobileVerificationSenderService;
import ru.dgmu.smartqueue.services.impl.OneTimeTokenGenerator.VerificationCode;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

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
    Appointment appointment = appointmentRepository.getReferenceById(verificationRequest.id());
    if (appointment.getPin().equals(verificationRequest.verificationCode())) {
      appointment.setIsVerified(Boolean.TRUE);
    } else {
      throw new IncorrectVerificationCode("Неверный код верификации");
    }
    return AppointmentDto.fromEntity(appointmentRepository.save(appointment));
  }

  @Override
  public AppointmentDto getTets(Long id) {
    return AppointmentDto.fromEntity(appointmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.APPOINTMENT, id)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<CalendarAppointmentsResponseDto> getAllByFilter(LocalDate from, LocalDate to,
      Long degreeId) {
    List<Appointment> allByInterval = appointmentRepository.findAllByInterval(from, to, degreeId);
    return allByInterval.stream()
        .map(this::buildCalendarResponseDto)
        .toList();
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

  CalendarAppointmentsResponseDto buildCalendarResponseDto(Appointment appointment) {
    return new CalendarAppointmentsResponseDto(
        appointment.getId(),
        appointment.getPin(),
        appointment.getSlot().getStartTimeAt(),
        appointment.getSlot().getEndTimeAt()
    );
  }

  // todo шедуллер который будет выгребать все устаревшие не верефицированные соты

  // todo подумать как сделать недоступность слотов которые уже переполнены даже если appointment not verified
  private Appointment buildEntity(AppointmentsRequestDto requestDto,
      VerificationCode verificationCode, Slot slot) {
    return new Appointment(null, verificationCode.value(), requestDto.phone(), Boolean.FALSE,
        LocalDateTime.now(ZoneOffset.UTC), slot);
  }

  // todo удаление слота сразу же как прошел таймер если человек не подтвердил его
}
