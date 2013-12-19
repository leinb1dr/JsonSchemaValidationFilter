package com.danleinbach.sample.constants;

/**
 * Internal/External error codes with messages.
 * <p/>
 * Created: 12/17/13
 *
 * @author Daniel
 */
public enum ErrorCode {

	UNREADABLE_REQUEST_BODY(400, "400_1", "Unreadable Request Body"),
	MALFORMED_JSON_SCHEMA(500, "500_1", "Schema For Request Has An Error"),
	ERROR_VALIDATING_JSON(500, "500_2", "Could Not Validation Json Request");

	private final int httpStatus;
	private final String errorCode;
	private final String errorMessage;

	private ErrorCode(int httpStatus, String errorCode, String errorMessage) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
