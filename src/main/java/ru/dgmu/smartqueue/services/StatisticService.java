package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import ru.dgmu.smartqueue.dtos.StatisticResponseDto;

public interface StatisticService {

  StatisticResponseDto getStatistic(Long degreeId, LocalDate date);
}
