package ru.dgmu.smartqueue.services.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto.DegreeProgramPresentation;
import ru.dgmu.smartqueue.dtos.DegreeProgramsWithPeriodDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.enums.Role;
import ru.dgmu.smartqueue.repositories.DegreeProgramRepository;
import ru.dgmu.smartqueue.services.AdminSettingService;
import ru.dgmu.smartqueue.services.DegreeProgramService;
import ru.dgmu.smartqueue.services.UserService;

@Service
public class DegreeProgramServiceImpl implements DegreeProgramService {

  public DegreeProgramDto findDegreeProgramByUserId(User user) {
    return DegreeProgramDto.fromEntity(degreeProgramRepository.findByUserId(user));
  }

  private final DegreeProgramRepository degreeProgramRepository;
  private final AdminSettingService adminSettingService;
  private final UserService userService;

  public DegreeProgramServiceImpl(DegreeProgramRepository degreeProgramRepository,
      AdminSettingService adminSettingService, UserService userService) {
    this.degreeProgramRepository = degreeProgramRepository;
    this.adminSettingService = adminSettingService;
    this.userService = userService;
  }

  @Override
  public DegreeProgramsWithPeriodDto getDegreeProgramsPresentations() {
    List<DegreeProgramPresentation> degreePresantations = degreeProgramRepository.findAll().stream()
        .map(DegreeProgramDto::fromEntity)
        .map(DegreeProgramDto::toPresentation)
        .toList();
    PeriodSettingsDto periodSettings = adminSettingService.getPeriodSettings();

    return new DegreeProgramsWithPeriodDto(degreePresantations, periodSettings.workDate());
  }

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
    degreeProgramRepository.save(degreeProgramDto.toEntity(user));
  }

  @Override
  public void deleteDegreeProgram(Long id) {
    degreeProgramRepository.deleteById(id);
  }
}
