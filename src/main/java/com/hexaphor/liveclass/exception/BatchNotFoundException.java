package com.hexaphor.liveclass.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BatchNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BatchNotFoundException() {
		super();
	
	}

	public BatchNotFoundException(String message) {
		super(message);
	}

}
