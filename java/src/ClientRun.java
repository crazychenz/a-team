import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import clueless.SimpleClient;

import java.util.concurrent.TimeUnit;

public class ClientRun {

    private static final Logger logger =
        LogManager.getLogger(ClientRun.class);

	public static void main(String[] args) {
		logger.info("Starting client");
		SimpleClient client = new SimpleClient();
		try {
        		client.connectToServer();
        		client.connectToGame();
        		client.connectToGame();
        		client.connectToGame();
        		TimeUnit.SECONDS.sleep(5);
		} catch (Exception e) {
		    logger.error("Client died.");
		    System.exit(-1);
		}

	}

}
