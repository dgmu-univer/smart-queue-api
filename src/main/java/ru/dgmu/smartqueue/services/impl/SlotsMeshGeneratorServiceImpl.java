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
import ru.dgmu.smartqueue.entites.Slot;

public class SlotsMeshGeneratorServiceImpl {

  public List<SlotDto> generate(PeriodSettingsDto periodSettings, SlotSettingsDto slotSettings,
      List<LocalDate> nonWorkingDays, List<ExcludedSlotSettingsDto> excludedSlots,
      List<Slot> hadAppointmentsSlots) {
    var workDate = periodSettings.getWorkDate();
    var workTime = periodSettings.getWorkTime();
    var lunch = periodSettings.getLunch();

    List<SlotDto> generatedSlots = new ArrayList<>();

    // Группируем исключения по датам для быстрой проверки
    var exclusionsByDate = excludedSlots.stream()
        .collect(Collectors.groupingBy(ExcludedSlotSettingsDto::date));

    // Группируем занятые слоты по датам (избегаем O(N) перебора для каждого минутного шага)
    var appointmentsByDate = hadAppointmentsSlots.stream()
        .collect(Collectors.groupingBy(slot -> slot.getStartTimeAt().toLocalDate()));

    for (LocalDate date = workDate.getStartDate(); !date.isAfter(workDate.getEndDate());
        date = date.plusDays(1)) {
      if (nonWorkingDays.contains(date)) {
        continue;
      }

      List<ExcludedSlotSettingsDto> dailyExclusions = exclusionsByDate.getOrDefault(date,
          List.of());

      // Достаем занятые слоты конкретно для текущего дня расписания
      List<Slot> dailyAppointments = appointmentsByDate.getOrDefault(date, List.of());

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

        // 1. Проверка пересечения с административными исключениями
        boolean intersectsWithExclusion = dailyExclusions.stream().anyMatch(ex -> {
          LocalTime exStart = ex.startTime();
          LocalTime exEnd = ex.endTime();
          return current.isBefore(exEnd) && end.isAfter(exStart);
        });

        // 2. Проверка пересечения со слотами, на которые уже записаны люди
        boolean intersectsWithAppointment = dailyAppointments.stream().anyMatch(slot -> {
          LocalTime appStart = slot.getStartTimeAt().toLocalTime();
          LocalTime appEnd = slot.getEndTimeAt().toLocalTime();
          return current.isBefore(appEnd) && end.isAfter(appStart);
        });

        // Слот валиден, только если нет пересечений ни с исключениями, ни с записями
        if (!intersectsWithExclusion && !intersectsWithAppointment) {
          SlotDto slot = new SlotDto(
              null,
              LocalDateTime.of(date, currentTime),
              LocalDateTime.of(date, slotEnd)
          );

          generatedSlots.add(slot);

          currentTime = slotEnd;
        } else {
          // Если пересечение найдено, делаем шаг в 1 минуту для поиска ближайшего окна
          currentTime = currentTime.plusMinutes(1);
        }
      }
    }

    return generatedSlots;
  }
}
