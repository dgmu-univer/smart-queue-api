package ru.dgmu.smartqueue.services;

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
    var slotCapacity = adminSettingService.getSlotSettings().capacityPerSlot();
    if (Boolean.TRUE.equals(filter.booked())) {
      return slotRepository.findByFilterBookedSlots(filter, slotCapacity).stream()
          .map(SlotDto::fromEntity)
          .toList();
    }
    return slotRepository.findByFilterNotBookedSlots(filter, slotCapacity).stream()
        .map(SlotDto::fromEntity)
        .toList();
  }
}
