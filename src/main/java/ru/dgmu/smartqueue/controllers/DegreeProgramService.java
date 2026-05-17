package ru.dgmu.smartqueue.controllers;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.repositories.DegreeProgramRepository;

@Service
public class DegreeProgramService {

  private final DegreeProgramRepository degreeProgramRepository;

  public DegreeProgramService(DegreeProgramRepository degreeProgramRepository) {
    this.degreeProgramRepository = degreeProgramRepository;
  }

  public List<DegreeProgramDto> getDegreePrograms() {
    return degreeProgramRepository.findAll().stream()
        .map(DegreeProgramDto::fromEntity)
        .toList();
  }

  @Transactional
  public void saveDegreeProgram(DegreeProgram degreeProgram) {
    degreeProgramRepository.save(degreeProgram);
  }

  public DegreeProgramDto findDegreeProgramByUserId(User user) {
    return DegreeProgramDto.fromEntity(degreeProgramRepository.findByUserId(user));
  }

  @Transactional
  public void deleteDegreeProgramByUserId(Long id) {
    degreeProgramRepository.deleteById(id);
  }
}
