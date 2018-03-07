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
            
            sendRequest();
            
            // Read in the response message object
            // expecting a list of suspects
            readResponse();
            in_buffer.getInt();
            bbis = new ByteBufferBackedInputStream(in_buffer);
            ois = new ObjectInputStream(bbis);
            msg = (Message) ois.readObject();
            logger.info("resp msg id " + msg.getMessageID());
            in_buffer.clear();
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
            sc.configureBlocking(true);
            
            // Start connection
            sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 2323));
            logger.info("Connected.");
            
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
    
    public void sendRequest() throws Exception
    {   
        try {
            // Add socket to write queue
            int bytes = sc.write(out_buffer);
            logger.info("Sent " + bytes + " bytes");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }
    
    public void readResponse() throws Exception
    {   
        try {
            in_buffer.clear();
            sc.read(in_buffer);
            logger.info("Read " + in_buffer.position() + " bytes so far");
            int length = in_buffer.getInt(0);
            while (in_buffer.position() < length)
            {
                sc.read(in_buffer);
                logger.info("Read " + in_buffer.position() + " bytes so far");
            }
            in_buffer.flip();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }
    
    

}


