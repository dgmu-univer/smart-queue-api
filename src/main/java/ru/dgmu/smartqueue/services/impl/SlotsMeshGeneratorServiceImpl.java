package ru.dgmu.smartqueue.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;

public class SlotsMeshGeneratorServiceImpl {

  public List<SlotDto> generate(PeriodSettingsDto periodSettings, SlotSettingsDto slotSettings,
      List<LocalDate> nonWorkingDays, List<ExcludedSlotSettingsDto> excludedSlots, Long degreeProgramId) {
    var workDate = periodSettings.getWorkDate();
    var workTime = periodSettings.getWorkTime();
    var lunch = periodSettings.getLunch();

    List<SlotDto> generatedSlots = new ArrayList<>();
    var exclusionsByDate = excludedSlots.stream()
        .collect(Collectors.groupingBy(dto -> LocalDate.parse(dto.date())));

    for (LocalDate date = workDate.getStartDate(); !date.isAfter(workDate.getEndDate());
        date = date.plusDays(1)) {
      if (nonWorkingDays.contains(date)) {
        continue;
      }

      List<ExcludedSlotSettingsDto> dailyExclusions = exclusionsByDate.getOrDefault(date,
          List.of());

      LocalTime currentTime = workTime.getStartTime();

      while (true) {
        LocalTime slotEnd = currentTime.plusMinutes(slotSettings.durationMinutes());

        if (slotEnd.isAfter(workTime.getEndTime())) {
          break;
        }

        if (lunch != null && lunch.getStartTime() != null && lunch.getEndTime() != null) {
          boolean intersectsWithLunch =
              currentTime.isBefore(lunch.getEndTime()) && slotEnd.isAfter(lunch.getStartTime());

          if (intersectsWithLunch) {
            currentTime = lunch.getEndTime();
            continue;
          }
        }

        final LocalTime current = currentTime;
        final LocalTime end = slotEnd;

        boolean intersectsWithExclusion = dailyExclusions.stream().anyMatch(ex -> {
          LocalTime exStart = LocalTime.parse(ex.startTime());
          LocalTime exEnd = LocalTime.parse(ex.endTime());
          return current.isBefore(exEnd) && end.isAfter(exStart);
        });

        if (!intersectsWithExclusion) {
          SlotDto slot = new SlotDto(
              null,
              LocalDateTime.of(date, currentTime),
              LocalDateTime.of(date, slotEnd)
          );

          generatedSlots.add(slot);

          currentTime = slotEnd;
        } else {
          currentTime = currentTime.plusMinutes(1);
        }
      }
    }

    return generatedSlots;
  }
}
