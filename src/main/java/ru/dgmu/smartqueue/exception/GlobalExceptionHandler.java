package ru.dgmu.smartqueue.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
  public ResponseEntity<ApiError> handleAuthenticationFailedException(
      AuthenticationFailedException e) {
    log.warn("Authentication failed: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiError(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
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
  public ResponseEntity<ApiError> handleAuthorizationDeniedException(
      AuthorizationDeniedException e) {
    log.warn("Access denied: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ApiError("Доступ запрещен", HttpStatus.FORBIDDEN.value()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.warn("Invalid request format: {}", e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError("Неверный формат запроса", HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    log.warn("Validation failed for request");
    return ResponseEntity.badRequest()
        .body(new ApiError(ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("Invalid argument: {}", e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ApiError("Неверные параметры запроса", HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiError> handleNotAllowedMethodException(HttpRequestMethodNotSupportedException e) {
    log.error("Not allowed method", e);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(new ApiError("HTTP метод не поддерживается", HttpStatus.METHOD_NOT_ALLOWED.value()));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiError> handleNoResourceException(NoResourceFoundException e) {
    log.error("Not found resource", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiError("Запрашиваемый ресурс не найден", HttpStatus.NOT_FOUND.value()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception e) {
    log.error("Unexpected error occurred", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiError("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
