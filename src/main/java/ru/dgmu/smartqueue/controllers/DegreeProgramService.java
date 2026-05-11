package ru.dgmu.smartqueue.controllers;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
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

  public void saveDegreeProgram(DegreeProgram degreeProgram) {
    degreeProgramRepository.save(degreeProgram);
  }
}
