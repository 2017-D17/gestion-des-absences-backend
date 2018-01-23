package dev.gda.api.controller;

import dev.gda.api.exception.AbsenceException;
import dev.gda.api.exception.GlobalApiErrorEntity;
import dev.gda.api.exception.JourFerieException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler{
  
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({AbsenceException.class, JourFerieException.class})
  public ResponseEntity<Object> handleJourFerieException(Exception ex) {
    GlobalApiErrorEntity gae = 
      new GlobalApiErrorEntity(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "");
    return new ResponseEntity<>(gae,HttpStatus.BAD_REQUEST);
  }
  
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, 
    HttpHeaders headers, 
    HttpStatus status, 
    WebRequest request) {
      List<String> errors = new ArrayList<>();
      for (FieldError error : ex.getBindingResult().getFieldErrors()) {
          errors.add(error.getField() + ": " + error.getDefaultMessage());
      }
      for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
          errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
      }
      GlobalApiErrorEntity gae = 
        new GlobalApiErrorEntity(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
      
      return handleExceptionInternal(ex, gae, headers, gae.getStatus(), request);
  }
  
  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
    MethodArgumentTypeMismatchException ex, WebRequest request) {
      String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
   
      GlobalApiErrorEntity apiError = 
        new GlobalApiErrorEntity(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
      return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

}