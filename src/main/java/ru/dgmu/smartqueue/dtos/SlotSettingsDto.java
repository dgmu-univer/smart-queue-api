package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.dgmu.smartqueue.entites.AdminSetting;

public record SlotSettingsDto(
    @JsonProperty("duration_minutes")
    Integer durationMinutes,
    @JsonProperty("capacity_per_slot")
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
