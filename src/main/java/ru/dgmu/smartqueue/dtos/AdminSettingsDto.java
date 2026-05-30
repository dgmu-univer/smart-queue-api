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
    PeriodSettingsDto periods,
    @NotNull(message = "Наличие настроек слотов обязательно")
    SlotSettingsDto slots,
    @NotNull(message = "Наличие настроек не рабочих дней обязательно")
    List<LocalDate> nonWorkingDays,
    @NotNull(message = "Наличие настроек исклченных слотов обязательно")
    List<ExcludedSlotSettingsDto> excludedSlots
) {

    AdminSetting toEntity(DegreeProgram degreeProgram) {
        return new AdminSetting(
            null,
            slots.durationMinutes(),
            slots.capacityPerSlot(),
            periods.getWorkDate().getStartDate(),
            periods.getWorkDate().getEndDate(),
            periods.getWorkTime().getStartTime(),
            periods.getWorkTime().getEndTime(),
            periods.getLunch().getStartTime(),
            periods.getLunch().getEndTime(),
            nonWorkingDays,
            excludedSlots.stream().map(ExcludedSlotSettingsDto::toEntity).toList(),
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
