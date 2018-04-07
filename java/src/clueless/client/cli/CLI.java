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
import org.jline.reader.impl.DefaultParser;
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
    private Client client;
    private ClientState clientState;
    private EventHandler evtHandler;
    private Watchdog watchdog;
    private Thread watchdogThread;
    private Heartbeat heartbeat;
    private Thread heartbeatThread;

    // Terminal Properties
    private static Terminal terminal;
    private static LineReader reader;
    private static PrintWriter termout;

    private static DefaultParser parser;

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

    public static void buildCardsMap(
            HashMap<String, Card> map, boolean suspects, boolean locations, boolean weapons) {
        if (suspects) {
            map.put("Green", SuspectCard.SUSPECT_GREEN);
            map.put("Mustard", SuspectCard.SUSPECT_MUSTARD);
            map.put("Peacock", SuspectCard.SUSPECT_PEACOCK);
            map.put("Plum", SuspectCard.SUSPECT_PLUM);
            map.put("Scarlet", SuspectCard.SUSPECT_SCARLET);
            map.put("White", SuspectCard.SUSPECT_WHITE);
        }

        if (locations) {
            map.put("Ballroom", RoomCard.LOCATION_BALLROOM);
            map.put("Billiard", RoomCard.LOCATION_BILLIARDROOM);
            map.put("Conservatory", RoomCard.LOCATION_CONSERVATORY);
            map.put("Dining", RoomCard.LOCATION_DININGROOM);
            map.put("Hall", RoomCard.LOCATION_HALL);
            map.put("Kitchen", RoomCard.LOCATION_KITCHEN);
            map.put("Library", RoomCard.LOCATION_LIBRARY);
            map.put("Lounge", RoomCard.LOCATION_LOUNGE);
            map.put("Study", RoomCard.LOCATION_STUDY);
        }

        if (weapons) {
            map.put("Revolver", WeaponCard.WEAPON_REVOLVER);
            map.put("Pipe", WeaponCard.WEAPON_LEADPIPE);
            map.put("Rope", WeaponCard.WEAPON_ROPE);
            map.put("Candlestick", WeaponCard.WEAPON_CANDLESTICK);
            map.put("Wrench", WeaponCard.WEAPON_WRENCH);
            map.put("Dagger", WeaponCard.WEAPON_DAGGER);
        }
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

    public static void buildDirectionMap(HashMap<String, DirectionsEnum> map) {
        map.put("north", DirectionsEnum.DIRECTION_NORTH);
        map.put("south", DirectionsEnum.DIRECTION_SOUTH);
        map.put("east", DirectionsEnum.DIRECTION_EAST);
        map.put("west", DirectionsEnum.DIRECTION_WEST);
        map.put("secret", DirectionsEnum.DIRECTION_SECRET);
    }

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

        label:
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
                    break label;
            }
        }
        return argMap;
    }

    public static String handleCommand(CLI cli, String line) {
        Client client = cli.client;
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
            client.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Failed to send Message: " + msg.getMessageID());
        }

        return null;
    }

    public void sendMessage(Message msg) {
        try {
            client.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Failed to send Message: " + msg.getMessageID());
        }
    }

    public CLI() {
        this(null, null, null);
    }

    public CLI(EventHandler evtHandler, ClientState stateParam, Watchdog dogParam) {

        if (stateParam == null) {
            // Where client side state lives
            clientState = new ClientState();
        } else {
            clientState = stateParam;
        }

        if (dogParam == null) {
            // Initialize watchdog thread
            watchdog = new Watchdog(10000);
            watchdog.pulse();
        } else {
            watchdog = dogParam;
        }
        watchdogThread = new Thread(watchdog);
        // TODO start thread outside of constructor
        watchdogThread.start();

        // Client side event handler (for CLI user interface)
        if (evtHandler == null) {
            this.evtHandler = new CLIEventHandler(clientState, watchdog);
        } else {
            this.evtHandler = evtHandler;
        }

        // Initialize the Client link.
        client = new Client(this.evtHandler);
        logger.info("Client UUID: " + client.uuid);

        try {
            // logger.info("argmap address " + argMap.get("address"));
            // logger.info("argmap port " + argMap.get("port"));
            client.connect(argMap.get("address"), argMap.get("port"));

            // Do the initial available suspect fetch
            client.sendMessage(Message.clientConnect());
        } catch (Exception e) {
            logger.error("Exception in connect client.");
        }

        // Init heartbeat thread (duration half length of watchdog timeout)
        heartbeat = new Heartbeat(client, 1000);
        heartbeatThread = new Thread(heartbeat);
        // TODO start thread outside of constructor
        heartbeatThread.start();
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
                        node("disprove"));

        reader =
                LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(completer)
                        .parser(parser)
                        .build();

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

        parser = new DefaultParser();
        parser.setEofOnUnclosedQuote(true);
    }

    public static void main(String[] args) {
        logger.info("Starting interactive client CLI");

        // Initialize common static environment
        init(args);

        // Create an instance of our CLI state
        CLI cli = new CLI();

        // Start interactive loop
        doInteractiveSession(cli);
    }
}
