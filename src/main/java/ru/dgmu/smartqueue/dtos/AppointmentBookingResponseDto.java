package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dgmu.smartqueue.services.impl.AppointmentServiceImpl.ExistingResponseStatus;

@Schema(description = "Ответ на запись в слот")
public record AppointmentBookingResponseDto(
    @Schema(description = "Идентификатор записи")
    Long id,
    @Schema(description = "Статус записи")
    ExistingResponseStatus status
) {

}
