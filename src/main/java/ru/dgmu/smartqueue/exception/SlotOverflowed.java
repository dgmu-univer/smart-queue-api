package ru.dgmu.smartqueue.exception;

public class SlotOverflowed extends BusinessException {

  public SlotOverflowed(String message) {
    super(message);
  }

  public SlotOverflowed(String message, Throwable cause) {
    super(message, cause);
  }
}
