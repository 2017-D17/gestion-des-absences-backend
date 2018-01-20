package dev.gda.api.controller;

import dev.gda.api.exception.JourFerieException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionController {
    
  @ExceptionHandler(JourFerieException.class)
  public void handleJourFerieException(Exception ex, HttpServletResponse resp) {
    resp.setStatus(400);
  }
  

}