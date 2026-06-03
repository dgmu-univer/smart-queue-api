package ru.dgmu.smartqueue.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ApiError {

  @JsonProperty("message")
  private final String message;

  @JsonProperty("timestamp")
  private final LocalDateTime timestamp;

  @JsonProperty("status")
  private final int status;

  public ApiError(String message, int status) {
    this.message = message;
    this.status = status;
    this.timestamp = LocalDateTime.now();
  }

  public ApiError(String message) {
    this(message, 500);
  }

  public String getMessage() {
    return message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }
}
