package ru.dgmu.smartqueue.controllers;

import io.github.bucket4j.ConsumptionProbe;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.component.AppointmentBookRateLimiterComponent;
import ru.dgmu.smartqueue.dtos.AppointmentBookingResponseDto;
import ru.dgmu.smartqueue.dtos.AppointmentDto;
import ru.dgmu.smartqueue.dtos.AppointmentExistingValidationResponseDto;
import ru.dgmu.smartqueue.dtos.AppointmentVerificationRequest;
import ru.dgmu.smartqueue.dtos.AppointmentsExistingValidationRequestDto;
import ru.dgmu.smartqueue.dtos.AppointmentsRequestDto;
import ru.dgmu.smartqueue.dtos.CalendarAppointmentsResponseDto;
import ru.dgmu.smartqueue.services.AppointmentService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Appointments", description = "Управление записями")
public class AppointmentsController {

  private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
  private static final String X_RATE_LIMIT_RETRY_AFTER_HEADER = "X-Ratelimit-Retry-After";
  private static final String X_RATE_LIMIT_REMAINING_HEADER = "X-Ratelimit-Remaining";
  private static final String AC_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";

  private final AppointmentService appointmentService;
  private final AppointmentBookRateLimiterComponent appointmentBookRateLimiterComponent;

  @PostMapping("/public/appointments")
  @Operation(summary = "Запись в слот", description = "Создает новую неверефицированную запись")
  public ResponseEntity<AppointmentBookingResponseDto> bookSlot(
      @RequestBody @Valid AppointmentsRequestDto requestDto, HttpServletRequest request) {
    String ipAddress = request.getHeader(X_FORWARDED_FOR_HEADER);
    ConsumptionProbe probe;
    try {
      probe = appointmentBookRateLimiterComponent.resolveBucket(ipAddress)
          .tryConsumeAndReturnRemaining(1);
    } catch (Exception e) {
      probe = ConsumptionProbe.consumed(0, 0);
      log.error("Redis Rate Limiter недоступен для ключа {}. Сработал Fail-Open.", ipAddress, e);
    }
    if (probe.isConsumed()) {
      return ResponseEntity.status(HttpStatus.OK)
          .header(AC_EXPOSE_HEADERS_HEADER,
              "%s, %s".formatted(X_RATE_LIMIT_RETRY_AFTER_HEADER, X_RATE_LIMIT_REMAINING_HEADER))
          .header(X_RATE_LIMIT_REMAINING_HEADER, String.valueOf(probe.getRemainingTokens()))
          .body(appointmentService.bookSlot(requestDto));
    }
    log.warn("Temporally blocked appointment for ip: {}", ipAddress);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .header(AC_EXPOSE_HEADERS_HEADER,
            "%s, %s".formatted(X_RATE_LIMIT_RETRY_AFTER_HEADER, X_RATE_LIMIT_REMAINING_HEADER))
        .header(X_RATE_LIMIT_RETRY_AFTER_HEADER,
            String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())))
        .header(X_RATE_LIMIT_REMAINING_HEADER, String.valueOf(probe.getRemainingTokens()))
        .build();
  }

  @PostMapping("/public/appointments/existing-validation")
  @Operation(summary = "Проверка на существование ранее созданных записей по номеру телефона и программе образования",
      description = "Проверяет наличие записей по номеру телефона и программе образования")
  public ResponseEntity<AppointmentExistingValidationResponseDto> checkExisting(@RequestBody @Valid
  AppointmentsExistingValidationRequestDto requestDto) {
    return ResponseEntity.ok(new AppointmentExistingValidationResponseDto(
        appointmentService.checkExisting(requestDto)));
  }

  @PostMapping("/public/appointments/verification")
  @Operation(summary = "Верификация записи в слот", description = "Верифицирует запись")
  public ResponseEntity<AppointmentDto> verify(
      @RequestBody @Valid AppointmentVerificationRequest verificationRequest) {
    return ResponseEntity.ok(appointmentService.verifyAppointment(verificationRequest));
  }

  @GetMapping("/public/appointments/{id}")
  @Operation(summary = "Тестовый эндпоинт возвращения всех записей", description = "Возвращает все записи в табилце")
  public ResponseEntity<AppointmentDto> getAll(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentService.getTets(id));
  }

  @GetMapping("/appointments")
  @Operation(summary = "Получение записей для календаря", description = "Возвращаем записи по фильтру для календаря")
  public ResponseEntity<CalendarAppointmentsResponseDto> getAllByFilter(
      @Parameter(name = "Начало интервала") @RequestParam LocalDate from,
      @Parameter(name = "Конец интервала") @RequestParam LocalDate to,
      @Parameter(name = "Программа образования") @RequestParam Long degreeId) {
    return ResponseEntity.ok(appointmentService.getAllByFilter(from, to, degreeId));
  }
}
