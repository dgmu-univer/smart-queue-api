package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PeriodSettingsDto(
    @JsonProperty("work_date")
    Period period,
    @JsonProperty("work_time")
    WorkingTime workingTime,
    @JsonProperty("lunch")
    Lunch lunch
) {

  public record Period(
      @JsonProperty("start_date") String startDate,
      @JsonProperty("end_date") String endDate
  ) {

  }

  public record WorkingTime(
      @JsonProperty("start_time") String startTime,
      @JsonProperty("end_time") String endTime
  ) {

  }

  public record Lunch(
      @JsonProperty("start_time") String startTime,
      @JsonProperty("end_time") String endTime
  ) {

  }
}
