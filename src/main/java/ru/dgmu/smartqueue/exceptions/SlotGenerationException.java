package ru.dgmu.smartqueue.exceptions;

public class SlotGenerationException extends BusinessException {

  public SlotGenerationException(String message) {
    super(message);
  }

  public SlotGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
