package ru.dgmu.smartqueue.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;

public record SlotResponseDto(
    @JsonFormat(pattern = "HH:mm")
    List<LocalTime> slots
) {

}
