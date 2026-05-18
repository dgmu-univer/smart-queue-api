package ru.dgmu.smartqueue.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto.DegreeProgramPresentation;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.enums.AdminSettingResourceEnum;
import ru.dgmu.smartqueue.enums.Role;
import ru.dgmu.smartqueue.exception.ApiError;
import ru.dgmu.smartqueue.repositories.AdminSettingsRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.DegreeProgramService;
import ru.dgmu.smartqueue.services.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSettingServiceImpl implements AdminSettingService {

  private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

  private final AdminSettingsRepository repository;

  @Override
  public PeriodSettingsDto getPeriodSettings() {
    return repository.findById(AdminSettingResourceEnum.PERIODS)
        .map(entity -> mapper.convertValue(entity.getSettings(), PeriodSettingsDto.class))
        .orElseThrow(() -> new EntityNotFoundException("Period settings not found"));
  }

  @Override
  @Transactional
  public void updatePeriodSettings(PeriodSettingsDto updatedPeriodSettingsDto) {
    AdminSetting entity = repository.findById(AdminSettingResourceEnum.PERIODS)
        .orElseThrow(() -> new EntityNotFoundException("Period settings not found"));
    PeriodSettingsDto current = mapper.convertValue(entity.getSettings(), PeriodSettingsDto.class);
    PeriodSettingsDto merged = current.merge(updatedPeriodSettingsDto);
    entity.setSettings(merged);
  }

  @Override
  public SlotSettingsDto getSlotSettings() {
    return repository.findById(AdminSettingResourceEnum.SLOTS)
        .map(entity -> mapper.convertValue(entity.getSettings(), SlotSettingsDto.class))
        .orElseThrow(() -> new EntityNotFoundException("Slots settings not found"));
  }

  @Override
  @Transactional
  public void updateSlotSettings(SlotSettingsDto updatedSlotSettingsDto) {
    AdminSetting entity = repository.findById(AdminSettingResourceEnum.SLOTS)
        .orElseThrow(() -> new EntityNotFoundException("Slot settings not found"));
    SlotSettingsDto current = mapper.convertValue(entity.getSettings(), SlotSettingsDto.class);
    SlotSettingsDto merged = current.merge(updatedSlotSettingsDto);
    entity.setSettings(merged);
  }

  @Override
  public List<LocalDate> getNonWorkingDays() {
    return repository.findById(AdminSettingResourceEnum.NON_WORKING_DAYS)
        .map(entity -> mapper.convertValue(entity.getSettings(),
            new TypeReference<List<LocalDate>>() {
            }))
        .orElseThrow(() -> new EntityNotFoundException("Non working days settings not found"));
  }

  @Override
  @Transactional
  public List<LocalDate> updateNonWorkingDays(List<LocalDate> updatedNonWorkingDays) {
    AdminSetting entity = repository.findById(AdminSettingResourceEnum.NON_WORKING_DAYS)
        .orElseThrow(() -> new EntityNotFoundException("Non working days settings not found"));
    entity.setSettings(updatedNonWorkingDays);
    return List.of();
  }

  @Override
  public List<ExcludedSlotSettingsDto> getExcludedSlots() {
    return repository.findById(AdminSettingResourceEnum.EXCLUDED_SLOTS)
        .map(entity -> mapper.convertValue(
            entity.getSettings(),
            new TypeReference<List<ExcludedSlotSettingsDto>>() {
            }
        ))
        .orElse(Collections.emptyList());
  }

  @Override
  @Transactional
  public void createExcludedSlot(
      ExcludedSlotSettingsDto excludedSlotSettingsDto) {
    AdminSetting entity = repository.findById(AdminSettingResourceEnum.EXCLUDED_SLOTS)
        .orElseGet(() -> {
          AdminSetting s = new AdminSetting();
          s.setResource(AdminSettingResourceEnum.EXCLUDED_SLOTS);
          return s;
        });

    List<ExcludedSlotSettingsDto> list;
    if (entity.getSettings() == null) {
      list = new ArrayList<>();
    } else {
      list = mapper.convertValue(entity.getSettings(), new TypeReference<>() {
      });
    }

    list.add(excludedSlotSettingsDto.withId(System.currentTimeMillis()));

    entity.setSettings(list);
    repository.save(entity);
  }

  @Override
  @Transactional
  public void deleteExcludedSlot(Long id) {
    AdminSetting entity = repository.findById(AdminSettingResourceEnum.EXCLUDED_SLOTS)
        .orElseThrow(() -> new EntityNotFoundException("Exclude slot settings not found"));

    if (entity.getSettings() == null) {
      throw new EntityNotFoundException("Exclude slot settings not found");
    }

    List<ExcludedSlotSettingsDto> list = mapper.convertValue(entity.getSettings(),
        new TypeReference<>() {
        });
    list.removeIf(slot -> slot.id().equals(id));
    entity.setSettings(list);
  }

  // todo убрать этот слой
  // todo связать слоты с направлением
}
