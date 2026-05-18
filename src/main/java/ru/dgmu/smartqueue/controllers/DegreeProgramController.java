package ru.dgmu.smartqueue.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.services.DegreeProgramService;

@RestController
@RequestMapping("/degree-program")
public class DegreeProgramController {

  private final DegreeProgramService degreeProgramService;

  public DegreeProgramController(DegreeProgramService degreeProgramService) {
    this.degreeProgramService = degreeProgramService;
  }

  public List<DegreeProgramDto> getDegreePrograms() {
    return degreeProgramService.getDegreePrograms();
  }
}