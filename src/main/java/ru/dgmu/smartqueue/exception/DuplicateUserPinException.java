package ru.dgmu.smartqueue.exception;

public class DuplicateUserPinException extends BusinessException {

  private static final String MESSAGE = "Пин-код должен быть уникальным. Программа образования с таким пин-кодом уже существует";

  public DuplicateUserPinException() {
    super(MESSAGE);
  }

  public DuplicateUserPinException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
