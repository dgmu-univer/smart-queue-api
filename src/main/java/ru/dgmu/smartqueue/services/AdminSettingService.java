package ru.dgmu.smartqueue.services;

import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;

public interface AdminSettingService {

  PeriodSettingsDto getPeriodSettings();

  PeriodSettingsDto updatePeriodSettings(PeriodSettingsDto periodSettingsDto);

  SlotSettingsDto getSlotSettings();

  SlotSettingsDto updateSlotSettings();

  List<LocalDate> getNonWorkingDays();

  List<LocalDate> updateNonWorkingDays();
}
