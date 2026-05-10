package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import java.util.function.BinaryOperator;

public record PeriodSettingsDto(
    @JsonProperty("work_date") Period workDate,
    @JsonProperty("work_time") WorkingTime workTime,
    @JsonProperty("lunch") Lunch lunch
) {

  public PeriodSettingsDto merge(PeriodSettingsDto patch) {
    return new PeriodSettingsDto(
        mergeOptional(this.workDate, patch.workDate, Period::merge),
        mergeOptional(this.workTime, patch.workTime, WorkingTime::merge),
        mergeOptional(this.lunch, patch.lunch, Lunch::merge)
    );
  }

  private <T> T mergeOptional(T current, T patch, BinaryOperator<T> mergerFunction) {
    return Optional.ofNullable(patch)
        .map(p -> Optional.ofNullable(current)
            .map(c -> mergerFunction.apply(c, p))
            .orElse(p))
        .orElse(current);
  }

  public record Period(
      @JsonProperty("start_date") String startDate,
      @JsonProperty("end_date") String endDate
  ) {

    public Period merge(Period patch) {
      return new Period(
          patch.startDate() != null ? patch.startDate() : this.startDate(),
          patch.endDate() != null ? patch.endDate() : this.endDate()
      );
    }
  }

  public record WorkingTime(
      @JsonProperty("start_time") String startTime,
      @JsonProperty("end_time") String endTime
  ) {

    public WorkingTime merge(WorkingTime patch) {
      return new WorkingTime(
          patch.startTime() != null ? patch.startTime() : this.startTime(),
          patch.endTime() != null ? patch.endTime() : this.endTime()
      );
    }
  }

  public record Lunch(
      @JsonProperty("start_time") String startTime,
      @JsonProperty("end_time") String endTime
  ) {

    public Lunch merge(Lunch patch) {
      return new Lunch(
          patch.startTime() != null ? patch.startTime() : this.startTime(),
          patch.endTime() != null ? patch.endTime() : this.endTime()
      );
    }
  }

}
