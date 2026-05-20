package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.dtos.SlotResponse;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.exception.ResourceNotFoundException;
import ru.dgmu.smartqueue.repositories.DegreeProgramRepository;
import ru.dgmu.smartqueue.repositories.SlotRepository;
import ru.dgmu.smartqueue.services.impl.SlotsMeshGeneratorServiceImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotService {

  private final SlotRepository slotRepository;
  private final DegreeProgramRepository degreeProgramRepository;
  private final AdminSettingService adminSettingService;

  /**
   * Получение слотов по фильтру
   */
  public SlotResponse getSlotsByFilter(SlotFilter filter) {
    try {
      log.debug("Getting slots with filter: {}", filter);

      int slotCapacity = adminSettingService.getSlotSettings().capacityPerSlot();
      LocalDateTime dateStart = (filter.date() != null) ? filter.date().atStartOfDay() : null;
      LocalDateTime dateEnd = (filter.date() != null) ? filter.date().atTime(23, 59, 59) : null;
      boolean isBooked = filter.booked() == null || filter.booked();

      List<Slot> slots = slotRepository.findSlotsByFilter(dateStart, dateEnd, filter.degreeId(),
          isBooked, slotCapacity);

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

  /**
   * Генерация сетки слотов для программы образования
   */
  @Transactional
  public void generateServiceMesh(Long degreeProgramId) {
    try {
      log.info("Starting slots mesh generation for degree program ID: {}", degreeProgramId);

      DegreeProgram degreeProgram = degreeProgramRepository.findById(degreeProgramId)
          .orElseThrow(() -> new ResourceNotFoundException("Программа образования", degreeProgramId));

      var periodSettings = adminSettingService.getPeriodSettings();
      var slotSettings = adminSettingService.getSlotSettings();
      var nonWorkingDays = adminSettingService.getNonWorkingDays();
      var excludedSlots = adminSettingService.getExcludedSlots();

      var slotsMeshGenerator = new SlotsMeshGeneratorServiceImpl();
      List<SlotDto> slotsMesh = slotsMeshGenerator.generate(periodSettings, slotSettings,
          nonWorkingDays, excludedSlots, degreeProgramId);

      List<Slot> slots = slotsMesh.stream()
          .map(slotDto -> slotDto.toEntity(degreeProgram))
          .toList();

      slotRepository.saveAll(slots);

      log.info("Successfully generated {} slots for degree program ID: {}",
          slots.size(), degreeProgramId);

    } catch (ResourceNotFoundException e) {
      log.error("Degree program not found: {}", degreeProgramId);
      throw e;
    } catch (Exception e) {
      log.error("Error generating slots mesh for degree program ID: {}", degreeProgramId, e);
      throw new RuntimeException("Ошибка при генерации сетки слотов", e);
    }
  }
}
