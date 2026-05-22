package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.services.AppointmentService;

@RestController
@RequestMapping("/public/appointments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Appointments", description = "Управление записями")
public class AppointmentsController {

  private final AppointmentService appointmentService;

  @PostMapping
  @Operation(summary = "Запись в слот", description = "Создает новую неверефицированную запись")
  public ResponseEntity<Long> bookSlot(@RequestBody @Valid AppointmentsRequestDto requestDto) {
    return ResponseEntity.ok(appointmentService.bookSlot(requestDto));
  }

  // todo на дто поменять
  @PostMapping("/verification")
  @Operation(summary = "Верификация записи в слот", description = "Верифицирует запись")
  public ResponseEntity<Appointment> verify(@RequestBody @Valid AppointmentVerificationRequest verificationRequest) {
    return ResponseEntity.ok(appointmentService.verifyAppointment(verificationRequest));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Тестовый эндпоинт возвращения всех записей", description = "Возвращает все записи в табилце")
  public ResponseEntity<Appointment> getAll(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentService.getTets(id));
  }

  // TODO: Реализовать методы для работы с записями
  // - Получение свободных слотов с учетом количества, обедов и исключительных дней
  // - Создание записи
  // - Отмена записи
  // - Получение записей по фильтру
}
