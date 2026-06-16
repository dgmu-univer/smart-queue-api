package ru.dgmu.smartqueue.exceptions;

public class NumberWasNotFoundException extends BusinessException {

  private static final String MESSAGE = "Указанный номер не сущетсвует";

  public NumberWasNotFoundException() {
    super(MESSAGE);
  }

  public NumberWasNotFoundException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
