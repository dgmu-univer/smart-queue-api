package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Запрос на аутентификацию по pin")
public record SignInOperatorDto(
    @Schema(name = "Имя пользователя", example = "Ruslan")
    @Size(min = 6, max = 6, message = "Pin должен состоять из 6 символов")
    @NotBlank(message = "Имя обязательно для ввода")
    String pin
) {

}