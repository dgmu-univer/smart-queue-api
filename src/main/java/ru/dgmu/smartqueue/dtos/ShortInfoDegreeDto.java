package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import ru.dgmu.smartqueue.entites.DegreeProgram;

@Schema(description = "Краткая информация программы обучения")
public record ShortInfoDegreeDto(
    @NotBlank(message = "Наличие имени обязательно")
    @Schema(description = "Название программы обучения")
    String name,
    @NotBlank(message = "Наличие описания обязательно")
    @Schema(description = "Описание программы обучения")
    String description
) {

  static ShortInfoDegreeDto fromEntity(DegreeProgram degreeProgram) {
    return new ShortInfoDegreeDto(
        degreeProgram.getName(),
        degreeProgram.getDescription()
    );
  }
}
