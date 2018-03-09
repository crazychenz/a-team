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

import java.net.Socket;
import java.net.ServerSocket;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.io.OutputStream;

public class SingleThreadServer {

    private static final Logger logger =
        LogManager.getLogger(SingleThreadServer.class);

    ServerSocketChannel ssc;
    ServerSocket ss;
    Selector selector;
    Game gameState;
    private Hashtable<SocketChannel, ClientConnection> clients;
    
    public SingleThreadServer() {
        clients = new Hashtable<SocketChannel, ClientConnection>();
        gameState = new Game(this);
    }
    
    public void disconnect(SocketChannel sc) {
        ClientConnection conn = clients.get(sc);
        conn.cancelSelection();
        clients.remove(sc);
        try {
            sc.close();
            logger.debug("Socket closed.");
        } catch (IOException se) {
            logger.error("Failed to close socket.");
        } 
        return;
    }
    
    public void listenForConnections(
        CluelessServerSocket cluelessServerSocket) throws Exception {
        
        try {
            ssc = cluelessServerSocket.getServerSocketChannel();
            ss = ssc.socket();
        } catch (Exception e) {
            logger.error("Failed to get server socket channel.");
            throw e;
        }
        
        selector = Selector.open();
        
        // Register the ServerSocket with selector for new connections.
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        
        while(true) {
            int ret;
            long timeout = 10;
            
            // Wait for available data and connections
            ret = selector.select(timeout);
            if (ret == 0)
            {
                // nothing to do, rinse and repeat
                continue;
            }
            
            // Handle incoming stuff
            Set keys = selector.selectedKeys();
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = (SelectionKey)it.next();
                if (key.isValid() && key.isAcceptable()) {
                    ClientConnection conn;
                    Socket s;
                    SocketChannel sc;
                    SelectionKey selectionKey;
                    
                    // Handle incoming connection
                    logger.debug("Got a connection.");
                    s = ss.accept();
                    sc = s.getChannel();
                    sc.configureBlocking(false);
                    selectionKey = sc.register(selector, SelectionKey.OP_READ);
                    conn = new ClientConnection(s, selectionKey, this);
                    clients.put(sc, conn);
                }
                else if (key.isValid() && key.isReadable()) {
                    
                    // Handle incoming data
                    SocketChannel sc = null;
                    ClientConnection conn = null;
                    
                    try {
                        sc = (SocketChannel)key.channel();
                        
                        // TODO: if handleRequest detects reset connection,
                        //       we need to clear the connection from
                        //       the ssc and connectedClients.
                        // Fetch the connection related to this request
                        conn = (ClientConnection)clients.get(sc);

                        // The first 4 bytes are the length of the
                        // blob, so this client is going to keep trying
                        // to read until it gets all those bytes.
                        // TODO: This could be made better if we
                        // made sure we always had atleast 4 bytes too.
                        
                        int bytes = sc.read(conn.in_buffer);
                        int length = conn.in_buffer.getInt(0);
                        if (conn.in_buffer.position() != length) {
                            // TODO: Check out timeout and size violations
                            logger.info("bytes " + bytes + " length " + length);
                            continue;
                        }
                        else {
                            conn.in_buffer.flip();
                            conn.handleRequest();
                            // Set this ClientConnection for output
                            sc.register(selector, SelectionKey.OP_WRITE);
                        }
                        
                    } catch (Exception e) {
                        disconnect(sc);
                        logger.error("Failed to handleRequest");
                        throw e;
                    }
                }
                else if (key.isValid() && key.isWritable()) {
                    
                    // Handle incoming data
                    SocketChannel sc = null;
                    ClientConnection conn = null;
                    
                    try {
                        sc = (SocketChannel)key.channel();
                        // TODO: if handleRequest detects reset connection,
                        //       we need to clear the connection from
                        //       the ssc and connectedClients.
                        // Fetch the connection related to this request
                        conn = (ClientConnection)clients.get(sc);
                        int bytes = sc.write(conn.out_buffer);
                        logger.info("Sent " + bytes + " bytes.");
                        if (conn.out_buffer.position() == conn.out_buffer.limit())
                        {
                            // We're done writing, look for more requests
                            sc.register(selector, SelectionKey.OP_READ);
                        }
                        
                    } catch (Exception e) {
                        disconnect(sc);
                        logger.error("Failed to handleRequest");
                        throw e;
                    }
                }
            }
            keys.clear();
        }
    }
    
}

class ClientConnection {

    private static final Logger logger =
        LogManager.getLogger(ClientConnection.class);

    public ByteBuffer in_buffer;
    public ByteBuffer out_buffer;
    private Socket socket;
    private SocketChannel sc;
    private SingleThreadServer server;
    private SelectionKey selectionKey;
    OutputStream out_stream;
    private boolean running = false;

    public ClientConnection(Socket socket, SelectionKey key, SingleThreadServer server)
    {
        selectionKey = key;
        this.socket = socket;
        sc = socket.getChannel();
        this.server = server;
        in_buffer = ByteBuffer.allocate(4096);
        out_buffer = ByteBuffer.allocate(4096);
        running = true;
        
        logger.debug("Client created");
    }
    
    public void cancelSelection() {
        try {
            selectionKey.cancel();
        } catch (Exception e) {
            logger.error("Failed to cancel selector key.");
        }
    }
    
    public void handleRequest() throws Exception {
        ByteBufferBackedOutputStream bbos;
        ByteBufferBackedInputStream bbis;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        Message req;
        Message resp;
        
        try {
            // Parse the message.
            in_buffer.getInt();
            bbis = new ByteBufferBackedInputStream(in_buffer);
            ois = new ObjectInputStream(bbis);
            req = (Message) ois.readObject();
            logger.info("req msg id " + req.getMessageID());
            
            // Process the message
            resp = server.gameState.processMessage(req);
    
            // Serialize outgoing message
            out_buffer.clear();
            // Reserve size bytes
            out_buffer.putInt(0);out_buffer.putInt(0, out_buffer.position());
            bbos = new ByteBufferBackedOutputStream(out_buffer);
            oos = new ObjectOutputStream(bbos);
            oos.writeObject(resp);
            // Overlay size of blob to front of array
            out_buffer.putInt(0, out_buffer.position());
            out_buffer.flip();
            
            logger.info(String.format("sending %d byte resp", out_buffer.limit()));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } 
    
    }

}
