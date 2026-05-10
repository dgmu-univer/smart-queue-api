package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SlotSettingsDto(
    @JsonProperty("duration_minutes")
    int durationMinutes,
    @JsonProperty("capacity_per_slot")
    int capacityPerSlot
) {

}
