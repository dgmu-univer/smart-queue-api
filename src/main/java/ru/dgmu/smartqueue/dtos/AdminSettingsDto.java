package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.entites.DegreeProgram;

@Schema(description = "Настройки администратора")
public record AdminSettingsDto(
    @NotNull(message = "Наличие настроек периода обязательно")
    PeriodSettingsDto periodSettings,
    @NotNull(message = "Наличие настроек слотов обязательно")
    SlotSettingsDto slotSettings,
    @NotNull(message = "Наличие настроек не рабочих дней обязательно")
    List<LocalDate> nonWorkingDays,
    @NotNull(message = "Наличие настроек исклченных слотов обязательно")
    List<ExcludedSlotSettingsDto> excludedSlotSettings
) {

    AdminSetting toEntity(DegreeProgram degreeProgram) {
        return new AdminSetting(
            null,
            slotSettings.durationMinutes(),
            slotSettings.capacityPerSlot(),
            periodSettings.getWorkDate().getStartDate(),
            periodSettings.getWorkDate().getEndDate(),
            periodSettings.getWorkTime().getStartTime(),
            periodSettings.getWorkTime().getEndTime(),
            periodSettings.getLunch().getStartTime(),
            periodSettings.getLunch().getEndTime(),
            nonWorkingDays,
            excludedSlotSettings.stream().map(ExcludedSlotSettingsDto::toEntity).toList(),
            degreeProgram
        );
    }

    public static AdminSettingsDto fromEntity(AdminSetting entity) {
        return new AdminSettingsDto(
            PeriodSettingsDto.fromEntity(entity),
            SlotSettingsDto.fromEntity(entity),
            entity.getNonWorkingDays(),
            entity.getExcludedSlots().stream().map(ExcludedSlotSettingsDto::fromEntity).toList()
        );
    }
}
