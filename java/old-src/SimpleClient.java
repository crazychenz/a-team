/**
 * 
 */
package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import java.util.Set;
import java.util.Iterator;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class SimpleClient {

    private static final Logger logger =
        LogManager.getLogger(SimpleClient.class);
    
    Selector selector;
    private Socket socket;
    SocketChannel sc;
    
    private ByteBuffer in_buffer;
    private ByteBuffer out_buffer;
    //public User user;
    private boolean connected = false;
    
    public SimpleClient() {
        in_buffer = ByteBuffer.allocate(4096);
        out_buffer = ByteBuffer.allocate(4096);
    }
    
    public void connectToGame() {
        ByteBufferBackedOutputStream bbos;
        ByteBufferBackedInputStream bbis;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        Message msg;
        
        logger.debug("Connecting to Game");
        
        try {
            // Send the message object
            out_buffer.clear();
            // Reserve 4 bytes
            out_buffer.putInt(0);
            msg = new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED, "");
            bbos = new ByteBufferBackedOutputStream(out_buffer);
            oos = new ObjectOutputStream(bbos);
            oos.writeObject(msg);
            out_buffer.putInt(0, out_buffer.position());
            out_buffer.flip();
            
            while (out_buffer.position() < out_buffer.limit()) {
                sendRequest();
            }
            
            // Read in the response message object
            // expecting a list of suspects
            readResponse();
            logger.info(String.format("Received %d bytes.", in_buffer.limit()));
            bbis = new ByteBufferBackedInputStream(in_buffer);
            ois = new ObjectInputStream(bbis);
            msg = (Message) ois.readObject();
            logger.info("resp msg id " + msg.getMessageID());
        } catch (Exception e) {
            logger.error("Failure in connectToGame");
        }
    }
    
    public void connectToServer() throws Exception
    {
        SelectionKey conn_key;
        selector = Selector.open();
        
        try {
            // TODO: Use cli arguments
            // Connect a socket
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            conn_key = sc.register(selector, SelectionKey.OP_CONNECT);
            // Start connection
            sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 2323));
            // Wait for connection or timeout.
            handleComms();
            if (connected) {
                logger.info("Connected.");
            }
            else {
                logger.warn("Not connected.");
            }
            // Grab underlying socket
            socket = sc.socket();
        } catch (UnknownHostException e) {
            logger.error("Unknown host while connecting client.");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
    }
    
    private boolean handleComms() throws Exception
    {
        try {
            int ret;
            long timeout = 10;
            
            // Wait for available data and connections
            ret = selector.select(timeout);
            if (ret == 0)
            {
                // nothing to do, rinse and repeat
                return true;
            }
            
            // Handle incoming stuff
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isValid() && key.isConnectable()) {
                    connected = true;
                    SocketChannel sc = (SocketChannel) key.channel();
                    sc.finishConnect();
                }
                if (key.isValid() && key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    // Note: Caller responsible for resetting buffer
                    try {
                        // read data into buffer
                        int bytes = sc.read(in_buffer);
                        int length = in_buffer.getInt(0);
                        if (in_buffer.position() != length) {
                            // TODO: Check out timeout and size violations
                            logger.info("bytes " + bytes + " length " + length);
                            return false;
                        }
                    } catch (IOException e) {
                        logger.error("Failed to read buf.");
                        throw e;
                    }
                    // flip buffer for reading
                    in_buffer.flip();
                    logger.debug(String.format("Received %d bytes.\n", 
                        in_buffer.limit()));
                }
                if (key.isValid() && key.isWritable()) {
                    // Handle outgoing data
                    SocketChannel sc = (SocketChannel) key.channel();
                    try {
                        // write buffer to socket
                        int bytes = sc.write(out_buffer);
                        logger.info("Sent " + bytes + " bytes.");
                        if (out_buffer.position() < out_buffer.limit())
                        {
                            return false;    
                        }
                        out_buffer.clear();
                    } catch (IOException e) {
                        logger.error("Failed to write buf.");
                        throw e;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed handleComms");
            throw e;
        }
        return true;
    }
    
    public void readResponse() throws Exception
    {   
        try {
            in_buffer.clear();
            sc.register(selector, SelectionKey.OP_READ);
            while (handleComms() == false);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }
    
    public void sendRequest() throws Exception
    {   
        try {
            // Add socket to write queue
            sc.register(selector, SelectionKey.OP_WRITE);
            while (handleComms() == false);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }

}


