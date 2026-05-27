package ru.dgmu.smartqueue.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.StatisticResponseDto;
import ru.dgmu.smartqueue.services.StatisticService;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
public class StatisticController {

  private final StatisticService statisticService;

  @GetMapping
  public ResponseEntity<StatisticResponseDto> getStatistic(@Valid StatisticRequestParams params) {
    return ResponseEntity.ok(statisticService.getStatistic(params.degreeId, params.date));
  }

  public record StatisticRequestParams(
      @RequestParam
      @NotNull(message = "Не передан идентификатор программы образования")
      Long degreeId,
      @RequestParam(required = false)
      LocalDate date
  ) {

  }

}
