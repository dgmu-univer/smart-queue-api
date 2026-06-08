package ru.dgmu.smartqueue.exceptions;

public class VerifiedAppointmentByNumberAlreadyExists extends BusinessException {

  private static final String MESSAGE = "Верифицированный запись с таким номером телефона уже существует";

  public VerifiedAppointmentByNumberAlreadyExists() {
    super(MESSAGE);
  }
}
