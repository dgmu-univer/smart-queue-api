package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.dtos.CalendarAppointmentsResponseDto;
import ru.dgmu.smartqueue.entites.Appointment;

public interface AppointmentService {

  Long bookSlot(AppointmentsRequestDto requestDto);

  Appointment verifyAppointment(AppointmentVerificationRequest verificationRequest);

  Appointment getTets(Long id);

  List<CalendarAppointmentsResponseDto> getAllByFilter(LocalDate from, LocalDate to,
      Long degreeId);
}
