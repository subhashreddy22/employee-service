package com.paypal.bfs.test.employeeserv.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.model.Error;
import com.paypal.bfs.test.employeeserv.model.Errors;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class GlobalExceptionHandler.
 * 
 * Handles the exceptions occuring in the app and 
 * responds with appropirate error messages
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler { 
	
	/**
	 * Handle employee not found exception.
	 *
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<Errors> handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
		log.error("EmployeeNotFoundException -> {}", ExceptionUtils.getStackTrace(exception));
		return new ResponseEntity<>(getErrors(exception.getMessage()), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle bad request exception.
	 *
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<Errors> handleBadRequestException(BadRequestException exception) {
		log.error("BadRequestException -> {}", ExceptionUtils.getStackTrace(exception));
		return new ResponseEntity<>(getErrors(exception.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handle request header missing exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<Errors> handleRequestHeaderMissingException(MissingRequestHeaderException ex) {
		log.error("MissingRequestHeaderException -> {}", ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(getErrors(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handle generic exception.
	 *
	 */
	public ResponseEntity<Errors> handleGenericException(Exception ex) {
		log.error("Generic Exception -> {}", ExceptionUtils.getStackTrace(ex));
		Error error = new Error("Internal Servier Error occurred");
		Errors errors = new Errors();
		errors.add(error);
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle no handler found exception.
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("NoHandlerFoundException -> {}", ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(getErrors(ex.getMessage()), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle method not supported exception.
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("HttpRequestMethodNotSupportedException -> {}", ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(getErrors(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * Handle media type not supported exception.
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("HttpMediaTypeNotSupportedException -> {}", ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(getErrors(ex.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	/**
	 * Handle message not readable exception.
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("HttpMessageNotReadableException -> {}", ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(getErrors(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle method argument not valid exception.
	 * 
	 * Retrieves all the errors on the employee POJO 
	 * during the request as a BindingResult with 
	 * FieldErrors translated to Errors.
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("MethodArgumentNotValidException -> {}", ExceptionUtils.getStackTrace(ex));
		List<Error> errorList = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> new Error(fieldError.getField() + ": " + fieldError.getDefaultMessage()))
				.collect(Collectors.toList());
		Errors errors = new Errors();
		errors.addAll(errorList);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	private Errors getErrors(String message) {
		Error error = new Error(message);
		Errors errors = new Errors();
		errors.add(error);
		return errors;
	}

}
