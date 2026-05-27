package ru.dgmu.smartqueue.exception;

public class SlotExpired extends BusinessException {

  public SlotExpired(String message) {
    super(message);
  }

  public SlotExpired(String message, Throwable cause) {
    super(message, cause);
  }
}
