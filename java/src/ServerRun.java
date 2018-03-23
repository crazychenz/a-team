import clueless.server.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class ServerRun {

    private static final Logger logger = LogManager.getLogger(ServerRun.class);

    public static void main(String[] args) {

        // Check if we have logging enabled before anything else.
        boolean hasLogCfgArg = false;
        for (String arg : args) {
            if (arg.equals("--enable-logger")) {
                hasLogCfgArg = true;
            }
        }
        if (!hasLogCfgArg) {
            // You can also set the root logger:
            Configurator.setRootLevel(Level.OFF);
        }

        /*try {
            argHandler =
                new ServerArgumentHandler(args);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to parse arguments.");
            System.exit(-1);
        }*/

        logger.info("Starting server");
        Server server = new Server();
        Thread serverThread;

        /*try {
            server.bind();
        } catch (Exception e) {
            logger.error("Server bind failed.");
            System.exit(-1);
        }*/

        serverThread = new Thread(server);
        serverThread.start();

        try {
            // ... do admin things ...
        } catch (Exception e) {
            logger.error("Something went wrong in the server.");
            System.exit(-1);
        }

        // TODO: Interrupt the thread?
        // TODO: Join on the thread?
        try {
            serverThread.join();
        } catch (Exception e) {
            logger.error(e);
        }

        try {
            server.disconnect();
        } catch (Exception e) {
            logger.error("Server disconnect failed.");
            System.exit(-1);
        }
    }
}
