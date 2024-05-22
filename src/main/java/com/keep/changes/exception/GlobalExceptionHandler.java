package com.keep.changes.exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.keep.changes.payload.response.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// handle other exceptions
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiResponse> handleAuthenticationException(Exception ex) {
		ApiResponse response = new ApiResponse(
				"Authentication failed! Try logging again. " + HttpStatus.UNAUTHORIZED.toString(), false);

		return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(
			MethodArgumentNotValidException ex) {

		Map<String, String> respErrors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String field = ((FieldError) error).getField();
			String msg = ((FieldError) error).getDefaultMessage();
			respErrors.put(field, msg);
		});

		return new ResponseEntity<Map<String, String>>(respErrors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> apiExceptionHandler(ApiException ex) {

		String msg = ex.getMessage();
		HttpStatus status = ex.getStatus();
		Boolean bool = ex.getBool();

		ApiResponse apiResponse = new ApiResponse(msg, bool);

		return new ResponseEntity<ApiResponse>(apiResponse, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleSecurityException(Exception exception) {
		ProblemDetail errorDetail = null;

		String msg = null;

		// TODO send this stack trace to an observability tool
		exception.printStackTrace();

		if (exception instanceof Unauthorized) {
			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);

		}

		if (exception instanceof BadCredentialsException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
			errorDetail.setProperty("description", "The username or password is incorrect");

			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
//			return errorDetail;
		}

		if (exception instanceof AccountStatusException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The account is locked");

			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.FORBIDDEN);
		}

		if (exception instanceof AccessDeniedException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "You are not authorized to access this resource");

			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.FORBIDDEN);
		}

		if (exception instanceof SignatureException) {
//			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
//			errorDetail.setProperty("description", "The JWT signature is invalid");

			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
		}

		if (exception instanceof ExpiredJwtException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
			errorDetail.setProperty("description", "The JWT token has expired");

			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
		}

		if (exception instanceof InternalAuthenticationServiceException) {
			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
		}

		if (exception instanceof MultipartException) {
			String message = exception.getMessage();
			ApiResponse apiResponse = new ApiResponse(message, false);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		}

		if (exception instanceof MissingServletRequestPartException) {
			String message = exception.getMessage();
			ApiResponse apiResponse = new ApiResponse(message, false);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		}

		if (exception instanceof NullPointerException) {
			msg = exception.getMessage();
			ApiResponse response = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (exception instanceof ResourceNotFoundException) {
			msg = exception.getMessage();
			ApiResponse apiResponse = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
		}

		if (exception instanceof ResourceAlreadyExistsException) {
			msg = exception.getMessage();
			ApiResponse apiResponse = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CONFLICT);
		}

		if (exception instanceof DataIntegrityViolationException) {
			HttpStatus status;
			if (exception.getMessage().contains("Duplicate entry")) {
				msg = "Error: A category with the same name already exists.";
				status = HttpStatus.CONFLICT;
			} else {
				msg = "Data integrity violation occurred.";
				status = HttpStatus.BAD_REQUEST;
			}
			ApiResponse apiResponse = new ApiResponse(msg, false);
			return new ResponseEntity<ApiResponse>(apiResponse, status);
		}

		ApiResponse response = new ApiResponse(exception.getMessage(), false);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);
	}

}
