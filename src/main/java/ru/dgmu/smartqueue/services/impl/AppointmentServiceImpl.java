package ru.dgmu.smartqueue.services.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.exception.IncorrectVerificationCode;
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
    VerificationCode verificationCode = OneTimeTokenGenerator.generateCode();
    Slot slot = slotRepository.getSlotByStartTimeAtAndDegreeProgram_Id(
        LocalDateTime.of(requestDto.date(),
                requestDto.time()).atZone(ZoneOffset.UTC).toLocalDateTime(),
        requestDto.degreeId()).orElseThrow(EntityNotFoundException::new);
    Appointment appointment = buildEntity(requestDto, verificationCode, slot);
    Appointment notVerifiedAppointment = appointmentRepository.save(appointment);
    if (isSlotAlreadyOverflowed(appointment)) {
      throw new SlotOverflowed("Данный слот уже занят");
    }
    mobileVerificationSenderService.sendVerificationCode(notVerifiedAppointment.getPhone(),
        notVerifiedAppointment.getPin());
    return notVerifiedAppointment.getId();
  }

  private boolean isSlotAlreadyOverflowed(Appointment appointment) {
    var slotSettings = adminSettingService.getSlotSettings();
    List<Appointment> appointments = appointment.getSlot().getAppointments();
    return appointments.size() >= slotSettings.capacityPerSlot();
  }

  @Override
  @Transactional
  public void verifyAppointment(AppointmentVerificationRequest verificationRequest) {
    Appointment appointment = appointmentRepository.getReferenceById(verificationRequest.id());
    if (appointment.getPin().equals(verificationRequest.verificationCode())) {
      appointment.setIsVerified(Boolean.TRUE);
    } else {
      throw new IncorrectVerificationCode("Неверный код верификации");
    }
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
