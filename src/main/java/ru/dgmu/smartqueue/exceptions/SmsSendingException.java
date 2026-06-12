package ru.dgmu.smartqueue.exceptions;

public class SmsSendingException extends BusinessException {

  private static final String MESSAGE = "Ошибка отправки СМС";

  public SmsSendingException() {
    super(MESSAGE);
  }

  public SmsSendingException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
