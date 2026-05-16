package ru.dgmu.smartqueue.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
//import ru.dgmu.smartqueue.controllers.AppointmentsController.AppointmentsFilter;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.enums.AdminSettingResourceEnum;
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
