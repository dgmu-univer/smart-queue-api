package ru.dgmu.smartqueue.services;

import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.entites.Appointment;

public interface AppointmentService {

  Long bookSlot(AppointmentsRequestDto requestDto);

  Appointment verifyAppointment(AppointmentVerificationRequest verificationRequest);

  Appointment getTets(Long id);
}
