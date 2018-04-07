package clueless.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the Watchdog thread
 *
 * @author ateam
 */
public class Watchdog implements Runnable {

    private static final Logger logger = LogManager.getLogger(Client.class);

    long lastPulse;
    long timeout;

    /**
     * Default constructor
     *
     * <p>TODO: Let the eventhandler handle a watchdog timeout
     *
     * @param timeout The timeout before the Watchdog kills the process
     */
    public Watchdog(long timeout) {
        this.timeout = timeout;
    }

    /** Update the watchdog timeout (to prevent a watchdog termination). */
    public void pulse() {
        lastPulse = System.currentTimeMillis();
    }

    @Override
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
