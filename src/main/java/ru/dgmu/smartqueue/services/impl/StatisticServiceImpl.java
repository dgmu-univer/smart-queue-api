package ru.dgmu.smartqueue.services.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.dtos.StatisticResponseDto;
import ru.dgmu.smartqueue.services.AppointmentService;
import ru.dgmu.smartqueue.services.StatisticService;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

  private final AppointmentService appointmentService;

  @Override
  public StatisticResponseDto getStatistic(Long degreeId, LocalDate date) {
    return new StatisticResponseDto(appointmentService.countByDegreeAndDate(degreeId, date));
  }
}
