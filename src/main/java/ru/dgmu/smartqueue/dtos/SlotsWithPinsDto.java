package ru.dgmu.smartqueue.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import ru.dgmu.smartqueue.entites.Appointment;
import ru.dgmu.smartqueue.entites.Slot;

@Schema(description = "Слоты с пин-кодами")
public record SlotsWithPinsDto(
    @Schema(description = "Время начала приема по записей")
    LocalDateTime start,
    @Schema(description = "Время конца приема по записей")
    LocalDateTime end,
    @Schema(description = "Пин коды по слоту")
    List<String> pins
) implements Comparable<SlotsWithPinsDto> {

  public static SlotsWithPinsDto build(Map.Entry<Slot, List<Appointment>> slotWithAppointment) {
    return new SlotsWithPinsDto(
        slotWithAppointment.getKey().getStartTimeAt(),
        slotWithAppointment.getKey().getEndTimeAt(),
        slotWithAppointment.getValue().stream().map(Appointment::getPin).toList()
    );
  }

  @Override
  public int compareTo(SlotsWithPinsDto o) {
    if (o == null) {
      throw new NullPointerException("Cannot compare to a null SlotsWithPinsDto");
    }
    return this.start.compareTo(o.start);
  }
}
