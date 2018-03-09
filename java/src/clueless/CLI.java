package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import java.util.HashMap;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.builtins.Completers.TreeCompleter;
import static org.jline.builtins.Completers.TreeCompleter.node;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.ParsedLine;

public class CLI {

    private static final Logger logger
            = LogManager.getLogger(CLI.class);

    public static Object[] buildSuspectMap(HashMap<String, CardsEnum> map) {
        map.put("green", CardsEnum.SUSPECT_GREEN);
        map.put("mustard", CardsEnum.SUSPECT_MUSTARD);
        map.put("peacock", CardsEnum.SUSPECT_PEACOCK);
        map.put("plum", CardsEnum.SUSPECT_PLUM);
        map.put("scarlet", CardsEnum.SUSPECT_SCARLET);
        map.put("white", CardsEnum.SUSPECT_WHITE);

        Object[] nodes = new Object[map.size() + 1];
        nodes[0] = "config";
        int i = 1;
        for (String suspect : map.keySet()) {
            nodes[i] = node(suspect);
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
                    System.out.println(""
                            + "--address <server-ip>\n"
                            + "    The IPv4 address or hostname of the server.\n"
                            + "--port <server-port>\n"
                            + "    The TCP port number of the server.\n"
                            + "--help\n"
                            + "    This help message.\n");
                    System.exit(0);
                default:
                    index++;
                    break label;
            }
        }
        return argMap;
    }

    public static void main(String[] args) {
        logger.info("Starting client CLI");

        // Parse command line arguments
        HashMap<String, String> argMap = argumentHandler(args);

        // Where client side state lives
        ClientState clientState = new ClientState();

        // Initialize watchdog thread
        Watchdog watchdog = new Watchdog(10000);
        Thread watchdogThread = new Thread(watchdog);
        watchdogThread.start();

        // Client side event handler (for CLI user interface)
        CLIEventHandler evtHandler = new CLIEventHandler(clientState, watchdog);

        // Initialize the Client link.
        Client client = new Client(evtHandler);
        logger.info("Client UUID: " + client.uuid);

        try {
            client.connect(argMap.get("address"), argMap.get("port"));

            // Do the initial available suspect fetch
            client.sendMessage(Message.clientConnect());
        } catch (Exception e) {
            logger.error("Exception in connect client.");
        }

        // Init heartbeat thread (duration half length of watchdog timeout)
        Heartbeat heartbeat = new Heartbeat(client, 5000);
        Thread heartbeatThread = new Thread(heartbeat);
        heartbeatThread.start();

        HashMap<String, CardsEnum> suspectStrToEnum
                = new HashMap<String, CardsEnum>();
        Object[] suspectNodes = buildSuspectMap(suspectStrToEnum);

        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder().build();
        } catch (IOException e) {
            logger.error("Failed to build terminal.");
            System.exit(-1);
        }

        TreeCompleter completer = new TreeCompleter(
                node("exit"),
                node("quit"),
                node("help"),
                node(suspectNodes),
                node("chat")
        );

        DefaultParser parser = new DefaultParser();
        parser.setEofOnUnclosedQuote(true);

        LineReader reader = LineReaderBuilder.builder().terminal(terminal)
                .completer(completer)
                .parser(parser)
                .build();

        // Display minimum guidance
        terminal.writer().println("Type 'exit' or 'quit' to return to shell.");
        terminal.writer().println("Type 'help' for more info.");
        String prompt = "clueless>";
        while (true) {
            String line = null;
            try {
                line = reader.readLine(prompt);
            } catch (UserInterruptException e) {
                // Ctrl-D
                return;
            } catch (EndOfFileException e) {
                return;
            }
            if (line == null) {
                // Ctrl-C
                return;
            }
            line = line.trim();
            //terminal.writer().println("======>\"" + line + "\"");

            if (line.equalsIgnoreCase("quit")
                    || line.equalsIgnoreCase("exit")) {
                /*try {
                    client.disconnect();
                } catch (Exception e) {
                    logger.error("Failed to disconnect client.");
                    System.exit(-1);
                }*/
                System.exit(0);
            }

            if (line.equalsIgnoreCase("help")) {
                terminal.writer().println("\n"
                        + "chat <message>\n"
                        + "    Send a message to all players\n"
                        + "config <green|mustard|peacock|plum|scarlet|white>\n"
                        + "    Configure the client\n"
                        + "exit|quit\n"
                        + "    Exit clueless CLI\n");
            }

            ParsedLine pl = reader.getParser().parse(line, 0);

            if ("chat".equals(pl.word())) {
                try {
                    client.sendMessage(Message.chatMessage(line.split(" ", 2)[1]));
                } catch (Exception e) {
                    logger.error("failed chat");
                }
            } else if ("config".equals(pl.word())) {
                if (pl.words().size() == 2) {
                    try {
                        client.sendMessage(Message.clientConfig(suspectStrToEnum.get(pl.words().get(1))));
                    } catch (Exception e) {
                        logger.error("failed chat");
                    }
                }
            }

        }
    }
}
