package ru.dgmu.smartqueue.services.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.Slot;
import ru.dgmu.smartqueue.exception.ResourceNotFoundException;
import ru.dgmu.smartqueue.repositories.DegreeProgramRepository;
import ru.dgmu.smartqueue.repositories.SlotRepository;
import ru.dgmu.smartqueue.services.SlotGenerationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotGenerationServiceImpl implements SlotGenerationService {

  private final DegreeProgramRepository degreeProgramRepository;
  private final SlotRepository slotRepository;

  /**
   * Генерация сетки слотов для программы образования
   */
  @Override
  @Transactional
  public void generateAndSaveWithSkipBooked(PeriodSettingsDto periodSettings,
      SlotSettingsDto slotSettings, List<LocalDate> nonWorkingDays,
      List<ExcludedSlotSettingsDto> excludedSlots) {
    var degreePrograms = degreeProgramRepository.findAll();

    degreePrograms.forEach(degreeProgram -> {
      var slotsWithAppointments = slotRepository.findSlotsWithAppointments(degreeProgram.getId());
      var slots = generateSlotsForDegreeProgram(periodSettings, slotSettings, nonWorkingDays,
          excludedSlots, degreeProgram, slotsWithAppointments);
      slotRepository.deleteAllByDegreeProgram(degreeProgram.getId(), slotsWithAppointments.stream().map(Slot::getId).toList());
      slotRepository.saveAll(slots);
    });
  }

  @Override
  @Transactional
  public void generateAndSaveNewDegree(PeriodSettingsDto periodSettings,
      SlotSettingsDto slotSettings, List<LocalDate> nonWorkingDays,
      List<ExcludedSlotSettingsDto> excludedSlots, DegreeProgram degreeProgram) {
    var slots = generateSlotsForDegreeProgram(periodSettings, slotSettings, nonWorkingDays,
        excludedSlots, degreeProgram, Collections.emptyList());
    slotRepository.saveAll(slots);
  }

  List<Slot> generateSlotsForDegreeProgram(PeriodSettingsDto periodSettings,
      SlotSettingsDto slotSettings, List<java.time.LocalDate> nonWorkingDays,
      List<ExcludedSlotSettingsDto> excludedSlots, DegreeProgram program,
      List<Slot> slotsWithAppointments) {

//    var startDate = LocalDateTime.of(periodSettings.getWorkDate().getStartDate(),
//        periodSettings.getWorkTime().getStartTime());
//    var endDate = LocalDateTime.of(periodSettings.getWorkDate().getEndDate(),
//        periodSettings.getWorkTime().getEndTime());

    var degreeProgramId = program.getId();

    try {
      log.info("Starting slots mesh generation for degree program ID: {}", degreeProgramId);

      var slotsMeshGenerator = new SlotsMeshGeneratorServiceImpl();

      List<SlotDto> slotsMesh = slotsMeshGenerator.generate(periodSettings, slotSettings,
          nonWorkingDays, excludedSlots, slotsWithAppointments);

      List<Slot> slots = slotsMesh.stream()
          .map(slotDto -> slotDto.toEntity(program))
          .toList();

      log.info("Successfully generated {} slots for degree program ID: {}",
          slots.size(), degreeProgramId);

      return slots;
    } catch (ResourceNotFoundException e) {
      log.error("Degree program not found: {}", degreeProgramId);
      throw e;
    } catch (Exception e) {
      log.error("Error generating slots mesh for degree program ID: {}", degreeProgramId, e);
      throw new RuntimeException("Ошибка при генерации сетки слотов", e);
    }

  }
}
