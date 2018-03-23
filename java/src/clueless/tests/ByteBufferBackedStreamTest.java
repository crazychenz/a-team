package clueless.tests;

import static org.junit.Assert.assertEquals;

import clueless.io.ByteBufferBackedInputStream;
import clueless.io.ByteBufferBackedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ByteBufferBackedStreamTest {

    private static final Logger logger = LogManager.getLogger(ByteBufferBackedStreamTest.class);

    @Test
    public void simpleTest() {
        assertEquals(0, 0);
    }

    @Test
    public void testObjectStreamToByteBufferToObjectStream() {
        String outgoing;
        String incoming;

        ByteBuffer buf;
        ByteBufferBackedOutputStream bbos;
        ByteBufferBackedInputStream bbis;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        outgoing = "A string object.";
        buf = ByteBuffer.allocate(4096);
        bbos = new ByteBufferBackedOutputStream(buf);

        try {
            oos = new ObjectOutputStream(bbos);
            oos.writeObject(outgoing);
            oos.close();
        } catch (IOException e) {
            logger.error(e);
            return;
        }

        buf.flip();

        bbis = new ByteBufferBackedInputStream(buf);
        try {
            ois = new ObjectInputStream(bbis);
            incoming = (String) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e);
            return;
        }

        assertEquals(outgoing.compareTo(incoming), 0);
    }
}
