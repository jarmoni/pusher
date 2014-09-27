package org.jarmoni.pusher.controller;

import org.jarmoni.restxe.common.Representation;
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
	public ResponseEntity<Representation<Object>> handleException(final Exception ex) {
		this.logger.info("Exception in Controller occured", ex);
		final Representation<Object> representation = Representation.builder().errorMessage(ex.getMessage()).build();
		return new ResponseEntity<Representation<Object>>(representation, HttpStatus.BAD_REQUEST);
	}

}
