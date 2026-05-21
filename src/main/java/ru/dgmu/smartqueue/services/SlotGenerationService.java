package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;

public interface SlotGenerationService {

  void generateAndSaveWithSkipBooked(PeriodSettingsDto periodSettings,
      SlotSettingsDto slotSettings, List<LocalDate> nonWorkingDays,
      List<ExcludedSlotSettingsDto> excludedSlots);

  void generateAndSaveNewDegree(PeriodSettingsDto periodSettings,
      SlotSettingsDto slotSettings, List<LocalDate> nonWorkingDays,
      List<ExcludedSlotSettingsDto> excludedSlots, DegreeProgram degreeProgram);
}
