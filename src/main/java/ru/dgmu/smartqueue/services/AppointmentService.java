package ru.dgmu.smartqueue.services;

import java.util.List;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.entites.Appointment;

public interface AppointmentService {

  Long bookSlot(AppointmentsRequestDto requestDto);

  void verifyAppointment(AppointmentVerificationRequest verificationRequest);

  List<Appointment> getTets();
}
