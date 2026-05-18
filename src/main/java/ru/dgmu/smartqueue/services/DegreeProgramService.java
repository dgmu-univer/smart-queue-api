package ru.dgmu.smartqueue.services;

import java.util.List;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramsWithPeriodDto;
import ru.dgmu.smartqueue.entites.User;

public interface DegreeProgramService {

  DegreeProgramsWithPeriodDto getDegreeProgramsPresentations();

  List<DegreeProgramDto> getDegreePrograms();

  void createDegreeProgram(DegreeProgramDto degreeProgramDto);

  DegreeProgramDto findDegreeProgramByUserId(User user);

  void deleteDegreeProgram(Long id);


}