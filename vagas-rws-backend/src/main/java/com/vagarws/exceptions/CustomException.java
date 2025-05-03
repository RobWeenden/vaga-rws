package com.vagarws.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class CustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Getter
	private final HttpStatus status;

	@Getter
	private String message;

	public CustomException(String message, HttpStatus status) {
		super(message);
		this.message = message;
		this.status = status;
	}

}
