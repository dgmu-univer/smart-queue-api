package ru.dgmu.smartqueue.exception;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusinessException(BusinessException e) {
    log.warn("Business exception: {}", e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e) {
    log.warn("Resource not found: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiError(e.getMessage(), HttpStatus.NOT_FOUND.value()));
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiError> handleValidationException(ValidationException e) {
    log.warn("Validation error: {}", e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ApiError> handleAuthenticationFailedException(AuthenticationFailedException e) {
    log.warn("Authentication failed: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiError(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
    log.warn("Entity not found: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiError("Запрашиваемый ресурс не найден", HttpStatus.NOT_FOUND.value()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("Data integrity violation", e);
    String message = "Нарушение целостности данных. Возможно, запись уже существует";
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiError(message, HttpStatus.CONFLICT.value()));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e) {
    log.warn("Authentication error: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiError("Неверные учетные данные", HttpStatus.UNAUTHORIZED.value()));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ApiError> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
    log.warn("Access denied: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ApiError("Доступ запрещен", HttpStatus.FORBIDDEN.value()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.warn("Invalid request format: {}", e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError("Неверный формат запроса", HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    log.warn("Validation failed for request");
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("Invalid argument: {}", e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError("Неверные параметры запроса", HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception e) {
    log.error("Unexpected error occurred", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiError("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
