package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

public record ExcludedSlotSettingsDto(
    @JsonProperty("id")
    @With
    Long id,
    @JsonProperty("date")
    String date,
    @JsonProperty("start_time")
    String startTime,
    @JsonProperty("end_time")
    String endTime
) {

}
