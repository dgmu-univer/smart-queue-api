package ru.dgmu.smartqueue.dtos;

import java.time.LocalDateTime;
import java.time.ZoneId;
import ru.dgmu.smartqueue.entites.Appointment;

public record AppointmentDto(
    Long id,
    String pin,
    String phone,
    Boolean isVerified,
    LocalDateTime requestedAt,
    SlotDto slot,
    ShortInfoDegreeDto degree
) {

  public static AppointmentDto fromEntity(Appointment entity) {
    return new AppointmentDto(
        entity.getId(),
        entity.getPin(),
        entity.getPhone(),
        entity.getIsVerified(),
        entity.getRequestedAt()
            .atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of("Europe/Moscow")).toLocalDateTime(),
        SlotDto.fromEntity(entity.getSlot()),
        ShortInfoDegreeDto.fromEntity(entity.getSlot().getDegreeProgram())
    );
  }

}
