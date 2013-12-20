package com.danleinbach.sample.validation.mock;

import com.danleinbach.sample.domain.ValidationResponse;
import com.danleinbach.sample.exception.JsonSchemaValidationException;
import com.danleinbach.sample.validation.IJsonSchemaValidationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created: 12/19/13
 *
 * @author Daniel
 */
public class MockValidationProcessor implements IJsonSchemaValidationProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockValidationProcessor.class);

	@Override
	public void setSchemaRoot(String schemaRoot) {

	}

	@Override
	public String getSchemaRoot() {
		return null;
	}

	@Override
	public ValidationResponse validateRequest(HttpServletRequest request) throws JsonSchemaValidationException {
		return null;
	}
}
