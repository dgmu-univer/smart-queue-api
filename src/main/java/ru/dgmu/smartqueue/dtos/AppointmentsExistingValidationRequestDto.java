package ru.dgmu.smartqueue.dtos;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import ru.dgmu.smartqueue.validators.phone.Phone;

@Schema(description = "Запрос на проверку существования записей по номеру телефона и идентификатору программы образования")
public record AppointmentsExistingValidationRequestDto(

    @Schema(description = "Идентификатор программы")
    @NotNull(message = "'Идентификатор программы' не может быть пустым")
    Long degreeId,

    @Schema(description = "Номер телефона", example = "79999999999")
    @NotBlank(message = "'Номер телефона' не может быть пустым")
    @Phone
    String phone
) {

  private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  @SneakyThrows
  public AppointmentsExistingValidationRequestDto(
      @Schema(description = "Запрос на проверку существования записей по номеру телефона и идентификатору программы образования")
      @NotNull(message = "'Идентификатор программы' не может быть пустым")
      Long degreeId,
      @Schema(description = "Номер телефона", example = "79999999999")
      @NotBlank(message = "'Номер телефона' не может быть пустым")
      @Phone
      String phone) {
    this.degreeId = degreeId;
    this.phone = String.valueOf(phoneUtil.parse(phone, "RU").getNationalNumber());
  }
}
