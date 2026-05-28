package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.With;
import ru.dgmu.smartqueue.entites.ExcludedSlot;

public record ExcludedSlotSettingsDto(
    @JsonProperty("id")
    @With
    Long id,
    @JsonProperty("date")
    LocalDate date,
    @JsonProperty("start_time")
    LocalTime startTime,
    @JsonProperty("end_time")
    LocalTime endTime
) {

    public static ExcludedSlotSettingsDto fromEntity(ExcludedSlot entity) {
        return new ExcludedSlotSettingsDto(
            entity.getId(),
            entity.getDate(),
            entity.getStartTime(),
            entity.getEndTime()
        );
    }

    public ExcludedSlot toEntity() {
        return new ExcludedSlot(
            System.currentTimeMillis(),
            date,
            startTime,
            endTime
        );
    }

}
