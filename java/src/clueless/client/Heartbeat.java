package clueless.client;

import clueless.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client heartbeat generator.
 *
 * @author ateam
 */
public class Heartbeat implements Runnable {

    private static final Logger logger = LogManager.getLogger(Heartbeat.class);

    Client client;
    long duration;

    /**
     * Constructor
     *
     * @param c The client to send heartbeat from.
     * @param d Duration between heartbeats.
     */
    public Heartbeat(Client c, long d) {
        duration = d;
        client = c;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                client.sendMessage(Message.clientPulse());
                Thread.sleep(duration);
            } catch (Exception e) {
                logger.error("Something wrong with heartbeat thread.");
            }
        }
    }
}
