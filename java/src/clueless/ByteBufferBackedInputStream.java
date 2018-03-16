/* https://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream */
package clueless;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ByteBufferBackedInputStream extends InputStream {

    private static final Logger logger = LogManager.getLogger(ByteBufferBackedInputStream.class);

    ByteBuffer buf;

    public ByteBufferBackedInputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public int read() throws IOException {
        if (!buf.hasRemaining()) {
            return -1;
        }
        return buf.get() & 0xFF;
    }

    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        if (!buf.hasRemaining()) {
            return -1;
        }

        len = Math.min(len, buf.remaining());
        buf.get(bytes, off, len);
        return len;
    }
}
