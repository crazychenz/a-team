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
import org.zeromq.ZMQException;

public class Server implements Runnable {

    private static final Logger logger = LogManager.getLogger(Server.class);

    Context zmqContext;
    Socket socket;
    Game gameState;

    public Server() {
        // if no difficulty specified, start with easy (0)
        this(System.currentTimeMillis(), 0);
    }

    public Server(long seed, int difficulty) {
        // Grab a context object with one I/O thread.
        logger.info("Server running with seed: " + seed);
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.ROUTER);
        gameState = new Game(seed, difficulty);
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
        try {
            socket.bind("tcp://*:2323");
        } catch (ZMQException e) {
            // Address already in use
            // Not sure how to force it to be unbound
        }

        Poller items = zmqContext.poller(1);
        items.register(socket, Poller.POLLIN);

        // while (!Thread.currentThread().isInterrupted()) {
        while (true) {
            try {
                if (items.poll(2000) < 0) {
                    return; //  Interrupted
                }
            } catch (Exception e) {
                // Seeing some ClosedSelectorException
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
                            for (Player player : gameState.players.getArray()) {
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

            Player remove = null;
            for (Player player : gameState.players.getArray()) {
                logger.trace("Checking on " + player.uuid);
                if (player.getPulseTime() != 0
                        && (System.currentTimeMillis() - player.getPulseTime() > 5000)) {
                    remove = player;
                }
            }

            if (remove != null) {
                logger.info("remove this guy " + remove.getSuspect());

                // Remove the player
                gameState.processMessage(Message.internalRemovePlayer(remove));

                // If the game has started, end the game
                // We could add the cards to the face up pile and continue the game
                if (gameState.getGameStarted()) {
                    gameState.processMessage(Message.internalGameEnd());
                    for (Player notifyPlayer : gameState.players.getArray()) {
                        logger.trace("Sending broadcast to " + notifyPlayer.uuid);
                        try {
                            sendMessage(
                                    notifyPlayer.uuid,
                                    Message.error(
                                            "Game is over! "
                                                    + remove.getSuspect().getName()
                                                    + " has disconnected!"));
                        } catch (Exception e) {
                            logger.error("Failed to send game over message.");
                            e.printStackTrace();
                        }
                    }

                    try {
                        disconnect();
                        return;
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    public void disconnect() throws Exception {
        socket.close();
        zmqContext.close();
        return;
    }
}
