package clueless;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class Server implements Runnable {

    private static final Logger logger
            = LogManager.getLogger(Server.class);

    Context zmqContext;
    Socket socket;
    Game gameState;

    ConcurrentLinkedQueue<Message> chatMessageQueue;

    public Server() {
        // Grab a context object with one I/O thread.
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.ROUTER);
        chatMessageQueue = new ConcurrentLinkedQueue<Message>();
        gameState = new Game();
    }

    public void run() {
        socket.bind("tcp://*:2323");

        Poller items = zmqContext.poller(1);
        items.register(socket, Poller.POLLIN);

        //while (!Thread.currentThread().isInterrupted()) {
        while (true) {
            if (items.poll() < 0) {
                return; //  Interrupted
            }

            if (items.pollin(0)) {
                Message msg = null;
                byte[] replyto = socket.recv();
                logger.info("Got a message from " + replyto);

                // Fetch delimiter (assumed empty)
                socket.recvStr();

                // Fetch the request
                try {
                    msg = Message.fromBuffer(ByteBuffer.wrap(socket.recv()));
                } catch (Exception e) {
                    logger.error("Failed to parse message.");
                }

                logger.info("Request: " + msg);

                msg = gameState.processMessage(msg);

                if (msg != null) {
                    // Send the response
                    socket.sendMore(replyto);
                    socket.sendMore("");
                    try {
                        ByteBuffer buf = Message.toBuffer(msg);
                        logger.info("Response: " + msg.getMessageID() + " " + buf.limit() + " bytes");
                        socket.send(buf.array());
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Failed to serialize reply.");
                    }
                }

            }
        }
    }

    public void disconnect() throws Exception {
        socket.close();
        return;
    }

}
