package com.danleinbach.sample.wrapper;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Extention of HttpServletRequestWrapper, that will allow the request body to be read
 * multiple times. Given a HttpServletRequest, the body is read into memory, so it can
 * be reused as much as the user wants.
 */
public class ReloadableHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] body;

	/**
	 * Construct a new ReloadableHttpServletRequest from an existing HttpServletRequest
	 *
	 * @param request - Incoming HttpServletRequest, that the body needs to be read more than once.
	 * @throws java.io.IOException - In the case the request's body cannot be read.
	 */
	public ReloadableHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		// Read the request body and save it as a byte array
		InputStream is = super.getInputStream();
		body = IOUtils.toByteArray(is);
	}

	/**
	 * Create a new ServletInputStream from the cached contents of the request body.
	 *
	 * @return - ServletInputStream containing a copy of the request body
	 * @throws java.io.IOException
	 */
	@Override
	public ServletInputStream getInputStream() {
		return new ServletInputStreamImpl(new ByteArrayInputStream(body));
	}

	/**
	 * Create a BufferedReader from the cached contents of the request body. This can be
	 * called in addition to calling {@link #getInputStream} since a copy of the request
	 * body is returned.
	 *
	 * @return - BufferedReader containing a copy of the request body
	 * @throws java.io.IOException
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		String enc = getCharacterEncoding();
		if(enc == null) {
			enc = "UTF-8";
		}
		return new BufferedReader(new InputStreamReader(getInputStream(), enc));
	}
}