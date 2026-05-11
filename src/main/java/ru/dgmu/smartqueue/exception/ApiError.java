package ru.dgmu.smartqueue.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {

  @JsonProperty("message")
  private final String message;

  public ApiError(String message) {
    this.message = message;
  }
}
