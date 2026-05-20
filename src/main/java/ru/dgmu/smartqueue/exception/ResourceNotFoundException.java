package ru.dgmu.smartqueue.exception;

/**
 * Исключение для случаев, когда запрашиваемый ресурс не найден
 */
public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceType, Object id) {
    super(String.format("%s с ID %s не найден", resourceType, id));
  }
}