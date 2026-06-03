package ru.dgmu.smartqueue.exceptions;

public class SlotOverflowed extends BusinessException {

  public SlotOverflowed(String message) {
    super(message);
  }

  public SlotOverflowed(String message, Throwable cause) {
    super(message, cause);
  }
}
