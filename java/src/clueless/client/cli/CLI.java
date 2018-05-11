package clueless.client.cli;

import static org.jline.builtins.Completers.TreeCompleter.node;

import clueless.*;
import clueless.client.*;
import clueless.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class CLI {

    private static final Logger logger = LogManager.getLogger(CLI.class);

    // Static Maps
    private static HashMap<String, String> argMap;
    private static Object[] directionNodes;
    private static Object[] suspectNodes;
    private static Object[] accuseNodes;
    private static Object[] suggestNodes;

    // Client Specific Properties
    private ClientState clientState;

    // Terminal Properties
    private static Terminal terminal;
    private static LineReader reader;
    private static PrintWriter termout;

    public CLI() {
        clientState = new ClientState();
    }

    public void startup() {
        String addr = argMap.get("address");
        String port = argMap.get("port");
        clientState.startup(new CLIEventHandler(clientState), addr, port);
    }

    public static Object[] buildSuspectNodes(HashMap<String, Card> map) {
        Object[] nodes = new Object[map.size() + 1];
        nodes[0] = "config";
        int i = 1;
        for (String suspect : map.keySet()) {
            nodes[i] = node(suspect);
            i += 1;
        }

        return nodes;
    }

    public static Object[] buildAccuseNodes(HashMap<String, Card> map) {
        Object[] nodes = new Object[map.size() + 1];
        nodes[0] = "accuse";
        int i = 1;
        for (String card : map.keySet()) {
            nodes[i] = node(card);
            i += 1;
        }

        return nodes;
    }

    public static Object[] buildSuggestNodes(HashMap<String, Card> map) {
        Object[] nodes = new Object[map.size() + 1];
        nodes[0] = "suggest";
        int i = 1;
        for (String card : map.keySet()) {
            nodes[i] = node(card);
            i += 1;
        }

        return nodes;
    }

    /*public Object[] buildDisproveNodes(HashMap<String, CardsEnum> map) {
        Object[] nodes = new Object[map.size() + 1];
        nodes[0] = "disprove";
        int i = 1;
        for (String suspect : map.keySet()) {
            nodes[i] = node(suspect);
            i += 1;
        }

        return nodes;
    }

    public static void buildDisproveMap(HashMap<String, CardsEnum> map) {
        if (clientState.isDisproving()) {
            for (Card card : clientState.getDisproveCards()) {
                map.put(card.getCardEnum().getLabel(), card.getCardEnum());
            }
        }
    }*/

    public static Object[] buildDirectionNodes(HashMap<String, DirectionsEnum> map) {
        Object[] nodes = new Object[map.size() + 1];
        nodes[0] = "move";
        int i = 1;
        for (String direction : map.keySet()) {
            nodes[i] = node(direction);
            i += 1;
        }

        return nodes;
    }

    public static HashMap<String, String> argumentHandler(String[] args) {
        int index = 0;
        HashMap<String, String> argMap = new HashMap<String, String>();
        argMap.put("addess", null);
        argMap.put("port", null);
        while (args.length > index) {
            switch (args[index]) {
                case "--address":
                    index++;
                    argMap.put("address", args[index]);
                    index++;
                    continue;
                case "--port":
                    index++;
                    argMap.put("port", args[index]);
                    index++;
                    continue;
                case "--help":
                    System.out.println(
                            ""
                                    + "--address <server-ip>\n"
                                    + "    The IPv4 address or hostname of the server.\n"
                                    + "--port <server-port>\n"
                                    + "    The TCP port number of the server.\n"
                                    + "--help\n"
                                    + "    This help message.\n");
                    System.exit(0);
                    break;
                default:
                    index++;
            }
        }
        return argMap;
    }

    public static String handleCommand(CLI cli, String line) {
        Message msg = ClientCommand.processCommand(cli.clientState, line);
        if (msg == null) {
            return null;
        }
        if (msg.getMessageID() == MessagesEnum.MESSAGE_ERROR) {
            return (String) msg.getMessageData();
        } else if (msg.getMessageID() == MessagesEnum.MESSAGE_INFO) {
            return (String) msg.getMessageData();
        }

        try {
            cli.clientState.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Failed to send Message: " + msg.getMessageID());
        }

        return null;
    }

    public void sendMessage(Message msg) {
        try {
            clientState.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Failed to send Message: " + msg.getMessageID());
        }
    }

    public static void doInteractiveSession(CLI cli) {
        terminal = null;
        try {
            terminal = TerminalBuilder.builder().build();
        } catch (IOException e) {
            logger.error("Failed to build terminal.");
            System.exit(-1);
        }

        termout = terminal.writer();

        TreeCompleter completer =
                new TreeCompleter(
                        node("exit"),
                        node("quit"),
                        node("help"),
                        node("start"),
                        node(directionNodes),
                        node(suspectNodes),
                        node(accuseNodes),
                        node(suggestNodes),
                        node("done"),
                        node("chat"),
                        node("cards"),
                        node("board"),
                        node("disprove"),
                        node("note"));

        reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).build();

        // Display minimum guidance
        termout.println("Type 'exit' or 'quit' to return to shell.");
        termout.println("Type 'help' for more info.");
        String prompt = "clueless>";
        while (true) {
            String line = null;
            try {
                if (cli.clientState.isConfigured()) {
                    prompt =
                            "My suspect: "
                                    + cli.clientState.getMySuspect().getName()
                                    + "\nclueless>";
                }
                line = reader.readLine(prompt);
            } catch (UserInterruptException | EndOfFileException e) {
                // Ctrl-D
                return;
            }
            if (line == null) {
                // Ctrl-C
                return;
            }
            line = line.trim();
            // terminal.writer().println("======>\"" + line + "\"");

            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                /*try {
                    client.disconnect();
                } catch (Exception e) {
                    logger.error("Failed to disconnect client.");
                    System.exit(-1);
                }*/
                System.exit(0);
            }

            String response = handleCommand(cli, line);
            if (response != null) {
                termout.println(response);
            }
        }
    }

    public static void init(String[] args) {
        // Parse command line arguments
        argMap = argumentHandler(args);

        // Setup some static mappings
        suspectNodes = buildSuspectNodes(ClientCommand.suspectStrToEnum);
        accuseNodes = buildAccuseNodes(ClientCommand.accuseStrToEnum);
        suggestNodes = buildSuggestNodes(ClientCommand.suggestStrToEnum);
        directionNodes = buildDirectionNodes(ClientCommand.directionsStrToEnum);
    }

    public static void main(String[] args) {
        logger.info("Starting interactive client CLI");

        // Initialize common static environment
        init(args);

        // Create an instance of our CLI state
        CLI cli = new CLI();
        cli.startup();

        // Start interactive loop
        doInteractiveSession(cli);
    }
}
