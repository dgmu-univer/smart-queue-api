package ru.dgmu.smartqueue.dtos;

import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;

public record DegreeProgramDto(
    String name,
    String description,
    String pin
) {

  public DegreeProgramPresentation toPresentation() {
    return new DegreeProgramPresentation(name, description);
  }

  public DegreeProgram toEntity(User user) {
    return new DegreeProgram(name, description, user);
  }

  public static DegreeProgramDto fromEntity(DegreeProgram degreeProgram) {
    return new DegreeProgramDto(
        degreeProgram.getName(),
        degreeProgram.getDescription(),
        null
    );
  }

  public record DegreeProgramPresentation(
      String name,
      String description
  ) {

  }
}
