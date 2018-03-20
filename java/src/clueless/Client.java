package clueless;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class Client implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);

    Context zmqContext;
    Socket socket;
    // Socket chatSocket;
    UUID uuid;
    ConcurrentLinkedQueue<Message> chatMessageQueue;
    Thread thread;
    // TODO: Make this abstract
    CLIEventHandler evtHandler;

    public Client(CLIEventHandler handler) {
        uuid = UUID.randomUUID();
        // Grab a context object with one I/O thread.
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.DEALER);
        socket.setIdentity(uuid.toString().getBytes());
        chatMessageQueue = new ConcurrentLinkedQueue<Message>();
        evtHandler = handler;
    }

    public boolean connect(String hostStr, String portStr) throws Exception {
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
        return true;
    }

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

    public void disconnect() throws Exception {
        thread.interrupt();
        thread.join();
        socket.close();
        return;
    }
}
