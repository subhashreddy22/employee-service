package com.paypal.bfs.test.employeeserv.model;

public class Error {
	
	private String errorMessage;
	
	public Error(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
