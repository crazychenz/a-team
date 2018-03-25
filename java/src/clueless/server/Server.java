package clueless.server;
// TODO - If a client disconnects in the middle of the game, we need to handle it.  The easy way out
// is to end the game.
// The more difficult way is to add their cards to the face up cards, or have the server pretend to
// play as the person (not happening)

import clueless.*;
import clueless.io.*;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class Server implements Runnable {

    private static final Logger logger = LogManager.getLogger(Server.class);

    Context zmqContext;
    Socket socket;
    Game gameState;

    public Server() {
        this(System.currentTimeMillis());
    }

    public Server(long seed) {
        // Grab a context object with one I/O thread.
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.ROUTER);
        gameState = new Game(seed);
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

    public void sendMessage(String uuid, Message msg) throws Exception {
        ByteBuffer buf;
        try {
            buf = Message.toBuffer(msg);
        } catch (Exception e) {
            logger.error("Failed to sendMessage.");
            throw e;
        }
        socket.sendMore(uuid);
        socket.sendMore("");
        socket.send(buf.array());
    }

    @Override
    public void run() {
        socket.bind("tcp://*:2323");

        Poller items = zmqContext.poller(1);
        items.register(socket, Poller.POLLIN);

        // while (!Thread.currentThread().isInterrupted()) {
        while (true) {
            if (items.poll(2000) < 0) {
                return; //  Interrupted
            }

            if (items.pollin(0)) {
                Message msg = null;
                String replyto = socket.recvStr();
                logger.trace("Got a message from " + replyto);

                // Fetch delimiter (assumed empty)
                socket.recvStr();

                // Fetch the request
                try {
                    msg = Message.fromBuffer(ByteBuffer.wrap(socket.recv()));
                    msg.setFromUuid(replyto);
                } catch (Exception e) {
                    logger.error("Failed to parse message.");
                }

                logger.trace("Request: " + msg);

                msg = gameState.processMessage(msg);

                try {
                    if (msg != null) {
                        if (msg.isBroadcast()) {
                            for (Player player : gameState.players.getActivePlayers()) {
                                logger.trace("Sending broadcast to " + player.uuid);
                                sendMessage(player.uuid, msg);
                            }
                        } else {
                            logger.trace("Sending message to " + replyto);
                            sendMessage(replyto, msg);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to send response message.");
                    e.printStackTrace();
                }
            }
        }
    }

    public void disconnect() throws Exception {
        socket.close();
        return;
    }
}
