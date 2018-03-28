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
import org.jline.reader.ParsedLine;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class CLI {

    private static final Logger logger = LogManager.getLogger(CLI.class);

    // Static Maps
    private static HashMap<String, String> argMap;
    private static HashMap<String, DirectionsEnum> directionsStrToEnum;
    private static HashMap<String, Card> suspectStrToEnum;
    private static HashMap<String, Card> accuseStrToEnum;
    private static HashMap<String, Card> suggestStrToEnum;
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
        logger.debug(cli.clientState.getMySuspect() + ": " + line);
        Client client = cli.client;
        ClientState clientState = cli.clientState;

        ParsedLine pl;
        pl = parser.parse(line, 0);
        if (null == pl.word()) {
            return null;
        }

        switch (pl.word()) {
            case "chat":
                try {
                    String chatMsg = line.split(" ", 2)[1];
                    Message msg = Message.chatMessage(chatMsg);
                    client.sendMessage(msg);
                } catch (Exception e) {
                    logger.error("failed chat");
                }
                break;
            case "done":
                try {
                    client.sendMessage(Message.endTurn());
                } catch (Exception e) {
                    logger.error("failed chat");
                }
                break;
            case "config":
                try {
                    if (clientState.isConfigured()) {
                        return "You've already selected a suspect!";
                    }

                    if (pl.words().size() == 2) {
                        if (suspectStrToEnum.get(pl.words().get(1)) == null) {
                            return "Problem selecting that suspect." + "  Please try again!";
                        }

                        logger.info("Selected " + suspectStrToEnum.get(pl.words().get(1)));
                        String card = pl.words().get(1);
                        SuspectCard suspect = (SuspectCard) suspectStrToEnum.get(card);
                        client.sendMessage(Message.clientConfig(suspect));
                        clientState.setConfigured(true);
                        String myCard = pl.words().get(1);
                        SuspectCard mySuspect = (SuspectCard) suspectStrToEnum.get(myCard);
                        clientState.setMySuspect(mySuspect);
                    }
                } catch (Exception e) {
                    logger.error("failed config");
                }
                break;
            case "start":
                try {
                    if (!clientState.isConfigured()) {
                        return "Must config first!";
                    }

                    client.sendMessage(Message.startGame());

                } catch (Exception e) {
                    logger.error("failed starting game");
                }
                break;
            case "disprove":
                try {
                    if (!clientState.isConfigured()) {
                        return "Must config first!";
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return "Must start first!";
                    }

                    if (!clientState.isDisproving()) {
                        return "Must be actively disproving!";
                    }

                    if (pl.words().size() == 2) {
                        HashMap<String, Card> disproveStrToEnum = new HashMap<String, Card>();

                        for (Card card : clientState.getDisproveCards()) {
                            disproveStrToEnum.put(card.getName(), card);
                        }

                        if (disproveStrToEnum.get(pl.words().get(1)) == null) {
                            return "Problem selecting that card." + "  Please try again!";
                        }

                        String card1 = pl.words().get(1);
                        logger.info("Disproving " + card1);
                        Card ce1 = disproveStrToEnum.get(card1);

                        client.sendMessage(Message.clientRespondSuggestion(ce1));
                        clientState.setDisproving(false);
                    } else if (pl.words().size() == 1
                            && clientState.getDisproveCards().size() == 0) {
                        client.sendMessage(Message.clientRespondSuggestion(null));
                    } else {
                        return "Must pick a card to disprove the suggestion!";
                    }
                } catch (Exception e) {
                    logger.error("failed making suggestion");
                }
                break;
            case "cards":
                if (!clientState.isConfigured()) {
                    return "Must config first!";
                } else if (!clientState.getGameState().isGameActive()) {
                    return "Must start first!";
                }

                String toReturn = "";
                toReturn += "\nYour cards:\n";
                for (Card card : clientState.getCards()) {
                    toReturn += card.getName() + "\n";
                }

                toReturn += "\n\nFace Up Cards:\n";
                for (Card card : clientState.getFaceUpCards()) {
                    toReturn += card.getName() + "\n";
                }
                return toReturn;

            case "board":
                if (!clientState.isConfigured()) {
                    return "Must config first!";
                } else if (!clientState.getGameState().isGameActive()) {
                    return "Must start first!";
                } else {
                    /*String toReturn = "\n";
                    toReturn += "\nBoard:\n";
                    for (Entry<CardsEnum, CardsEnum> entry :
                            clientState.getGameState().getSuspectLocations().entrySet()) {
                        toReturn +=
                                "Suspect: "
                                        + entry.getKey()
                                        + "\t"
                                        + "Location: "
                                        + entry.getValue()
                                        + "\n";
                    }

                    toReturn += "\nWeapons:\n";
                    for (Entry<CardsEnum, CardsEnum> entry :
                            clientState.getGameState().getWeaponLocations().entrySet()) {
                        toReturn +=
                                "Weapon: "
                                        + entry.getKey()
                                        + "\t"
                                        + "Location: "
                                        + entry.getValue()
                                        + "\n";
                    }*/

                    BoardBuilder bb = new BoardBuilder(clientState);
                    System.out.println(bb.generateBoard());
                }
                break;
            case "accuse":
                try {
                    if (!clientState.isConfigured()) {
                        return "Must config first!";
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return "Must start first!";
                    }

                    if (!clientState.isMyTurn()) {
                        return "Must be the active player!";
                    }

                    if (pl.words().size() == 4) {

                        if (accuseStrToEnum.get(pl.words().get(1)) == null) {
                            return "Problem selecting card \""
                                    + pl.words().get(1)
                                    + "\". Please try again!";
                        }
                        if (accuseStrToEnum.get(pl.words().get(2)) == null) {
                            return "Problem selecting card \""
                                    + pl.words().get(2)
                                    + "\". Please try again!";
                        }
                        if (accuseStrToEnum.get(pl.words().get(3)) == null) {
                            return "Problem selecting card \""
                                    + pl.words().get(3)
                                    + "\". Please try again!";
                        }
                        String card1 = pl.words().get(1);
                        String card2 = pl.words().get(2);
                        String card3 = pl.words().get(3);
                        logger.info("Accusing " + card1 + card2 + card3);
                        try {
                            Card ce1 = accuseStrToEnum.get(card1);
                            Card ce2 = accuseStrToEnum.get(card2);
                            Card ce3 = accuseStrToEnum.get(card3);

                            Suggestion suggestion = new Suggestion(ce1, ce2, ce3);
                            client.sendMessage(Message.accusation(suggestion));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        return "Must specify a suspect, location, and weapon to accuse!";
                    }
                } catch (Exception e) {
                    logger.error("failed making accusation");
                }
                break;
            case "suggest":
                try {
                    if (!clientState.isConfigured()) {
                        return "Must config first!";
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return "Must start first!";
                    }

                    if (!clientState.isMyTurn()) {
                        return "Must be the active player!";
                    }

                    if (clientState.isSuggested()) {
                        return "Already made a suggestion this turn!";
                    }

                    if ((clientState.getMyLocation() != null)
                            && Hallway.isHallwayId(clientState.getMyLocation())) {
                        return "Cannot make a suggestion in a hallway!";
                    }

                    if (pl.words().size() == 3) {

                        if (suggestStrToEnum.get(pl.words().get(1)) == null) {
                            return "Problem selecting that card." + "  Please try again!";
                        }
                        if (suggestStrToEnum.get(pl.words().get(2)) == null) {
                            return "Problem selecting that card." + "  Please try again!";
                        }
                        String card1 = pl.words().get(1);
                        String card2 = pl.words().get(2);
                        logger.info("Suggesting " + card1 + " " + card2);
                        Card ce1 = suggestStrToEnum.get(card1);
                        Card ce2 = suggestStrToEnum.get(card2);
                        Card location = RoomCard.getById(clientState.getMyLocation());
                        if (ce1 == null) {
                            logger.error("Missing ce1");
                        }
                        if (ce2 == null) {
                            logger.error("Missing ce2");
                        }
                        if (location == null) {
                            logger.error("Missing location");
                        }
                        // ArrayList<CardsEnum> cardsToSend = new ArrayList<CardsEnum>();
                        // cardsToSend.add(ce1);
                        // cardsToSend.add(ce2);
                        // cardsToSend.add(location);
                        // CardWrapper cards = new CardWrapper(cardsToSend);
                        Suggestion cards = new Suggestion(ce1, ce2, location);
                        client.sendMessage(Message.suggestion(cards));
                        clientState.setSuggested(true);

                    } else {
                        return "Must specify a suspect and a weapon to suggest!";
                    }
                } catch (Exception e) {
                    logger.error("failed making suggestion");
                    e.printStackTrace();
                }
                break;
            case "move":
                try {
                    if (!clientState.isConfigured()) {
                        return "Must config first!";
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return "Must start first!";
                    }

                    if (!clientState.isMyTurn()) {
                        return "Must be the active player!";
                    }

                    if (clientState.isMoved()) {
                        return "Already moved this turn!";
                    }

                    if (pl.words().size() != 2) {
                        return "Must specify a direction to move in." + "  Please try again!";
                    }

                    if (directionsStrToEnum.get(pl.words().get(1)) == null) {
                        return "Problem moving in that direction." + "  Please try again!";
                    }

                    logger.info("Moving direction: " + directionsStrToEnum.get(pl.words().get(1)));

                    String dirStr = pl.words().get(1);
                    DirectionsEnum dir = directionsStrToEnum.get(dirStr);
                    client.sendMessage(Message.moveClient(dir));
                    clientState.setMoved(true);
                } catch (Exception e) {
                    logger.error("failed moving");
                }
                break;
            default:
                return "Please enter a valid command!";
        }
        return null;
    }

    public CLI() {
        // Where client side state lives
        clientState = new ClientState();

        // Initialize watchdog thread
        watchdog = new Watchdog(10000);
        watchdog.pulse();
        watchdogThread = new Thread(watchdog);
        // TODO start thread outside of constructor
        watchdogThread.start();

        // Client side event handler (for CLI user interface)
        evtHandler = new CLIEventHandler(clientState, watchdog);

        // Initialize the Client link.
        client = new Client(evtHandler);
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

            if (line.equalsIgnoreCase("help")) {
                if (cli.clientState.getGameState() != null) {
                    termout.println(
                            "\n"
                                    + "chat <message>\n"
                                    + "    Send a message to all players\n"
                                    + "config <"
                                    + cli.clientState.getAvailableSuspects().toString()
                                    + ">\n"
                                    + "    Configure the client\n"
                                    + "start\n"
                                    + "    Start the game\n"
                                    + "move <north|south|east|west|secret>\n"
                                    + "    Move in the given direction\n"
                                    + "done\n"
                                    + "    End your turn\n"
                                    + "cards\n"
                                    + "    Display your cards and face up cards\n"
                                    + "board\n"
                                    + "    Display the location of all weapons and suspects\n"
                                    + "accuse <cards>\n"
                                    + "    Accuse a suspect, location, and weapon combination were the who, where, and what of the crime to win the game!\n"
                                    + "suggest <cards>\n"
                                    + "    Suggest that a suspect and weapon were used in your current location to commit the crime!\n"
                                    + "disprove <card>\n"
                                    + "    Disprove one of the cards from the suggestion!\n"
                                    + "exit|quit\n"
                                    + "    Exit clueless CLI\n");
                } else {
                    termout.println("Connect to the server first!");
                }
            } else {
                String response = handleCommand(cli, line);
                if (response != null) {
                    termout.println(response);
                }
            }
        }
    }

    public static void init(String[] args) {
        // Parse command line arguments
        argMap = argumentHandler(args);

        // Setup some static mappings
        suspectStrToEnum = new HashMap<>();
        buildCardsMap(suspectStrToEnum, true, false, false);
        suspectNodes = buildSuspectNodes(suspectStrToEnum);

        accuseStrToEnum = new HashMap<>();
        buildCardsMap(accuseStrToEnum, true, true, true);
        accuseNodes = buildAccuseNodes(accuseStrToEnum);

        suggestStrToEnum = new HashMap<>();
        buildCardsMap(suggestStrToEnum, true, false, true);
        suggestNodes = buildSuggestNodes(suggestStrToEnum);

        directionsStrToEnum = new HashMap<>();
        buildDirectionMap(directionsStrToEnum);
        directionNodes = buildDirectionNodes(directionsStrToEnum);

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
