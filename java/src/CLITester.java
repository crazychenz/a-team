import clueless.CLI;
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

    public static void main(String[] args) {
        String resp;
        logger.info("Starting CLI tester.");
        CLI.init(args);

        // Create all possible clients.
        CLI cli1 = new CLI();
        CLI cli2 = new CLI();
        CLI cli3 = new CLI();
        CLI cli4 = new CLI();
        CLI cli5 = new CLI();
        CLI cli6 = new CLI();

        resp = CLI.handleCommand(cli1, "config Plum\n");
        assert_null(resp, "Configure Player #1");
        resp = CLI.handleCommand(cli2, "config Peacock\n");
        assert_null(resp, "Configure Player #2");
        resp = CLI.handleCommand(cli3, "config Scarlet\n");
        assert_null(resp, "Configure Player #3");
        resp = CLI.handleCommand(cli4, "config White\n");
        assert_null(resp, "Configure Player #4");
        resp = CLI.handleCommand(cli5, "config Green\n");
        assert_null(resp, "Configure Player #5");
        resp = CLI.handleCommand(cli6, "config Mustard\n");
        assert_null(resp, "Configure Player #6");
    }
}
