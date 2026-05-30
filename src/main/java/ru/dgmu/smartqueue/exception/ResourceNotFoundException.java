package ru.dgmu.smartqueue.exception;

import ru.dgmu.smartqueue.enums.RESOURCE;

public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(RESOURCE resource, Object id) {
    super(String.format("Ресурс: '%s' с идентификатором: %s не найден", resource.getDisplayName(), id));
  }
}