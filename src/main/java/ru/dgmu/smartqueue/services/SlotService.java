package ru.dgmu.smartqueue.services;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.repositories.SlotRepository;

@Service
@RequiredArgsConstructor
public class SlotService {

  private final SlotRepository slotRepository;
  private final AdminSettingService adminSettingService;

  public List<SlotDto> getSlotsByFilter(SlotFilter filter) {
    int slotCapacity = adminSettingService.getSlotSettings().capacityPerSlot();
    LocalDateTime dateStart = (filter.date() != null) ? filter.date().atStartOfDay() : null;
    LocalDateTime dateEnd = (filter.date() != null) ? filter.date().atTime(23, 59, 59) : null;
    boolean isBooked = filter.booked() == null || filter.booked();
    return slotRepository.findSlotsByFilter(dateStart, dateEnd, filter.degreeId(), isBooked,
            slotCapacity)
        .stream()
        .map(SlotDto::fromEntity)
        .toList();
  }
}
