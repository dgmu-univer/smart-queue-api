package ru.dgmu.smartqueue.services.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.AdminSettingsDto;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.entites.ExcludedSlot;
import ru.dgmu.smartqueue.enums.Resource;
import ru.dgmu.smartqueue.exceptions.ResourceNotFoundException;
import ru.dgmu.smartqueue.repositories.AdminSettingsRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.SlotGenerationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSettingServiceImpl implements AdminSettingService {

  private final AdminSettingsRepository repository;
  private final SlotGenerationService slotGenerationService;

  @Override
  public AdminSettingsDto getAdminSettings(Long degreeId) {
    return repository.findByDegreeProgramId(degreeId)
        .map(AdminSettingsDto::fromEntity)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
  }

  @Override
  public PeriodSettingsDto getPeriodSettings(Long degreeId) {
    return repository.findByDegreeProgramId(degreeId)
        .map(PeriodSettingsDto::fromEntity)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
  }

  @Override
  @Transactional
  public void updatePeriodSettings(Long degreeId, PeriodSettingsDto periodSettingsDto) {
    AdminSetting entity = repository.findByDegreeProgramId(degreeId)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));

    if (periodSettingsDto.getWorkDate() != null) {
      entity.setWorkStartDate(periodSettingsDto.getWorkDate().getStartDate());
      entity.setWorkEndDate(periodSettingsDto.getWorkDate().getEndDate());
    }
    if (periodSettingsDto.getWorkTime() != null) {
      entity.setWorkStartTime(periodSettingsDto.getWorkTime().getStartTime());
      entity.setWorkEndTime(periodSettingsDto.getWorkTime().getEndTime());
    }
    if (periodSettingsDto.getLunch() != null) {
      entity.setLunchStartTime(periodSettingsDto.getLunch().getStartTime());
      entity.setLunchEndTime(periodSettingsDto.getLunch().getEndTime());
    }

    repository.save(entity);
    regenerageUnbookedSlots(degreeId);
  }

  @Override
  public SlotSettingsDto getSlotSettings(Long degreeId) {
    return repository.findByDegreeProgramId(degreeId)
        .map(SlotSettingsDto::fromEntity)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
  }

  @Override
  @Transactional
  public void updateSlotSettings(Long degreeId, SlotSettingsDto updatedSlotSettingsDto) {
    AdminSetting entity = repository.findByDegreeProgramId(degreeId)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
    if (updatedSlotSettingsDto.capacityPerSlot() != null) {
      entity.setCapacityPerSlot(updatedSlotSettingsDto.capacityPerSlot());
    }
    if (updatedSlotSettingsDto.durationMinutes() != null) {
      entity.setDurationMinutes(updatedSlotSettingsDto.durationMinutes());
    }
    repository.save(entity);
    regenerageUnbookedSlots(degreeId);
  }

  @Override
  public List<LocalDate> getNonWorkingDays(Long degreeId) {
    return repository.findByDegreeProgramId(degreeId)
        .map(AdminSetting::getNonWorkingDays)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
  }

  @Override
  @Transactional
  public void updateNonWorkingDays(Long degreeId, List<LocalDate> updatedNonWorkingDays) {
    AdminSetting entity = repository.findByDegreeProgramId(degreeId)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
    entity.setNonWorkingDays(updatedNonWorkingDays);
    repository.save(entity);
    regenerageUnbookedSlots(degreeId);
  }

  @Override
  public List<ExcludedSlotSettingsDto> getExcludedSlots(Long degreeId) {
    List<ExcludedSlot> excludedSlots = repository.findByDegreeProgramId(degreeId)
        .map(AdminSetting::getExcludedSlots)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
    return excludedSlots.stream()
        .map(ExcludedSlotSettingsDto::fromEntity)
        .toList();
  }

  @Override
  @Transactional
  public void createExcludedSlot(Long degreeId, ExcludedSlotSettingsDto excludedSlotSettingsDto) {
    AdminSetting entity = repository.findByDegreeProgramId(degreeId)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
    entity.getExcludedSlots()
        .add(excludedSlotSettingsDto.withId(System.currentTimeMillis()).toEntity());
    repository.save(entity);
    regenerageUnbookedSlots(degreeId);
  }

  @Override
  @Transactional
  public void deleteExcludedSlot(Long degreeId, Long id) {
    AdminSetting settings = repository.findByDegreeProgramId(degreeId)
        .orElseThrow(() -> new ResourceNotFoundException(Resource.DEGREE_PROGRAM, degreeId));
    boolean removed = settings.getExcludedSlots().removeIf(slot -> slot.getId().equals(id));
    if (!removed) {
      throw new ResourceNotFoundException(Resource.EXCLUDED_SLOT, id);
    }
    repository.save(settings);
    regenerageUnbookedSlots(degreeId);
  }

  private void regenerageUnbookedSlots(Long degreeId) {
    AdminSettingsDto adminSettings = getAdminSettings(degreeId);
    slotGenerationService.generateAndSaveWithSkipBooked(adminSettings.periods(),
        adminSettings.slots(), adminSettings.nonWorkingDays(),
        adminSettings.excludedSlots(), degreeId);
  }
}
