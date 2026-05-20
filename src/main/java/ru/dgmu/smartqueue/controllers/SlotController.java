package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.dtos.SlotResponse;
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
  public ResponseEntity<SlotResponse> getSlotsByFilter(@Valid SlotFilter filter) {
    log.debug("Getting slots with filter: {}", filter);
    SlotResponse slots = slotService.getSlotsByFilter(filter);
    log.debug("Found {} slots", slots.slots().size());
    return ResponseEntity.ok(slots);
  }

  @Operation(description = "Создание сетки слотов по настройкам администратора")
  @PostMapping("/slots")
  public ResponseEntity<Void> createSlotsMesh(@RequestBody @Valid GenerateSlotsMeshRequest request) {
    log.info("Creating slots mesh for degree program ID: {}", request.degreeProgramId());
    slotService.generateServiceMesh(request.degreeProgramId());
    log.info("Slots mesh created successfully for degree program ID: {}", request.degreeProgramId());
    return ResponseEntity.ok().build();
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
