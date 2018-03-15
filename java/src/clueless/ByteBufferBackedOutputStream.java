/* https://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream */
package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.nio.ByteBuffer;
import java.io.OutputStream;
import java.io.IOException;

public class ByteBufferBackedOutputStream extends OutputStream {

	private static final Logger logger
			= LogManager.getLogger(ByteBufferBackedOutputStream.class);

	ByteBuffer buf;

	public ByteBufferBackedOutputStream(ByteBuffer buf) {
		this.buf = buf;
	}

	@Override
	public void write(int b) throws IOException {
		buf.put((byte) b);
	}

	@Override
	public void write(byte[] bytes, int off, int len)
			throws IOException {
		buf.put(bytes, off, len);
	}

}
