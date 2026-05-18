package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto.DegreeProgramPresentation;

@Schema(description = "Список программ с интервалом дат приема")
public record DegreeProgramsWithPeriodDto(
    List<DegreeProgramPresentation> degreePrograms,
    PeriodSettingsDto.Period periodSettings
) {

}