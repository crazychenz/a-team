/**
 * 
 */
package clueless;

import java.io.EOFException;
import java.io.IOException;

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

/**
 * @author tombo
 *
 */
public class SingleThreadServer {
    ServerSocketChannel ssc;
    ServerSocket ss;
    //Game gameState;
    private Hashtable<SocketChannel, ClientConnection> clients;
    
    // Define some shorthand aliases
    private final int OP_ACCEPT = SelectionKey.OP_ACCEPT;
    private final int OP_READ = SelectionKey.OP_READ;
    private final int OP_WRITE = SelectionKey.OP_WRITE;
    
    public SingleThreadServer() {
        System.out.println("default construtor");
        clients = new Hashtable<SocketChannel, ClientConnection>();
        //gameState = new Game(this);
    }
    
    public void listenForConnections(
        CluelessServerSocket cluelessServerSocket) throws Exception {
        
        try {
            ssc = cluelessServerSocket.getServerSocketChannel();
            ss = ssc.socket();
        } catch (Exception e) {
            System.out.println("Failed to get server socket channel.");
            throw e;
        }
        
        Selector selector = Selector.open();
        
        // Register the ServerSocket with selector for new connections.
        ssc.register(selector, OP_ACCEPT);
        
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
                if ((key.readyOps() & OP_ACCEPT) == OP_ACCEPT) {
                    ClientConnection conn;
                    Socket s;
                    SocketChannel sc;
                    
                    // Handle incoming connection
                    System.out.println("Got a connection.");
                    s = ss.accept();
                    sc = s.getChannel();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    conn = new ClientConnection(s, this);
                    //connectedClients.add(conn);
                    clients.put(sc, conn);
                }
                else if ((key.readyOps() & OP_READ) == OP_READ) {
                    
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
                        // Remove the key from selector on error.
                        key.cancel();
                        
                    //    try {
                    //        sc.close();
                    //        System.out.println("Socket closed.");
                    //    } catch (IOException se) {
                    //        System.out.println("Failed to close socket.");
                    //        throw se;
                    //    }
                        
                        System.out.println("Failed to handleRequest");
                        throw e;
                    }
                }
            }
            keys.clear();
        }
    }
    
}

class ClientConnection {

    private ByteBuffer in_buffer;
    private ByteBuffer out_buffer;
    private Socket socket;
    private SocketChannel sc;
    private SingleThreadServer server;
    private boolean running = false;

    public ClientConnection(Socket socket, SingleThreadServer server) {
        this.socket = socket;
        sc = socket.getChannel();
        this.server = server;
        in_buffer = ByteBuffer.allocate(4096);
        out_buffer = ByteBuffer.allocate(4096);
        running = true;
        
        System.out.println("Client created");
    }
    
    public void handleRequest() throws Exception {
    
        // reset buffer
        in_buffer.clear();
        
        try {
            // read data into buffer
            sc.read(in_buffer);
        } catch (IOException e) {
            System.out.println("Failed to read buf in handleRequest.");
            throw e;
        }
        // flip buffer for reading
        in_buffer.flip();
        
        System.out.format("Received %d bytes.", in_buffer.limit());
    }

}
