package com.keep.changes.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException {

	private String message;
	private HttpStatus status;
	private Boolean bool;

}
