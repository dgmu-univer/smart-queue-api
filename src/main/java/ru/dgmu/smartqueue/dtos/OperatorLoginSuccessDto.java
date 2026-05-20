package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ успешной аутентификации оператора")
public record OperatorLoginSuccessDto(
    String fio,
    String username,
    String role
) {

}
