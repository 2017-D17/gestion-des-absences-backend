package dev.gda.api.exception;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

public class GlobalApiErrorEntity {

  private HttpStatus status;
  private String message;
  private List<String> errors;

  public GlobalApiErrorEntity(HttpStatus status, String message, String error) {
    super();
    this.status = status;
    this.message = message;
    this.errors = Arrays.asList(error);
  }
  
  public GlobalApiErrorEntity(HttpStatus status, String message, List<String> errors) {
    super();
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

  /**
   * @return the status
   */
  public HttpStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the error
   */
  public List<String> getErrors() {
    return errors;
  }

  /**
   * @param error the error to set
   */
  public void setError(List<String> errors) {
    this.errors = errors;
  }
  
  
}
