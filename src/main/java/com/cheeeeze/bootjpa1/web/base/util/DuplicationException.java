package com.cheeeeze.bootjpa1.web.base.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicationException extends RuntimeException {

	private final HttpStatus httpStatus;
	private String message;
	
	public DuplicationException( String message ) {
		this.httpStatus = HttpStatus.CONFLICT;
		this.message = message;
	}
}
