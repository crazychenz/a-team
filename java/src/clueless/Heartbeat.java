package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Heartbeat implements Runnable {

	private static final Logger logger
			= LogManager.getLogger(Heartbeat.class);

	Client client;
	long duration;

	public Heartbeat(Client c, long d) {
		duration = d;
		client = c;
	}

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
