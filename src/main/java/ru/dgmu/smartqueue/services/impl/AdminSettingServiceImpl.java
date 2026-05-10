package ru.dgmu.smartqueue.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.AdminSettingsConverter;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.enums.AdminSettingResourceEnum;
import ru.dgmu.smartqueue.repositories.AdminSettingsRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;

@Service
@RequiredArgsConstructor
public class AdminSettingServiceImpl implements AdminSettingService {

  private final AdminSettingsRepository repository;

  @Override
  public PeriodSettingsDto getPeriodSettings() {
    Optional<AdminSetting> periodSettings =
        repository.findById(AdminSettingResourceEnum.PERIODS);
    var adminSetting = periodSettings.orElseThrow();
    try {
      return AdminSettingsConverter.convertToPeriodSettingsDto(adminSetting);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public PeriodSettingsDto updatePeriodSettings(PeriodSettingsDto periodSettingsDto) {
    return null;
  }

  @Override
  public SlotSettingsDto getSlotSettings() {
    Optional<AdminSetting> periodSettings =
        repository.findById(AdminSettingResourceEnum.SLOTS);
    var adminSetting = periodSettings.orElseThrow();
    try {
      return AdminSettingsConverter.convertToSlotSettingsDto(adminSetting);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public SlotSettingsDto updateSlotSettings() {
    return null;
  }

  @Override
  public List<LocalDate> getNonWorkingDays() {
    Optional<AdminSetting> periodSettings =
        repository.findById(AdminSettingResourceEnum.NON_WORKING_DAYS);
    var adminSetting = periodSettings.orElseThrow();
    try {
      return AdminSettingsConverter.convertToNonWorkingDaysSettingsDto(adminSetting);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }  }

  @Override
  public List<LocalDate> updateNonWorkingDays() {
    return List.of();
  }
}
