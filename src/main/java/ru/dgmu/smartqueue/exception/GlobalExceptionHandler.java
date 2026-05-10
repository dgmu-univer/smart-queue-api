package ru.dgmu.smartqueue.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleIllegalArgumentException(RuntimeException e) {
    log.error("Internal exception was thrown", e);
    return ResponseEntity.internalServerError().body(e.getMessage());
  }
}
