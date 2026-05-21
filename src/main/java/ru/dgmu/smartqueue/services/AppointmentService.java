package ru.dgmu.smartqueue.services;

import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;

public interface AppointmentService {

  Long bookSlot(AppointmentsRequestDto requestDto);

  void verifyAppointment(AppointmentVerificationRequest verificationRequest);
}
