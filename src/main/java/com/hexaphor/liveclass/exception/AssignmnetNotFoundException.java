package com.hexaphor.liveclass.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssignmnetNotFoundException extends RuntimeException {

	public AssignmnetNotFoundException() {
		super();
	}

	public AssignmnetNotFoundException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
