package com.danleinbach.sample.validation;

import com.danleinbach.sample.domain.ValidationResponse;
import com.danleinbach.sample.exception.JsonSchemaValidationException;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a structure for all validation processors. All implementers should take care
 * that the ability to check headers, body, and query params exist.
 * <p/>
 * Created: 12/17/13
 *
 * @author Daniel
 */
public interface IValidationProcessor {

	/**
	 * Validate a given HttpServletRequest by checking the request parameters, body, and header as
	 * necessary to ensure a well formed request.
	 *
	 * @param request Incoming request to be validated
	 * @return A processing report that details any errors that occurred when validating the request.
	 * @throws com.danleinbach.sample.exception.JsonSchemaValidationException
	 *          Any errors that occur during validation will be wrapped in a
	 *          JsonSchemaValidationException exception.
	 */
	ValidationResponse validateRequest(HttpServletRequest request) throws JsonSchemaValidationException;

}
