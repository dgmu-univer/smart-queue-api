package ru.dgmu.smartqueue.exception;

import ru.dgmu.smartqueue.enums.Resource;

public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(Resource resource, Object id) {
    super(String.format("Ресурс: '%s' с идентификатором: %s не найден", resource.getDisplayName(), id));
  }
}