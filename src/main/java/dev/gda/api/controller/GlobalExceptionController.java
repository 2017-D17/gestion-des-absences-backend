package dev.gda.api.controller;

import dev.gda.api.exception.GlobalApiErrorEntity;
import dev.gda.api.exception.JourFerieException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler{
    
  @ExceptionHandler(JourFerieException.class)
  public ResponseEntity<Object> handleJourFerieException(JourFerieException ex) {
    GlobalApiErrorEntity gae = 
      new GlobalApiErrorEntity(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "");
    return new ResponseEntity<Object>(gae,HttpStatus.BAD_REQUEST);
  }

}