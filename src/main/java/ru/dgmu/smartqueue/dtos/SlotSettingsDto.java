package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import ru.dgmu.smartqueue.entites.AdminSetting;

@Schema(description = "Обновленные настройки слотов")
public record SlotSettingsDto(
    @JsonProperty("duration_minutes")
    @Min(value = 1, message = "Длительность приема не может быть меньше 1 минут")
    @Max(value = 60, message = "Длительность приема не может быть больше 60 минут")
    @Schema(description = "Длительность приема в минутах")
    Integer durationMinutes,
    @JsonProperty("capacity_per_slot")
    @Min(value = 1, message = "Вместимость слота не может быть меньше 1")
    @Max(value = 50, message = "Вместимость слота не может быть больше 50")
    @Schema(description = "Вместимость слота")
    Integer capacityPerSlot
) {

  public SlotSettingsDto merge(SlotSettingsDto patch) {
    return new SlotSettingsDto(
        patch.durationMinutes() != null ? patch.durationMinutes() : this.durationMinutes(),
        patch.capacityPerSlot() != null ? patch.capacityPerSlot() : this.capacityPerSlot()
    );
  }

  public static SlotSettingsDto fromEntity(AdminSetting entity) {
    return new SlotSettingsDto(
        entity.getDurationMinutes(),
        entity.getCapacityPerSlot()
    );
  }

}
