package ru.dgmu.smartqueue.services.impl;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.AdminSettingsDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto.DegreeProgramPresentation;
import ru.dgmu.smartqueue.dtos.DegreeProgramsWithPeriodDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.enums.Role;
import ru.dgmu.smartqueue.repositories.DegreeProgramRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.DegreeProgramService;
import ru.dgmu.smartqueue.services.SlotGenerationService;
import ru.dgmu.smartqueue.services.UserService;

@Service
@RequiredArgsConstructor
public class DegreeProgramServiceImpl implements DegreeProgramService {

  private final DegreeProgramRepository degreeProgramRepository;
  private final AdminSettingService adminSettingService;
  private final SlotGenerationService slotGenerationService;
  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public DegreeProgramDto findDegreeProgramByUserId(User user) {
    return DegreeProgramDto.fromEntity(degreeProgramRepository.findByUserId(user));
  }

  @Override
  @Transactional(readOnly = true)
  public DegreeProgramsWithPeriodDto getDegreeProgramsPresentations() {
    List<DegreeProgramPresentation> degreePresantations = degreeProgramRepository.findAll().stream()
        .map(DegreeProgramDto::fromEntity)
        .map(DegreeProgramDto::toPresentation)
        .toList();
    return new DegreeProgramsWithPeriodDto(degreePresantations);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DegreeProgramDto> getDegreePrograms() {
    return degreeProgramRepository.findAll().stream()
        .map(DegreeProgramDto::fromEntity)
        .toList();
  }

  @Override
  @Transactional
  public void createDegreeProgram(DegreeProgramDto degreeProgramDto) {
    var user = userService.create(User.builder()
        .username(UUID.randomUUID().toString())
        .pin(degreeProgramDto.pin())
        .isEnabled(Boolean.TRUE)
        .role(Role.OPERATOR)
        .build());
    var degreeProgram = degreeProgramRepository.save(degreeProgramDto.toEntity(user));
    createSlots(degreeProgram);
  }

  private void createSlots(DegreeProgram degreeProgram) {
    AdminSettingsDto adminSettingsDto = AdminSettingsDto.fromEntity(
        degreeProgram.getAdminSettings());
    slotGenerationService.generateAndSaveNewDegree(adminSettingsDto.periodSettings(),
        adminSettingsDto.slotSettings(), adminSettingsDto.nonWorkingDays(),
        adminSettingsDto.excludedSlotSettings(), degreeProgram);
  }

  @Override
  @Transactional
  public void deleteDegreeProgram(Long id) {
    degreeProgramRepository.deleteById(id);
  }
}
