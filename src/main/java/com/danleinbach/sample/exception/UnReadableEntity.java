package com.danleinbach.sample.exception;

import com.danleinbach.sample.constants.ErrorCode;

/**
 * Created: 12/17/13
 *
 * @author Daniel
 */
public class UnReadableEntity extends JsonSchemaValidationException {

	public UnReadableEntity(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
