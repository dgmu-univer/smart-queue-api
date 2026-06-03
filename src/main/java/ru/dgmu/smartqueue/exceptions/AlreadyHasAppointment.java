package ru.dgmu.smartqueue.exceptions;

public class AlreadyHasAppointment extends BusinessException {

  private static final String MESSAGE = "По данному номеру уже была совершена запись";

  public AlreadyHasAppointment() {
    super(MESSAGE);
  }

  public AlreadyHasAppointment(Throwable cause) {
    super(MESSAGE, cause);
  }
}
