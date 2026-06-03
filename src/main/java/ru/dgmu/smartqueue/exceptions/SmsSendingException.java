package ru.dgmu.smartqueue.exceptions;

public class SmsSendingException extends BusinessException {

  private static final String MESSAGE = "По данному номеру уже была совершена запись";

  public SmsSendingException() {
    super(MESSAGE);
  }

  public SmsSendingException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
