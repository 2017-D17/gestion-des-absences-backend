package dev.gda.api.exception;

public class AbsenceException extends RuntimeException {

  private static final long serialVersionUID = 7600026050146219291L;

  public AbsenceException() {
    super();
  }

  public AbsenceException(String message) {
    super(message);
  }

}