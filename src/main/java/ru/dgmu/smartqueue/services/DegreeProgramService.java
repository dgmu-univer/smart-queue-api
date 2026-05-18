package ru.dgmu.smartqueue.services;

import java.util.List;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;

public interface DegreeProgramService {
  List<DegreeProgramDto> getDegreePrograms();
  void saveDegreeProgram(DegreeProgram degreeProgram);
  DegreeProgramDto findDegreeProgramByUserId(User user);
  void deleteDegreeProgramByUserId(Long id);
}