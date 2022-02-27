package com.hexaphor.liveclass.handel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hexaphor.liveclass.exception.SchoolNotFoundException;
import com.hexaphor.liveclass.exception.StudentNotFoundxception;
import com.hexaphor.liveclass.exception.SubjectNotFoundException;
import com.hexaphor.liveclass.exception.UserNotFoundException;

@ControllerAdvice
public class OnlineLiveException {
	
	private Logger log=LoggerFactory.getLogger(OnlineLiveException.class);
	
	@ExceptionHandler(Exception.class)
	public String allException(Exception ex,Model model){
		log.error("Exception handel {}",ex.getLocalizedMessage());
		return "Error/error";
	}
	
	@ExceptionHandler(StudentNotFoundxception.class)
	public String handleStudentNotFoundxceptionEntity(StudentNotFoundxception unfe,Model model) {
		log.error("StudentNotFoundxception handel {}",unfe.getLocalizedMessage());
		return "Error/StudentError";
	}
	
	@ExceptionHandler(SchoolNotFoundException.class)
	public String handleSchoolNotFounEntity(SchoolNotFoundException unfe,Model model) {
		log.error("handleSchoolNotFounEntity handel {}",unfe.getLocalizedMessage());
		return "Error/SchoolError";
	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleDataIntegrityViolationException(DataIntegrityViolationException unfe,Model model) {
       model.addAttribute("message", unfe.getLocalizedMessage());
       log.error("handleDataIntegrityViolationException handel {}",unfe.getLocalizedMessage());
		return "Error/error";
	}
	
	@ExceptionHandler(SubjectNotFoundException.class)
	public String handleSubjectNotFounEntity(SubjectNotFoundException unfe,Model model) {
		log.error("handleSubjectNotFounEntity handel {}",unfe.getLocalizedMessage());
		return "Error/SubjectError";
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFounEntity(UserNotFoundException unfe,Model model) {
		log.error("User ot found handel {}",unfe.getLocalizedMessage());
		return "Error/UserError";
	}

}
