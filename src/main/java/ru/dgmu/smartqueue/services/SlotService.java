package ru.dgmu.smartqueue.services;

import java.time.LocalTime;
import java.util.List;
import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;

public interface SlotService {

  List<LocalTime> getSlotsByFilter(SlotFilter filter);
}
