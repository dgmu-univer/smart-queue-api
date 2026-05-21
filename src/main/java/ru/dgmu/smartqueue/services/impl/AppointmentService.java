package ru.dgmu.smartqueue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.repositories.AppointmentRepository;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;

//  public List<Appointment> findByFilter(AppointmentsFilter filter) {
////    return appointmentRepository.findByFilter(filter);
//    return List.of();
//  }
}
