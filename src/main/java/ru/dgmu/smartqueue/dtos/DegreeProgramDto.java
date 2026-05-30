package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto.Lunch;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto.Period;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto.WorkingTime;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.User;

@Schema(description = "Запрос на создание программы образовния")
public record DegreeProgramDto(
    Long id,
    @NotBlank(message = "Наличие имени обязательно")
    String name,
    @NotBlank(message = "Наличие описания обязательно")
    String description,
    @NotBlank(message = "Наличие пин-кода обязательно")
    @Size(min = 6, max = 6, message = "Пин должен состоять из 6 сиволов")
    String pin,
    @NotNull(message = "Наличие настроек обязательно")
    @Valid
    AdminSettingsDto settings
) {

  public static DegreeProgramDto fromEntity(DegreeProgram degreeProgram) {
    AdminSetting adminSettings = degreeProgram.getAdminSettings();
    AdminSettingsDto adminSettingsDto = new AdminSettingsDto(
        new PeriodSettingsDto(
            new Period(adminSettings.getWorkStartDate(), adminSettings.getWorkEndDate()),
            new WorkingTime(adminSettings.getWorkStartTime(), adminSettings.getWorkEndTime()),
            new Lunch(adminSettings.getLunchStartTime(), adminSettings.getLunchEndTime())),
        new SlotSettingsDto(adminSettings.getDurationMinutes(), adminSettings.getCapacityPerSlot()),
        adminSettings.getNonWorkingDays(),
        adminSettings.getExcludedSlots().stream()
            .map(ExcludedSlotSettingsDto::fromEntity)
            .toList());

    return new DegreeProgramDto(
        degreeProgram.getId(),
        degreeProgram.getName(),
        degreeProgram.getDescription(),
        degreeProgram.getUserId().getPin(),
        adminSettingsDto
    );
  }

  public DegreeProgramPresentation toPresentation() {
    return new DegreeProgramPresentation(id, name, description,
        settings.periods().getWorkDate());
  }

  public DegreeProgram toEntity(User user) {
    DegreeProgram degreeProgram = new DegreeProgram(null, name, description, user, null);
    degreeProgram.setAdminSettings(settings.toEntity(degreeProgram));
    return degreeProgram;
  }

  @Schema(description = "Ответ с ифорацией оператора")
  public record DegreeProgramPresentation(
      Long id,
      String name,
      String description,
      PeriodSettingsDto.Period periodSettings
  ) {

  }
}
