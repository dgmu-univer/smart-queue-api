package ru.dgmu.smartqueue.dtos;

import ru.dgmu.smartqueue.entites.Slot;

public record AppointmentDto(
    Long id,
    String pin,
    Slot slotId
) {

}
