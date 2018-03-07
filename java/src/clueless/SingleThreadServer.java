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
        logger.trace("default construtor");
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
                        conn.handleRequest();
                        
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
                        sc.write(conn.out_buffer);
                        
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

    private ByteBuffer in_buffer;
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
        
        // Read in the request message object
        
        // TODO: This needs flow control
        in_buffer.clear();
        sc.read(in_buffer);
        in_buffer.flip();
        logger.info(String.format("read %d byte request", in_buffer.limit()));
        
        bbis = new ByteBufferBackedInputStream(in_buffer);
        ois = new ObjectInputStream(bbis);
        req = (Message) ois.readObject();
        logger.info("req msg id " + req.getMessageID());
                
        resp = server.gameState.processMessage(req);

        // Send the message object
        out_buffer.clear();
        bbos = new ByteBufferBackedOutputStream(out_buffer);
        oos = new ObjectOutputStream(bbos);
        oos.writeObject(resp);
        out_buffer.flip();
        logger.info(String.format("sending %d byte resp", out_buffer.limit()));
        sendBuffer();
    
    }
    
    public void sendBuffer() throws Exception
    {   
        Selector selector = selectionKey.selector();
        try {
            // Do the write
            //while (out_buffer.remaining() > 0) {
                // Add socket to write queue
                logger.warn("Add socket to write queue");
                sc.register(selector, SelectionKey.OP_WRITE);
            //}
        
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }        
    }

}
