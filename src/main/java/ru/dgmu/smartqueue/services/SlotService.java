package ru.dgmu.smartqueue.services;

import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;
import ru.dgmu.smartqueue.dtos.SlotResponse;

public interface SlotService {

  SlotResponse getSlotsByFilter(SlotFilter filter);
}
