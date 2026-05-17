package ru.dgmu.smartqueue.services;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.repositories.DegreeProgramRepository;
import ru.dgmu.smartqueue.repositories.SlotRepository;
import ru.dgmu.smartqueue.services.impl.SlotsMeshGeneratorServiceImpl;

@Service
@RequiredArgsConstructor
public class SlotService {

  private final SlotRepository slotRepository;
  private final DegreeProgramRepository degreeProgramRepository;
  private final AdminSettingService adminSettingService;
  private final AdminSettingService settingService;

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

  @Transactional
  public void generateServiceMesh(Long degreeProgramId) {
    DegreeProgram degreeProgram = degreeProgramRepository.findById(degreeProgramId)
        .orElseThrow(() -> new EntityNotFoundException(
            "Degree program with id %s not found".formatted(degreeProgramId)));

    var periodSettings = settingService.getPeriodSettings();
    var slotSettings = settingService.getSlotSettings();
    var nonWorkingDays = settingService.getNonWorkingDays();
    var excludedSlots = settingService.getExcludedSlots();
    var slotsMeshGenerator = new SlotsMeshGeneratorServiceImpl();
    List<SlotDto> slotsMesh = slotsMeshGenerator.generate(periodSettings, slotSettings,
        nonWorkingDays, excludedSlots,
        degreeProgramId);

    List<Slot> slots = slotsMesh.stream()
        .map(slotDto -> slotDto.toEntity(degreeProgram))
        .toList();

    slotRepository.saveAll(slots);
  }
}
