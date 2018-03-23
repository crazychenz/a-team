import clueless.client.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientRun {

    private static final Logger logger = LogManager.getLogger(ClientRun.class);

    public static void main(String[] args) {
        logger.info("Starting client");
        CLI.main(args);
    }
}
