import clueless.client.cli.*;
import clueless.client.gooey.*;
import clueless.server.*;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** @author chenz */
public class EntryPoint {
    private static final Logger logger = LogManager.getLogger(EntryPoint.class);

    public static void showUsage(String[] args) {
        PrintStream out = System.out;
        out.println("Usage: java -jar clueless.jar [OPTIONS...]");
        out.println("Clueless is a game similar to Clue.\n");
        out.println("Arguments:");
        out.println("   --help           This help message.");
        out.println("   --enable-logger  Enable version logging.");
        out.println("   --seed           PRNG Seed (Server Only)");
        out.println("   --difficulty     Game difficulty (Server Only) <easy, med, hard>");
        out.println("\nMutually Exclusive Arguments:");
        out.println("   --server-only    Run this process as dedicated server.");
        out.println("   --cli-client     Start the CLI Client");
        out.println("   --gui-client     Start the GUI Client");
        out.println("");
    }

    public static void startServerOnly(String[] args) {
        logger.info("Starting Dedicated Server.");

        Server server;
        Thread serverThread;
        Integer seed = null;
        String difficulty = null;
        boolean hasSeed = false;
        boolean hasDifficulty = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--seed")) {
                seed = new Integer(args[i + 1]);
                hasSeed = true;
                // break;
            }

            if (args[i].equals("--difficulty")) {
                difficulty = new String(args[i + 1]);
                hasDifficulty = true;
                // break;
            }
        }

        try {
            int internalDifficulty = 0;
            if (difficulty.equals("easy")) {
                internalDifficulty = 0;
            } else if (difficulty.equals("med")) {
                internalDifficulty = 1;
            } else {
                internalDifficulty = 2;
            }
            if (hasSeed && hasDifficulty) {
                server = new Server(seed, internalDifficulty);
            } else if (hasSeed) {
                server = new Server(seed, 0);
            } else if (hasDifficulty) {
                server = new Server(System.currentTimeMillis(), internalDifficulty);
            } else {
                server = new Server();
            }
            serverThread = new Thread(server);
            serverThread.start();

            // TODO: Start a ServerCLI Thread()

            serverThread.join();
            server.disconnect();
        } catch (Exception e) {
            logger.error("Something went wrong in the server.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void startCliClient(String[] args) {
        logger.info("Starting CLI Client");
        CLI.main(args);
    }

    public static void startGooeyClient(String[] args) {
        logger.info("Starting Gooey Client");
        Gooey.main(args);
    }

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

        // Check if --help was requested
        for (String arg : args) {
            if (arg.equals("--help")) {
                showUsage(args);
                return;
            }
        }

        // Check if we're running as a server only
        for (String arg : args) {
            if (arg.equals("--server-only")) {
                startServerOnly(args);
                return;
            }
        }

        // Check if we're using a CLI Client
        for (String arg : args) {
            if (arg.equals("--cli-client")) {
                startCliClient(args);
                return;
            }
        }

        // Check if we're using a CLI Client
        for (String arg : args) {
            if (arg.equals("--gui-client")) {
                startGooeyClient(args);
                return;
            }
        }
    }
}
