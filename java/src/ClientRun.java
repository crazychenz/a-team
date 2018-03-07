import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import clueless.Client;

public class ClientRun {

	private static final Logger logger =
		LogManager.getLogger(ClientRun.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("Starting client");
		Client a = new Client();
		a.connectToServer();
	}

}
