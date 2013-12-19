package com.danleinbach.sample.validation.impl;

import com.danleinbach.sample.constants.ErrorCode;
import com.danleinbach.sample.constants.SchemaLocationConstants;
import com.danleinbach.sample.constants.ValidationConstants;
import com.danleinbach.sample.exception.JsonSchemaValidationException;
import com.danleinbach.sample.exception.UnReadableEntity;
import com.danleinbach.sample.validation.AbstractJsonSchemaValidationProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.report.ProcessingReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the json schema validation processor. This will convert request body,
 * headers, and query parameters into a json structure which can be validated against a json schema.
 * <p/>
 * Created: 12/15/13
 *
 * @author Daniel
 */
public class DefaultJsonSchemaValidationProcessor extends AbstractJsonSchemaValidationProcessor {

	private final Logger LOGGER = LoggerFactory.getLogger(DefaultJsonSchemaValidationProcessor.class);
	private ObjectMapper objectMapper;

	/**
	 * {@inheritDoc}
	 */
	public DefaultJsonSchemaValidationProcessor() {
		this("/");
	}

	/**
	 * {@inheritDoc}
	 */
	public DefaultJsonSchemaValidationProcessor(String schemaRoot) {
		super(schemaRoot);
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ProcessingReport validateRequestBody(HttpServletRequest request) throws JsonSchemaValidationException {
		String schemaLocation = getSchemaLocation(request, SchemaLocationConstants.BODY);
		if(ValidationConstants.SKIP.getValue().equals(schemaLocation)) {
			return null;
		}
		LOGGER.debug("Schema location:{}", schemaLocation);
		JsonSchema jsonSchema = getJsonSchema(schemaLocation);
		LOGGER.debug("Json Schema Parsed");
		JsonNode requestBody = parseRequestBody(request);
		LOGGER.debug("Request Body Parsed");

		try {
			return jsonSchema.validate(requestBody);
		} catch(ProcessingException e) {

			LOGGER.error("Exception validating json against schema", e);
			throw new UnReadableEntity(ErrorCode.ERROR_VALIDATING_JSON, e);
		}
	}

	/**
	 * Parse request body into a json node for analysis.
	 *
	 * @param request Servlet request with json payload to analyzed
	 * @return JsonNode that represents request body
	 * @throws JsonSchemaValidationException Thrown when an error occurs parsing the request body.
	 */
	private JsonNode parseRequestBody(HttpServletRequest request) throws JsonSchemaValidationException {
		try {
			return objectMapper.readTree(request.getInputStream());

		} catch(IOException e) {

			LOGGER.error("Exception parsing request body into json", e);
			throw new UnReadableEntity(ErrorCode.UNREADABLE_REQUEST_BODY, e);

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ProcessingReport validateRequestHeaders(HttpServletRequest request) throws JsonSchemaValidationException {
		String schemaLocation = getSchemaLocation(request, SchemaLocationConstants.HEADER);
		if(ValidationConstants.SKIP.getValue().equals(schemaLocation)) {
			return null;
		}
		LOGGER.debug("Schema location:{}", schemaLocation);
		JsonSchema jsonSchema = getJsonSchema(schemaLocation);
		LOGGER.debug("Json Schema Parsed");
		JsonNode requestBody = parseHeaders(request);
		LOGGER.debug("Request Body Parsed");

		try {
			return jsonSchema.validate(requestBody);
		} catch(ProcessingException e) {

			LOGGER.error("Exception validating json against schema", e);
			throw new UnReadableEntity(ErrorCode.ERROR_VALIDATING_JSON, e);
		}
	}

	/**
	 * Iterate through all headers to create a map that is converted into a JsonNode for analysis.
	 *
	 * @param request Servlet request with headers to analyzed
	 * @return JsonNode that represents request headers
	 */
	private JsonNode parseHeaders(HttpServletRequest request) {

		Enumeration<String> headers = request.getHeaderNames();
		Map<String, Object> headerMap = new HashMap<String, Object>();

		while(headers.hasMoreElements()) {
			String header = headers.nextElement();
			headerMap.put(header, request.getHeader(header));
		}

		return objectMapper.convertValue(headerMap, JsonNode.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ProcessingReport validateRequestParams(HttpServletRequest request) throws JsonSchemaValidationException {
		String schemaLocation = getSchemaLocation(request, SchemaLocationConstants.PARAM);
		if(ValidationConstants.SKIP.getValue().equals(schemaLocation)) {
			return null;
		}
		LOGGER.debug("Schema location:{}", schemaLocation);
		JsonSchema jsonSchema = getJsonSchema(schemaLocation);
		LOGGER.debug("Json Schema Parsed");
		JsonNode requestBody = parseParams(request);
		LOGGER.debug("Request Body Parsed");

		try {
			return jsonSchema.validate(requestBody);
		} catch(ProcessingException e) {

			LOGGER.error("Exception validating json against schema", e);
			throw new UnReadableEntity(ErrorCode.ERROR_VALIDATING_JSON, e);
		}
	}

	/**
	 * Iterate through all query params to create a map that is converted into a JsonNode for analysis.
	 *
	 * @param request Servlet request with query params to analyzed
	 * @return JsonNode that represents request query params
	 */
	private JsonNode parseParams(HttpServletRequest request) {

		Enumeration<String> parameters = request.getParameterNames();
		Map<String, Object> paramMap = new HashMap<String, Object>();

		while(parameters.hasMoreElements()) {
			String param = parameters.nextElement();
			paramMap.put(param, request.getParameter(param));
		}

		return objectMapper.convertValue(paramMap, JsonNode.class);
	}

}