package com.danleinbach.sample.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic implementation of a ServletInputStream, that wraps an existing input stream.
 * This object can only be constructed by the {@link ReloadableHttpServletRequest}
 */
public class ServletInputStreamImpl extends ServletInputStream {

	private static final int EOL = - 1;
  private static final Logger LOGGER = LoggerFactory.getLogger(ServletInputStreamImpl.class);
  private InputStream is;
	private ReadListener readListener;
	private boolean finished;


	/**
	 * Construct a new ServletInputStream that wraps the provided InputStream
	 *
	 * @param is - Wrapped input stream.
	 */
	public ServletInputStreamImpl(InputStream is) {

		this.is = is;
	}

	@Override
	public int read() throws IOException {
		int currentByte = is.read();
		if(currentByte == EOL) {
			finished = true;
		}
		return currentByte;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public synchronized void mark(int i) {
		throw new RuntimeException(new IOException("mark/reset not supported"));
	}

	@Override
	public synchronized void reset() throws IOException {
		throw new IOException("mark/reset not supported");
	}

	@Override
	public boolean isFinished() {
		return this.finished;
	}

	@Override
	public boolean isReady() {
		return ! this.finished;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		this.readListener = readListener;
		if(isReady()) {
			try {
				this.readListener.onDataAvailable();
			} catch(IOException e) {
				LOGGER.debug("Exception occured notifing read listener of available data.", e);
				this.readListener.onError(e);
			}
		}
	}

}