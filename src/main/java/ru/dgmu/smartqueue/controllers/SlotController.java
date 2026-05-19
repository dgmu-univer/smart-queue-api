package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.exception.ApiError;
import ru.dgmu.smartqueue.services.SlotService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Slots", description = "Управление слотами")
@Slf4j
@Validated
public class SlotController {

  private final SlotService slotService;

  @Operation(description = "Получение списка слотов")
  @GetMapping("/public/slots")
  public ResponseEntity<List<SlotDto>> getSlotsByFilter(@Valid SlotFilter filter) {
    return ResponseEntity.ok(slotService.getSlotsByFilter(filter));
  }

  @Operation(description = "Создание сетки слотов по настройками администратора")
  @PostMapping("/slots")
  public ResponseEntity<Void> createSlotsMesh(
      @RequestBody @Valid GenerateSlotsMeshRequest request) {
    slotService.generateServiceMesh(request.degreeProgramId());
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handle(EntityNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.badRequest()
        .body(new ApiError("Указанная программа образования не найдена"));
  }

  @Schema(description = "Фильтр для поиска слотов")
  public record SlotFilter(
      @RequestParam(required = false)
      Boolean booked,
      @RequestParam(required = false)
      LocalDate date,
      @RequestParam(required = false)
      Long degreeId
  ) {

  }

  @Schema(description = "Запрос на создание сетки слотов для программы образования")
  public record GenerateSlotsMeshRequest(
      Long degreeProgramId
  ) {

  }
}
