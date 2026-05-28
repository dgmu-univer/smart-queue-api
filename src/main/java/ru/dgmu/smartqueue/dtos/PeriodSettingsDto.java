package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dgmu.smartqueue.entites.AdminSetting;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
  public static class Period {

    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WorkingTime {

    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_time")
    private LocalTime endTime;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Lunch {

    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_time")
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
