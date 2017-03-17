package com.commons;

import java.util.List;

public class ErrorResponse {
	private String message;
	
	private String code;
	
	private List<FieldError> errors;
	
	//TODO
	public static class FieldError{
		private String field;
		private String value;
		private String reason;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
