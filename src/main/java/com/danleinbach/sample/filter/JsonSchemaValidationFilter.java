package com.danleinbach.sample.filter;

import com.danleinbach.sample.domain.ExceptionResponse;
import com.danleinbach.sample.domain.ValidationResponse;
import com.danleinbach.sample.exception.JsonSchemaValidationException;
import com.danleinbach.sample.validation.IJsonSchemaValidationProcessor;
import com.danleinbach.sample.validation.impl.DefaultJsonSchemaValidationProcessor;
import com.danleinbach.sample.wrapper.ReloadableHttpServletRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created: 12/14/13
 *
 * @author Daniel
 */
public class JsonSchemaValidationFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaValidationFilter.class);
	private String schemaRoot = "/";
	private IJsonSchemaValidationProcessor validationProcessor;
	private ObjectMapper objectMapper;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		setSchemaRoot(filterConfig.getInitParameter("schema-root"));

		validationProcessor = instantiateValidationProcessor(filterConfig.getInitParameter("validation-class"));

		validationProcessor.setSchemaRoot(this.schemaRoot);
		objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, true)
				.configure(MapperFeature.AUTO_DETECT_FIELDS, false)
				.configure(MapperFeature.AUTO_DETECT_GETTERS, false)
				.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
	}

	private void setSchemaRoot(String schemaRoot) {
		if(schemaRoot != null) {
			this.schemaRoot = schemaRoot;
		}
	}

	private IJsonSchemaValidationProcessor instantiateValidationProcessor(String className) {
		if(className != null) {
			try {
				Class<? extends IJsonSchemaValidationProcessor> clazz =
						(Class<? extends IJsonSchemaValidationProcessor>) Class.forName(className);

				return clazz.newInstance();

			} catch(ClassNotFoundException e) {
				handleValidationProcessorInstantiationExceptions(e);
			} catch(ReflectiveOperationException e) {
				handleValidationProcessorInstantiationExceptions(e);
			}
		}

		return new DefaultJsonSchemaValidationProcessor();

	}

	private void handleValidationProcessorInstantiationExceptions(Exception e) {
		LOGGER.warn("Error instantiating custom validationProcessor, using default", e);
		validationProcessor = new DefaultJsonSchemaValidationProcessor();
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		ReloadableHttpServletRequest httpServletRequest =
				new ReloadableHttpServletRequest((HttpServletRequest) servletRequest);

		LOGGER.debug(objectMapper
				.writeValueAsString(objectMapper.convertValue(httpServletRequest.getParameterMap(), JsonNode.class)));

		try {
			ValidationResponse report = validationProcessor.validateRequest(httpServletRequest);

			if(report.isSuccess()) {
				filterChain.doFilter(httpServletRequest, servletResponse);
			} else {

				writeErrorResponse(400, report, (HttpServletResponse) servletResponse);
			}

		} catch(JsonSchemaValidationException jsonSchemaValidationException) {
			ExceptionResponse response = new ExceptionResponse();
			response.setSubCode(jsonSchemaValidationException.getErrorCode().getErrorCode());
			response.setErrorMessage(jsonSchemaValidationException.getErrorCode().getErrorMessage());
			response.setErrorCausedBy(jsonSchemaValidationException.getCause().getMessage());

			writeErrorResponse(400, response, (HttpServletResponse) servletResponse);
		}


	}

	private void writeErrorResponse(int status, Object error, HttpServletResponse servletResponse) {
		servletResponse.setContentType("application/json");
		servletResponse.setStatus(status);

		try {
			objectMapper.writeValue(servletResponse.getOutputStream(), error);
		} catch(IOException e) {
			LOGGER.error("Could not write to servlet response", e);
		}

	}

	@Override
	public void destroy() {

	}
}
