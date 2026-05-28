package ru.dgmu.smartqueue.services.impl;

import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;
import ru.dgmu.smartqueue.dtos.SlotResponse;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.repositories.SlotRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.SlotService;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotServiceImpl implements SlotService {

  private final SlotRepository slotRepository;

  private final AdminSettingService adminSettingService;

  /**
   * Получение слотов по фильтру
   */
  public SlotResponse getSlotsByFilter(SlotFilter filter) {
    try {
      log.debug("Getting slots with filter: {}", filter);

      var slotSettings = adminSettingService.getSlotSettings(filter.degreeId());
      var dateStart = (filter.date() != null) ? filter.date().atStartOfDay() : null;
      var dateEnd = (filter.date() != null) ? filter.date().atTime(23, 59, 59) : null;
      boolean isBooked = filter.booked() == null || filter.booked();

      List<Slot> slots = slotRepository.findSlotsByFilter(dateStart, dateEnd, filter.degreeId(),
          isBooked, slotSettings.capacityPerSlot());

      List<LocalTime> result = slots.stream()
          .map(slot -> slot.getStartTimeAt().toLocalTime())
          .sorted()
          .toList();

      log.debug("Found {} slots matching filter", result.size());
      return new SlotResponse(result);

    } catch (Exception e) {
      log.error("Error getting slots by filter: {}", filter, e);
      throw new RuntimeException("Ошибка при получении слотов", e);
    }
  }
}
