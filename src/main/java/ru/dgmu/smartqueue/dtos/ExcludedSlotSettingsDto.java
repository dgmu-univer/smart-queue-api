package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.With;
import ru.dgmu.smartqueue.entites.ExcludedSlot;

@Schema(description = "Данные исключенного слота")
public record ExcludedSlotSettingsDto(
    @JsonProperty("id")
    @Schema(description = "Идентификатор исключенного слота")
    @With
    Long id,
    @JsonProperty("date")
    @Schema(description = "Дата исключенного слота")
    LocalDate date,
    @JsonProperty("start_time")
    @Schema(description = "Время начала исключенного слота")
    LocalTime startTime,
    @JsonProperty("end_time")
    @Schema(description = "Время окончания исключенного слота")
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
