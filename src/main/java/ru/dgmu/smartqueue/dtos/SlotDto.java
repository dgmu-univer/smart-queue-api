package ru.dgmu.smartqueue.dtos;

import java.time.LocalDateTime;
import java.util.List;
import ru.dgmu.smartqueue.entites.DegreeProgram;
import ru.dgmu.smartqueue.entites.Slot;

public record SlotDto(
    Long id,
    LocalDateTime startTimeAt,
    LocalDateTime endTimeAt
) {

  public static SlotDto fromEntity(Slot slot) {
    return new SlotDto(
        slot.getId(),
        slot.getStartTimeAt(),
        slot.getEndTimeAt()
    );
  }

  public Slot toEntity(DegreeProgram degreeProgram) {
    return new Slot(id, degreeProgram, startTimeAt, endTimeAt, List.of());
  }
}
