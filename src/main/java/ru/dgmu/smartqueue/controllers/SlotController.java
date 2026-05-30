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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.SlotResponseDto;
import ru.dgmu.smartqueue.services.impl.SlotServiceImpl;

@RestController
@RequiredArgsConstructor
@Tag(name = "Slots", description = "Управление слотами")
@Slf4j
@Validated
public class SlotController {

  private final SlotServiceImpl slotServiceImpl;

  @Operation(description = "Получение списка слотов")
  @GetMapping("/public/slots")
  public ResponseEntity<SlotResponseDto> getSlotsByFilter(@Valid SlotFilter filter) {
    log.debug("Getting slots with filter: {}", filter);
    List<LocalTime> slots = slotServiceImpl.getSlotsByFilter(filter);
    log.debug("Found {} slots", slots.size());
    return ResponseEntity.ok(new SlotResponseDto(slots));
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
}
