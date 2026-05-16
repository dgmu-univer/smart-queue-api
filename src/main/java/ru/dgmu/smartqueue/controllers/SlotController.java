package ru.dgmu.smartqueue.controllers;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.services.SlotService;

@RestController
@RequestMapping("/slots")
@RequiredArgsConstructor
@Validated
public class SlotController {

  private final SlotService slotService;

  @GetMapping
  public ResponseEntity<List<SlotDto>> getSlotsByFilter(@Valid SlotFilter filter,
      @Valid SlotFilter filter) {
    return ResponseEntity.ok(slotService.getSlotsByFilter(filter));
  }

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
