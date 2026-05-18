package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;

@Schema(description = "Запрос на создание программы образовния")
public record DegreeProgramDto(
    Long id,
    @NotBlank(message = "Наличие имени обязательно")
    String name,
    @NotBlank(message = "Наличие описания обязательно")
    String description,
    @NotBlank(message = "Наличие пин-кода обязательно")
    @Size(min = 6, max = 6, message = "Пин должен состоять из 6 сиволов")
    String pin
) {

  public DegreeProgramPresentation toPresentation() {
    return new DegreeProgramPresentation(id, name, description);
  }

  public DegreeProgram toEntity(User user) {
    return new DegreeProgram(null, name, description, user);
  }

  public static DegreeProgramDto fromEntity(DegreeProgram degreeProgram) {
    return new DegreeProgramDto(
        degreeProgram.getId(),
        degreeProgram.getName(),
        degreeProgram.getDescription(),
        degreeProgram.getUserId().getPin()
    );
  }

  @Schema(description = "Ответ с ифорацией оператора")
  public record DegreeProgramPresentation(
      Long id,
      String name,
      String description
  ) {

  }
}
