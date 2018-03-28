/* https://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream */
package clueless.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for writing a ByteBuffer to an OutputStream
 *
 * @author ateam
 */
public class ByteBufferBackedOutputStream extends OutputStream {

    private static final Logger logger = LogManager.getLogger(ByteBufferBackedOutputStream.class);

    ByteBuffer buf;

    /**
     * Constructor
     *
     * @param buf Buffer to send to OutputStream (for writing)
     */
    public ByteBufferBackedOutputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public void write(int b) throws IOException {
        buf.put((byte) b);
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws IOException {
        buf.put(bytes, off, len);
    }
}
