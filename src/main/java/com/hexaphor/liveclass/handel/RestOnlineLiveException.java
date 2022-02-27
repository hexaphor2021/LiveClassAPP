package com.hexaphor.liveclass.handel;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hexaphor.liveclass.exception.SchoolNotFoundException;
import com.hexaphor.liveclass.exception.SubjectNotFoundException;
import com.hexaphor.liveclass.exception.UserNotFoundException;
import com.hexaphor.liveclass.model.ErrorInfo;

@RestControllerAdvice
public class RestOnlineLiveException {
	private Logger log=LoggerFactory.getLogger(RestOnlineLiveException.class);
	
	@ExceptionHandler(SchoolNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleSchoolNotFounEntity(SchoolNotFoundException unfe) {
		log.error(unfe.getLocalizedMessage());
		return new ResponseEntity<>(
				new ErrorInfo(new Date().toString(), "NOT FOUND", 404, unfe.getMessage(), "SCHOOL MODULE"),
				HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException unfe) {
		log.error(unfe.getLocalizedMessage());
		return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handelAllException(Exception unfe) {
		log.error(unfe.getLocalizedMessage());
		return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(SubjectNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleSubjectNotFounEntity(SubjectNotFoundException unfe) {
		log.error(unfe.getLocalizedMessage());
		return new ResponseEntity<>(
				new ErrorInfo(new Date().toString(), "NOT FOUND", 404, unfe.getMessage(), "SUBJECT MODULE"),
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleUserNotFounEntity(UserNotFoundException unfe) {
		log.error(unfe.getLocalizedMessage());
		return new ResponseEntity<>(
				new ErrorInfo(new Date().toString(), "NOT FOUND", 404, unfe.getMessage(), "User Login"),
				HttpStatus.NOT_FOUND);
	}

}
