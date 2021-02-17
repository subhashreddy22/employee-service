package com.paypal.bfs.test.employeeserv.exception;

public class BadRequestException extends Exception {
	
	private static final long serialVersionUID = 2500432281447688269L;

	public BadRequestException(String message) {
		super(message);
	}
}
