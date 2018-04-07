package clueless.client;

import clueless.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

/**
 * Represents a generic Client for interacting with the Clueless Server
 *
 * @author ateam
 */
public class Client implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);

    Context zmqContext;
    Socket socket;

    public UUID uuid;
    ConcurrentLinkedQueue<Message> chatMessageQueue;
    Thread thread;
    // TODO: Make this abstract
    EventHandler evtHandler;

    /**
     * Constructor
     *
     * @param handler EventHandler for the specific UI
     */
    public Client(EventHandler handler) {
        uuid = UUID.randomUUID();
        // Grab a context object with one I/O thread.
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.DEALER);
        socket.setIdentity(uuid.toString().getBytes());
        chatMessageQueue = new ConcurrentLinkedQueue<Message>();
        evtHandler = handler;
    }

    /**
     * Initialize a connection to the server
     *
     * @param hostStr Host address of the server (localhost if null)
     * @param portStr TCP port of the server (port 2323 if null)
     * @throws Exception If the connection thread couldn't be started
     */
    public void connect(String hostStr, String portStr) throws Exception {
        String host = "localhost";
        String port = "2323";
        if (hostStr != null) {
            host = hostStr;
        }
        if (portStr != null) {
            port = portStr;
        }
        String connectionStr = "tcp://" + host + ":" + port;
        logger.info("Connecting to " + connectionStr);
        socket.connect(connectionStr);
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Send a message object to server
     *
     * @param msg Message to send to server
     * @throws Exception when serialization fails
     */
    public void sendMessage(Message msg) throws Exception {
        ByteBuffer buf;
        try {
            buf = Message.toBuffer(msg);
        } catch (Exception e) {
            logger.error("Failed to sendMessage.");
            throw e;
        }
        socket.sendMore("");
        socket.send(buf.array());
    }

    @Override
    public void run() {
        Poller items = zmqContext.poller(1);
        items.register(socket, Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            try {

                if (items.poll() < 0) {
                    return; // Interrupted
                }

                // On gameSocket Event
                if (items.pollin(0)) {
                    // Delimiter frame is empty
                    String empty = socket.recvStr();
                    assert (empty.length() == 0);

                    // Get the payload frame
                    ByteBuffer buf = ByteBuffer.wrap(socket.recv());
                    Message msg = Message.fromBuffer(buf);

                    // Handle the event
                    evtHandler.onMessageEvent(this, msg);
                }

            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Disconnect client from server.
     *
     * @throws Exception when thread gets interrupted
     */
    public void disconnect() throws Exception {
        thread.interrupt();
        thread.join();
        socket.close();
        return;
    }
}
