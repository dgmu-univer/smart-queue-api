package ru.dgmu.smartqueue.exceptions;

public class SlotExpired extends BusinessException {

  public SlotExpired(String message) {
    super(message);
  }

  public SlotExpired(String message, Throwable cause) {
    super(message, cause);
  }
}
