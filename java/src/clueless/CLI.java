package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.PrintWriter;

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

	// Static Maps
	private static HashMap<String, String> argMap;
	private static HashMap<String, DirectionsEnum> directionsStrToEnum;
	private static HashMap<String, CardsEnum> suspectStrToEnum;
	private static Object[] directionNodes;
	private static Object[] suspectNodes;
	
	// Client Specific Properties
	private Client client;
	private ClientState clientState;
	private CLIEventHandler evtHandler;
	private Watchdog watchdog;
	private Thread watchdogThread;
	private Heartbeat heartbeat;
	private Thread heartbeatThread;

	// Terminal Properties
	private static Terminal terminal;
	private static LineReader reader;
	private static PrintWriter termout;
	
	private static DefaultParser parser;

	public static void buildSuspectMap(HashMap<String, CardsEnum> map) {
		map.put("Green", CardsEnum.SUSPECT_GREEN);
		map.put("Mustard", CardsEnum.SUSPECT_MUSTARD);
		map.put("Peacock", CardsEnum.SUSPECT_PEACOCK);
		map.put("Plum", CardsEnum.SUSPECT_PLUM);
		map.put("Scarlet", CardsEnum.SUSPECT_SCARLET);
		map.put("White", CardsEnum.SUSPECT_WHITE);
	}

	public static Object[] buildSuspectNodes(HashMap<String, CardsEnum> map) {
		Object[] nodes = new Object[map.size() + 1];
		nodes[0] = "config";
		int i = 1;
		for (String suspect : map.keySet()) {
			nodes[i] = node(suspect);
			i += 1;
		}

		return nodes;
	}

	public static void buildDirectionMap(
			HashMap<String, DirectionsEnum> map) {
		map.put("north", DirectionsEnum.DIRECTION_NORTH);
		map.put("south", DirectionsEnum.DIRECTION_SOUTH);
		map.put("east", DirectionsEnum.DIRECTION_EAST);
		map.put("west", DirectionsEnum.DIRECTION_WEST);
		map.put("secret", DirectionsEnum.DIRECTION_SECRET);
	}
	
	public static Object[] buildDirectionNodes(
			HashMap<String, DirectionsEnum> map) {
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
					System.out.println(""
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
							return "Problem selecting that suspect."
									+ "  Please try again!";
						}

						logger.info("Selected " 
								+ suspectStrToEnum.get(pl.words().get(1)));
						String card = pl.words().get(1);
						CardsEnum suspect = suspectStrToEnum.get(card);
						client.sendMessage(Message.clientConfig(suspect));
						clientState.setConfigured(true);
						String myCard = pl.words().get(1);
						CardsEnum mySuspect = suspectStrToEnum.get(myCard);
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
			case "cards":
        		if(!clientState.isConfigured()) {
        			return "Must config first!";
        		}
        		else if (!clientState.getGameState().isGameActive()) {
        			return "Must start first!";
        		}
        		else {
        			String toReturn = "";
        			toReturn += "\nYour cards:\n";
        			for(Card card : clientState.getCards()) {
            			toReturn += card.toString() +"\n";
        			}
        			
        			toReturn += "\n\nFace Up Cards:\n";
        			for(Card card : clientState.getFaceUpCards()) {
        				toReturn += card.toString() +"\n";
        			}
            		return toReturn;
        		}
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
						return "Must specify a direction to move in."
								+ "  Please try again!";
					}

					if (directionsStrToEnum.get(pl.words().get(1)) == null) {
						return "Problem moving in that direction."
								+ "  Please try again!";
					}

					logger.info("Moving direction: "
							+ directionsStrToEnum.get(pl.words().get(1)));
					
					String dirStr = pl.words().get(1);
					DirectionsEnum dir = directionsStrToEnum.get(dirStr);
					client.sendMessage(Message.moveClient(dir));
					clientState.setMoved(true);
				} catch (Exception e) {
					logger.error("failed starting game");
				}
				break;
			default:
				break;
		}
		return null;
	}
	
	public CLI() {
		// Where client side state lives
		clientState = new ClientState();

		// Initialize watchdog thread
		watchdog = new Watchdog(10000);
		watchdogThread = new Thread(watchdog);
		watchdogThread.start();

		// Client side event handler (for CLI user interface)
		evtHandler = new CLIEventHandler(clientState, watchdog);

		// Initialize the Client link.
		client = new Client(evtHandler);
		logger.info("Client UUID: " + client.uuid);

		try {
			client.connect(argMap.get("address"), argMap.get("port"));

			// Do the initial available suspect fetch
			client.sendMessage(Message.clientConnect());
		} catch (Exception e) {
			logger.error("Exception in connect client.");
		}

		// Init heartbeat thread (duration half length of watchdog timeout)
		heartbeat = new Heartbeat(client, 5000);
		heartbeatThread = new Thread(heartbeat);
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

        TreeCompleter completer = new TreeCompleter(
                node("exit"),
                node("quit"),
                node("help"),
                node("start"),
                node(directionNodes),
                node(suspectNodes),
                node("done"),
                node("chat"),
                node("cards")
        );

		

		reader = LineReaderBuilder.builder().terminal(terminal)
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
                termout.println("\n"
                        + "chat <message>\n"
                        + "    Send a message to all players\n"
                        + "config <" + cli.clientState.getAvailableSuspects().toString() + ">\n"
                        + "    Configure the client\n"
                        + "start\n"
                        + "    Start the game\n"
                        + "move <north|south|east|west|secret>\n"
                        + "    Move in the given direction\n"
                        + "done\n"
                        + "    End your turn\n"
                        + "cards\n"
                        + "    Display your cards and face up cards\n"
                        + "exit|quit\n"
                        + "    Exit clueless CLI\n");
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
		suspectStrToEnum = new HashMap<>();
		buildSuspectMap(suspectStrToEnum);
		suspectNodes = buildSuspectNodes(suspectStrToEnum);
		directionsStrToEnum = new HashMap<>();
		buildDirectionMap(directionsStrToEnum);
		directionNodes = buildDirectionNodes(directionsStrToEnum);
		
		parser = new DefaultParser();
		parser.setEofOnUnclosedQuote(true);
	}
	
	public static void main(String[] args) {
		logger.info("Starting interactive client CLI" + args);

		// Initialize common static environment
		init(args);

		// Create an instance of our CLI state
		CLI cli = new CLI();
		
		// Start interactive loop
		doInteractiveSession(cli);
	}
}
