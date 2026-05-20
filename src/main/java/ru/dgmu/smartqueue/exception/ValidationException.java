package ru.dgmu.smartqueue.exception;

/**
 * Исключение для случаев нарушения бизнес-правил
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}