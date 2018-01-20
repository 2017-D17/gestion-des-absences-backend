package dev.gda.api.controller;

import dev.gda.api.exception.AbsenceException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalExceptionController {
		
	@ExceptionHandler(AbsenceException.class)
	public ModelAndView handleAbsenceException(Exception ex, HttpServletResponse resp) {

		resp.setStatus(400);
		return null;
	}
	

}
