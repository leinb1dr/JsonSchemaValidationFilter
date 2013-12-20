package com.danleinbach.sample.exception;

import com.danleinbach.sample.constants.ErrorCode;

/**
 * Created: 12/17/13
 *
 * @author Daniel
 */
public class JsonSchemaValidationException extends Exception {

	private final ErrorCode errorCode;

	public JsonSchemaValidationException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
