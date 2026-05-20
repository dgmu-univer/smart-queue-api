package ru.dgmu.smartqueue.exception;

/**
 * Базовое исключение для бизнес-логики приложения
 */
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }
}