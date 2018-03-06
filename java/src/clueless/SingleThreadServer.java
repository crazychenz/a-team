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
import java.io.OutputStream;

public class SingleThreadServer {
    ServerSocketChannel ssc;
    ServerSocket ss;
    //Game gameState;
    private Hashtable<SocketChannel, ClientConnection> clients;
    
    public SingleThreadServer() {
        System.out.println("default construtor");
        clients = new Hashtable<SocketChannel, ClientConnection>();
        //gameState = new Game(this);
    }
    
    public void disconnect(SocketChannel sc) {
        ClientConnection conn = clients.get(sc);
        conn.cancelSelection();
        clients.remove(sc);
        try {
            sc.close();
            System.out.println("Socket closed.");
        } catch (IOException se) {
            System.out.println("Failed to close socket.");
        } 
        return;
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
                    System.out.println("Got a connection.");
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
        
        System.out.println("Client created");
    }
    
    public void cancelSelection() {
        try {
            selectionKey.cancel();
        } catch (Exception e) {
            System.out.println("Failed to cancel selector key.");
        }
    }
    
    public void handleRequest() throws Exception {
    
        // -- Read the request --
        // reset buffer
        in_buffer.clear();
        try {
            // read data into buffer
            sc.read(in_buffer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read buf");
            throw e;
        }
        // flip buffer for reading
        in_buffer.flip();
        
        if (in_buffer.limit() == 0)
        {
            System.out.println("Connection closed remotely.");
            server.disconnect(sc);
            return;
        }
        
        // -- Analyze Request --
        System.out.format("Received %d bytes.\n", in_buffer.limit());
        
        // -- Send Response --
        byte [] response = {0x05, 0x04, 0x03};
        try {
            int bytes = sc.write(ByteBuffer.wrap(response));
            System.out.format("Sent %d bytes.\n", bytes);
        } catch (IOException e) {
            System.out.println("Failed to write buf");
            throw e;
        }
    }

}
