
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import clueless.Client;
import clueless.CardsEnum;

import java.lang.Thread;

public class ClientRun {

    private static final Logger logger =
        LogManager.getLogger(ClientRun.class);

    public static void main(String[] args) {
        logger.info("Starting client");
        Client client = new Client();
        Thread clientThread;

        try {
            client.connect();
        } catch (Exception e) {
            logger.error("Client connect failed.");
            System.exit(-1);
        }

        clientThread = new Thread(client);
        clientThread.start();

        try {
            client.sendClientConnect();
            client.sendClientConfig(CardsEnum.SUSPECT_SCARLET);
            client.sendChatMessage("Test chat message");
            // ... do client things ...
        } catch (Exception e) {
            logger.error("Something went wrong in the client.");
            System.exit(-1);
        }

        // TODO: Interrupt the thread?
        try {
            clientThread.join();
        } catch (Exception e) {
            logger.error(e);
        }

        try {
            client.disconnect();
        } catch (Exception e) {
            logger.error("Client disconnect failed.");
            System.exit(-1);
        }

    }

}
