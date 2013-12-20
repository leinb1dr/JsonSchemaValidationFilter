package com.danleinbach.sample.filter;

import com.danleinbach.sample.domain.ValidationResponse;
import com.danleinbach.sample.validation.IJsonSchemaValidationProcessor;
import com.danleinbach.sample.validation.impl.DefaultJsonSchemaValidationProcessor;
import com.danleinbach.sample.validation.mock.MockValidationProcessor;
import com.danleinbach.sample.wrapper.ServletInputStreamImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created: 12/19/13
 *
 * @author Daniel
 */
public class JsonSchemaValidationFilterTest extends JsonSchemaValidationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonSchemaValidationFilterTest.class);

	@Before
	public void setUp() throws Exception {
		super.setObjectMapper(null);
		super.setValidationProcessor(null);

	}

	@Test
	public void testInitBasicConfig() throws Exception {
		FilterConfig config = mock(FilterConfig.class);
		when(config.getInitParameter("schema-root")).thenReturn(null);
		when(config.getInitParameter("validation-class")).thenReturn(null);
		super.init(config);

		assertNotNull(super.getSchemaRoot());
		assertEquals("/", super.getSchemaRoot());
		assertNotNull(super.getObjectMapper());
		assertNotNull(super.getValidationProcessor());
		assertTrue(super.getValidationProcessor() instanceof DefaultJsonSchemaValidationProcessor);
	}

	@Test
	public void testInitOverrideSchemaRoot() throws Exception {
		FilterConfig config = mock(FilterConfig.class);
		when(config.getInitParameter("schema-root")).thenReturn("/test");
		when(config.getInitParameter("validation-class")).thenReturn(null);
		super.init(config);

		assertNotNull(super.getSchemaRoot());
		assertEquals("/test", super.getSchemaRoot());
	}

	@Test
	public void testInitOverrideValidationProcessor() throws Exception {
		FilterConfig config = mock(FilterConfig.class);
		when(config.getInitParameter("schema-root")).thenReturn(null);
		when(config.getInitParameter("validation-class"))
				.thenReturn("com.danleinbach.sample.validation.mock.MockValidationProcessor");
		super.init(config);

		assertNotNull(super.getValidationProcessor());
		assertTrue(super.getValidationProcessor() instanceof MockValidationProcessor);
	}

	@Test
	public void testInitOverrideValidationProcessorClassNotFound() throws Exception {
		FilterConfig config = mock(FilterConfig.class);
		when(config.getInitParameter("schema-root")).thenReturn(null);
		when(config.getInitParameter("validation-class"))
				.thenReturn("com.danleinbach.sample.validation.mock.ASDF");
		super.init(config);

		assertNotNull(super.getValidationProcessor());
		assertTrue(super.getValidationProcessor() instanceof DefaultJsonSchemaValidationProcessor);

	}

	@Test
	public void testInitOverrideValidationProcessorNoDefaultConstructor() throws Exception {
		FilterConfig config = mock(FilterConfig.class);
		when(config.getInitParameter("schema-root")).thenReturn(null);
		when(config.getInitParameter("validation-class"))
				.thenReturn("com.danleinbach.sample.validation.mock.MockValidationProcessorNoDefaultConstructor");

		super.init(config);

		assertNotNull(super.getValidationProcessor());
		assertTrue(super.getValidationProcessor() instanceof DefaultJsonSchemaValidationProcessor);
	}

	@Test
	public void testDoFilterSuccess() throws Exception {

		ByteArrayInputStream stream = new ByteArrayInputStream("hello".getBytes());
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getInputStream()).thenReturn(new ServletInputStreamImpl(stream));

		HttpServletResponse response = mock(HttpServletResponse.class);

		ValidationResponse validationResponse = mock(ValidationResponse.class);
		when(validationResponse.isSuccess()).thenReturn(true);

		IJsonSchemaValidationProcessor processor = mock(IJsonSchemaValidationProcessor.class);
		when(processor.validateRequest(any(HttpServletRequest.class))).thenReturn(validationResponse);

		FilterChain chain = mock(FilterChain.class);
		doNothing().when(chain).doFilter(any(HttpServletRequest.class), eq(response));

		super.setValidationProcessor(processor);
		super.doFilter(request, response, chain);

		verify(chain, times(1)).doFilter(any(HttpServletRequest.class), eq(response));
	}
}
