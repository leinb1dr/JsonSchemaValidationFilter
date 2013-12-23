package com.danleinbach.sample.validation;

import com.danleinbach.sample.constants.ErrorCode;
import com.danleinbach.sample.constants.SchemaLocationConstants;
import com.danleinbach.sample.constants.ValidationConstants;
import com.danleinbach.sample.domain.ValidationResponse;
import com.danleinbach.sample.exception.JsonSchemaValidationException;
import com.danleinbach.sample.exception.UnReadableEntity;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created: 12/18/13
 *
 * @author Daniel
 */
public abstract class AbstractJsonSchemaValidationProcessor implements IJsonSchemaValidationProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJsonSchemaValidationProcessor.class);
	private String schemaRoot;
	private JsonSchemaFactory jsonSchemaFactory;

	/**
	 * Create a default validation processor
	 */
	public AbstractJsonSchemaValidationProcessor() {
		this(ValidationConstants.SCHEMA_ROOT_DEFAULT.getValue());

	}

	/**
	 * Create a validation processor with the given string as a root to look for
	 * schemas.
	 *
	 * @param schemaRoot - Root where search for schemas should begin.
	 */
	public AbstractJsonSchemaValidationProcessor(String schemaRoot) {
		this.schemaRoot = schemaRoot;
		this.jsonSchemaFactory = JsonSchemaFactory.byDefault();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSchemaRoot(String schemaRoot) {
		this.schemaRoot = schemaRoot;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSchemaRoot() {
		return this.schemaRoot;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidationResponse validateRequest(HttpServletRequest request) throws JsonSchemaValidationException {

		ValidationResponse response = new ValidationResponse();
		response.setBodyReport(validateRequestBody(request));
		response.setHeaderReport(validateRequestHeaders(request));
		response.setParamReport(validateRequestParams(request));

		return response;
	}

	/**
	 * Validate the request body
	 *
	 * @param request Incoming request to be validated
	 * @return Processing report that outlines and errors in the request
	 */
	protected abstract ProcessingReport validateRequestBody(HttpServletRequest request)
			throws JsonSchemaValidationException;

	/**
	 * Validate the request headers
	 *
	 * @param request Incoming request to be validated
	 * @return Processing report that outlines and errors in the request
	 */
	protected abstract ProcessingReport validateRequestHeaders(HttpServletRequest request)
			throws JsonSchemaValidationException;

	/**
	 * Validate the request parameters
	 *
	 * @param request Incoming request to be validated
	 * @return Processing report that outlines and errors in the request
	 */
	protected abstract ProcessingReport validateRequestParams(HttpServletRequest request)
			throws JsonSchemaValidationException;

	/**
	 * Finds the location of the request using the request uri, relative to the classpath. The search begins at the
	 * farthest point in the path and moves one folder back until it finds a file that matches the location name with
	 * the .json extension.
	 *
	 * @param request  - HttpServletRequest with the request uri used to search for schema location
	 * @param location Location in the request, limited by the enum values in the SchemaLocationConstants
	 * @return A String with the location of the json schema, or the word <code>skip</code> to notify the caller
	 *         that there is no json schema for this request/location combo.
	 */
	protected String getSchemaLocation(HttpServletRequest request, SchemaLocationConstants location) {

    String method = request.getMethod();
    String uri = request.getRequestURI();
    List<String> pathParts = Lists.newArrayList(Arrays.asList(uri.replaceAll("^/|\\..+$", "").split("/")));
    String fileName = location.getValue() + ".json";

    pathParts.add(method);

    for(int x = pathParts.size(); x > -1; x--) {
      StringBuilder path = new StringBuilder(schemaRoot.replaceFirst("/", ""));
			for(int y = 0; y < x; y++) {
        path.append(pathParts.get(y)).append("/");
      }
			String schemaPath = path.append(fileName).toString();
			LOGGER.debug("Looking for file:{}", schemaPath);
			URL exists = this.getClass().getClassLoader().getResource(schemaPath);
			if(exists != null) {
				return ValidationConstants.RESOURCE.getValue() + schemaRoot + schemaPath;
			}
		}

		LOGGER.info("Skipping validation for: {}", location.getValue());
		return ValidationConstants.SKIP.getValue();
	}

	/**
	 * Parse json schema and return it in objectified form.
	 *
	 * @param schemaLocation Location of the request schema
	 * @return Objectified form of the json schema
	 * @throws com.danleinbach.sample.exception.JsonSchemaValidationException
	 *          Thrown in the case there is an error processing the json schema
	 */
	protected JsonSchema getJsonSchema(String schemaLocation) throws JsonSchemaValidationException {
		try {

			return jsonSchemaFactory.getJsonSchema(schemaLocation);

		} catch(ProcessingException e) {
			LOGGER.error("Exception parsing json schema", e);
			throw new UnReadableEntity(ErrorCode.MALFORMED_JSON_SCHEMA, e);

		}
	}


}
