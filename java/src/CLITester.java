import clueless.client.cli.*;
import clueless.server.Server;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CLITester {

    private static final Logger logger = LogManager.getLogger(CLITester.class);

    public static void assert_null(Object obj, String desc) {
        if (obj != null) {
            logger.error("FAIL: " + desc);
            System.exit(1);
        }
        logger.info("PASS: " + desc);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        PrintStream out = System.out;
        String resp;
        logger.info("Starting CLI tester.");

        logger.info("Initializing the server thread.");
        try {
            Server server = new Server(1, 0);
            Thread serverThread = new Thread(server);
            serverThread.start();
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("Something went wrong in the server.");
            e.printStackTrace();
            System.exit(-1);
        }

        logger.info("Initializing the CLI environment.");
        CLI.init(args);

        // Create all possible clients.
        CLI plum = new CLI();
        plum.startup();
        CLI peacock = new CLI();
        peacock.startup();
        CLI scarlet = new CLI();
        scarlet.startup();
        CLI white = new CLI();
        white.startup();
        CLI green = new CLI();
        green.startup();
        CLI mustard = new CLI();
        mustard.startup();

        // Register Everyone
        resp = CLI.handleCommand(plum, "config Plum plumo\n");
        assert_null(resp, "Configure Player #1");
        resp = CLI.handleCommand(peacock, "config Peacock birdy\n");
        assert_null(resp, "Configure Player #2");
        resp = CLI.handleCommand(scarlet, "config Scarlet dragon\n");
        assert_null(resp, "Configure Player #3");
        resp = CLI.handleCommand(white, "config White egg\n");
        assert_null(resp, "Configure Player #4");
        resp = CLI.handleCommand(green, "config Green clover\n");
        assert_null(resp, "Configure Player #5");
        resp = CLI.handleCommand(mustard, "config Mustard ketchup\n");
        assert_null(resp, "Configure Player #6");

        // Start the game
        resp = CLI.handleCommand(scarlet, "start\n");
        sleep(5000);

        // Scarlet should always go first
        resp = CLI.handleCommand(scarlet, "move east\n");
        out.println(resp);
        sleep(1000);

        // Check where we are
        resp = CLI.handleCommand(scarlet, "board\n");
        out.println(resp);
        sleep(1000);

        // Make suggestion
        resp = CLI.handleCommand(scarlet, "suggest Mustard Rope\n");
        out.println(resp);
        sleep(1000);

        resp = CLI.handleCommand(mustard, "disprove\n");
        out.println(resp);
        sleep(1000);

        resp = CLI.handleCommand(white, "disprove\n");
        out.println(resp);
        sleep(1000);

        resp = CLI.handleCommand(green, "disprove\n");
        out.println(resp);
        sleep(1000);

        resp = CLI.handleCommand(peacock, "disprove Mustard\n");
        out.println(resp);
        sleep(1000);

        resp = CLI.handleCommand(scarlet, "done\n");
        out.println(resp);
        sleep(1000);

        // BUG: It doesn't like the quote
        // resp = CLI.handleCommand(mustard, "chat I told you I didn\'t do it!\n");
        // out.println(resp);
        // sleep(1000);

        // BUG: Items with spaces aren't working correctly. Example: 'Lead Pipe'
        resp = CLI.handleCommand(mustard, "accuse Peacock Study Pipe\n");
        out.println(resp);
        sleep(1000);

        // Kill all the clients
        resp = CLI.handleCommand(plum, "exit\n");
        resp = CLI.handleCommand(peacock, "exit\n");
        resp = CLI.handleCommand(scarlet, "exit\n");
        resp = CLI.handleCommand(white, "exit\n");
        resp = CLI.handleCommand(green, "exit\n");
        resp = CLI.handleCommand(mustard, "exit\n");
    }
}
