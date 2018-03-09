package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Watchdog implements Runnable {

    private static final Logger logger
            = LogManager.getLogger(Client.class);

    long lastPulse;
    long timeout;

    public Watchdog(long timeout) {
        this.timeout = timeout;
        pulse();
    }

    public void pulse() {
        lastPulse = System.currentTimeMillis();
    }

    public void run() {
        logger.trace("Starting watchdog thread.");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                long cur = System.currentTimeMillis();
                if (cur - lastPulse > timeout) {
                    logger.fatal("Watchdog triggered. Server unreachable.");
                    System.exit(-1);
                }
                Thread.sleep(timeout / 2);
            } catch (InterruptedException e) {
                logger.error("Watchdog thread interrupted.");
            }
        }
    }

}
