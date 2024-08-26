package com.aupnmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Sapmming is not allowed. Please retry otp generation after sometime!!!")
public class spammingException extends RuntimeException {

	String message;

	public spammingException(String message) {
		super(message);
	}

}
