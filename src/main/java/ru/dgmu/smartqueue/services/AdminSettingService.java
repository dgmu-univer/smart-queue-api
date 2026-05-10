package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;

public interface AdminSettingService {

  PeriodSettingsDto getPeriodSettings();

  void updatePeriodSettings(PeriodSettingsDto updatedPeriodSettingsDto);

  SlotSettingsDto getSlotSettings();

  void updateSlotSettings(SlotSettingsDto updatedSlotSettingsDto);

  List<LocalDate> getNonWorkingDays();

  List<LocalDate> updateNonWorkingDays(List<LocalDate> updatedNonWorkingDays);

  List<ExcludedSlotSettingsDto> getExcludedSlots();

  void createExcludedSlot(ExcludedSlotSettingsDto excludedSlotSettingsDto);

  void deleteExcludedSlot(Long id);
}
