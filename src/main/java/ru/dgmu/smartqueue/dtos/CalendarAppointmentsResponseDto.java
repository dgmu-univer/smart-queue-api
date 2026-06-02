package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Ответ с массивом записей")
public record CalendarAppointmentsResponseDto(
    SlotSettingsDto slotSettings,
    List<SlotsWithPinsDto> slots
) {

}
