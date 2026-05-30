package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.dtos.AppointmentDto;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.dtos.CalendarAppointmentsResponseDto;

public interface AppointmentService {

  Long bookSlot(AppointmentsRequestDto requestDto);

  AppointmentDto verifyAppointment(AppointmentVerificationRequest verificationRequest);

  AppointmentDto getTets(Long id);

  List<CalendarAppointmentsResponseDto> getAllByFilter(LocalDate from, LocalDate to,
      Long degreeId);

  long countByDegreeAndDate(Long degreeId, LocalDate date);
}
