package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.services.AppointmentService;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Appointments", description = "Управление записями")
public class AppointmentsController {

  private final AppointmentService appointmentService;

  // TODO: Реализовать методы для работы с записями
  // - Получение свободных слотов с учетом количества, обедов и исключительных дней
  // - Создание записи
  // - Отмена записи
  // - Получение записей по фильтру
}
