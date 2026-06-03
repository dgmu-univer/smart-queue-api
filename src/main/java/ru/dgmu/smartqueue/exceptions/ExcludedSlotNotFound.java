package ru.dgmu.smartqueue.exceptions;

public class ExcludedSlotNotFound extends BusinessException {

  public ExcludedSlotNotFound(String message) {
    super(message);
  }

  public ExcludedSlotNotFound(String message, Throwable cause) {
    super(message, cause);
  }
}
