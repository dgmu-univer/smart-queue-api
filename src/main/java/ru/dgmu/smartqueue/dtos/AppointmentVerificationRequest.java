package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос верификации записи")
public record AppointmentVerificationRequest(
    Long id,
    String verificationCode
) {

}
