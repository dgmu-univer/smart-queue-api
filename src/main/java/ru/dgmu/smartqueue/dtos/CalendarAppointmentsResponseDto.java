package ru.dgmu.smartqueue.dtos;

import java.time.LocalDateTime;

public record CalendarAppointmentsResponseDto(
    Long id,
    String titile,
    LocalDateTime start,
    LocalDateTime end
) {

}
