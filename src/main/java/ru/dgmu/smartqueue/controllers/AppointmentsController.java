package ru.dgmu.smartqueue.controllers;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.services.AppointmentService;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentsController {

  private final AppointmentService service;

  // todo возращать свободные слоты с учетом количества, обедов и исключительных дней дат и рабочего времени
//  @GetMapping
//  public ResponseEntity<List<Appointment>> getByFilter(AppointmentsFilter filter) {
//    return ResponseEntity.ok(service.findByFilter(filter));
//  }
}
