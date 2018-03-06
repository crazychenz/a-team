/**
 * 
 */
package clueless;

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
    Selector selector;
    private Socket socket;
    SocketChannel sc;
    
    private ByteBuffer in_buffer;
    private ByteBuffer out_buffer;
    public User user;
    private boolean connected = false;
    
    public SimpleClient() {
        in_buffer = ByteBuffer.allocate(4096);
        out_buffer = ByteBuffer.allocate(4096);
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
                System.out.println("Connected.");
            }
            else {
                System.out.println("Not connected.");
            }
            // Grab underlying socket
            socket = sc.socket();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host while connecting client.");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
    }
    
    private void handleComms() throws Exception
    {
        try {
            int ret;
            long timeout = 10;
            
            // Wait for available data and connections
            ret = selector.select(timeout);
            if (ret == 0)
            {
                // nothing to do, rinse and repeat
                return;
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
                    // reset buffer
                    in_buffer.clear();
                    try {
                        // read data into buffer
                        sc.read(in_buffer);
                    } catch (IOException e) {
                        System.out.println("Failed to read buf.");
                        throw e;
                    }
                    // flip buffer for reading
                    in_buffer.flip();
                    System.out.format("Received %d bytes.\n", 
                        in_buffer.limit());
                }
                if (key.isValid() && key.isWritable()) {
                    // Handle outgoing data
                    SocketChannel sc = (SocketChannel) key.channel();
                    try {
                        // write buffer to socket
                        sc.write(out_buffer);
                    } catch (IOException e) {
                        System.out.println("Failed to write buf.");
                        throw e;
                    }
                    System.out.format("Sent %d bytes.\n", out_buffer.position());
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Failed handleComms");
            throw e;
        }
    }
    
    public void reqResp() throws Exception
    {
        byte[] req = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        
        try {
            out_buffer.clear();
            out_buffer.put(req);
            out_buffer.flip();
            
            // Do the write
            while (out_buffer.remaining() > 0) {
                // Add socket to write queue
                sc.register(selector, SelectionKey.OP_WRITE);
                handleComms();
            }
            
            sc.register(selector, SelectionKey.OP_READ);
            in_buffer.clear();
            handleComms();
        
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }

}


