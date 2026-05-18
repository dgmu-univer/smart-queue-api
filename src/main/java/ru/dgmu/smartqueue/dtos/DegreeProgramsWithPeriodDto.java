package ru.dgmu.smartqueue.dtos;

import java.util.List;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto.DegreeProgramPresentation;

public record DegreeProgramsWithPeriodDto(
    List<DegreeProgramPresentation> degreePrograms,
    PeriodSettingsDto.Period periodSettings
) {

}