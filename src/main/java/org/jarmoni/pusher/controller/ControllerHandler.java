package org.jarmoni.pusher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerHandler {

	private final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(final Exception ex) {
		this.logger.info("Exception in Controller occured", ex);
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

}
