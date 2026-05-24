package ru.dgmu.smartqueue.dtos;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.SneakyThrows;
import ru.dgmu.smartqueue.validators.phone.Phone;

@Schema(description = "Запрос на запись")
public record AppointmentsRequestDto(

    @Schema(description = "Дата записи")
    @NotNull(message = "'Дата записи' не может быть пустой")
    LocalDate date,

    @Schema(description = "Идентификатор программы")
    @NotNull(message = "'Идентификатор программы' не может быть пустым")
    Long degreeId,

    @Schema(description = "Время записи")
    @NotNull(message = "'Время записи' не может быть пустым")
    LocalTime time,

    @Schema(description = "Номер телефона", example = "79999999999")
    @NotBlank(message = "'Номер телефона' не может быть пустым")
    @Phone
    String phone
) {

  private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  @SneakyThrows
  public AppointmentsRequestDto(@Schema(description = "Дата записи")
  @NotNull(message = "'Дата записи' не может быть пустой")
  LocalDate date, @Schema(description = "Идентификатор программы")
  @NotNull(message = "'Идентификатор программы' не может быть пустым")
  Long degreeId, @Schema(description = "Время записи")
  @NotNull(message = "'Время записи' не может быть пустым")
  LocalTime time, @Schema(description = "Номер телефона", example = "79999999999")
  @NotBlank(message = "'Номер телефона' не может быть пустым")
  @Phone
  String phone) {
    this.date = date;
    this.degreeId = degreeId;
    this.time = time;
    this.phone = String.valueOf(phoneUtil.parse(phone, "RU").getNationalNumber());
  }
}
