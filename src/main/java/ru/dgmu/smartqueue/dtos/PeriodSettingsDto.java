package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dgmu.smartqueue.entites.AdminSetting;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Настройки периода приемки")
public class PeriodSettingsDto {

  @JsonProperty("work_date")
  private Period workDate;

  @JsonProperty("work_time")
  private WorkingTime workTime;

  @JsonProperty("lunch")
  private Lunch lunch;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Настройки дат периода приемки")
  public static class Period {

    @JsonProperty("start_date")
    @Schema(description = "Дата начала приемки")
    private LocalDate startDate;
    @JsonProperty("end_date")
    @Schema(description = "Дата окончания приемки")
    private LocalDate endDate;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Настройки времени приемки")
  public static class WorkingTime {

    @JsonProperty("start_time")
    @Schema(description = "Время начала приемки")
    private LocalTime startTime;
    @JsonProperty("end_time")
    @Schema(description = "Время окнчания приемки")
    private LocalTime endTime;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Настройки времени обеда приемки")
  public static class Lunch {

    @JsonProperty("start_time")
    @Schema(description = "Время начала обеда")
    private LocalTime startTime;
    @JsonProperty("end_time")
    @Schema(description = "Время окончания обеда")
    private LocalTime endTime;
  }

  public static PeriodSettingsDto fromEntity(AdminSetting setting) {
    return new PeriodSettingsDto(
        new Period(setting.getWorkStartDate(), setting.getWorkEndDate()),
        new WorkingTime(setting.getWorkStartTime(), setting.getWorkEndTime()),
        new Lunch(setting.getLunchStartTime(), setting.getLunchEndTime())
    );
  }
}
