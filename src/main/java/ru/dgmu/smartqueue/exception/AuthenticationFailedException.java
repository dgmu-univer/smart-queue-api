package ru.dgmu.smartqueue.exception;

/**
 * Исключение для ошибок аутентификации
 */
public class AuthenticationFailedException extends BusinessException {

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}