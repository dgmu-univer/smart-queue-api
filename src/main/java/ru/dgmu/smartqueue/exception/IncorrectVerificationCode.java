package ru.dgmu.smartqueue.exception;

public class IncorrectVerificationCode extends BusinessException {

  public IncorrectVerificationCode(String message) {
    super(message);
  }

  public IncorrectVerificationCode(String message, Throwable cause) {
    super(message, cause);
  }
}
