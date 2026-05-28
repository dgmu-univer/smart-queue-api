package ru.dgmu.smartqueue.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.dtos.AdminSettingsDto;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;

public interface AdminSettingService {

  AdminSettingsDto getAdminSettings(Long degreeId);

  PeriodSettingsDto getPeriodSettings(Long degreeId);

  void updatePeriodSettings(Long degreeId, PeriodSettingsDto periodSettingsDto) throws JsonProcessingException;

  SlotSettingsDto getSlotSettings(Long degreeId);

  void updateSlotSettings(Long degreeId, SlotSettingsDto updatedSlotSettingsDto);

  List<LocalDate> getNonWorkingDays(Long degreeId);

  void updateNonWorkingDays(Long degreeId, List<LocalDate> updatedNonWorkingDays);

  List<ExcludedSlotSettingsDto> getExcludedSlots(Long degreeId);

  void createExcludedSlot(Long degreeId, ExcludedSlotSettingsDto excludedSlotSettingsDto);

  void deleteExcludedSlot(Long degreeId, Long id);
}
