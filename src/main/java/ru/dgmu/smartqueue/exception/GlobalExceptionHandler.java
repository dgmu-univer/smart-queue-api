package ru.dgmu.smartqueue.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handle(HttpMessageNotReadableException e) {
    log.error("Invalid request", e);
    return ResponseEntity.badRequest().body("Invalid request");
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handle(RuntimeException e) {
    log.error("Internal exception was thrown", e);
    return ResponseEntity.internalServerError().body("Internal server error");
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<String> handle(AuthorizationDeniedException e) {
    log.error("Access denied", e);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
  }
}
