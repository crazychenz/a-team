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

	private static HashMap<String, DirectionsEnum> directionsStrToEnum;
	private static HashMap<String, String> argMap;
	private static Client client;
	private static ClientState clientState;
	private static HashMap<String, CardsEnum> suspectStrToEnum;
	private static CLIEventHandler evtHandler;
	private static Watchdog watchdog;
	private static Thread watchdogThread;
	private static Heartbeat heartbeat;
	private static Thread heartbeatThread;

	private static Terminal terminal;
	private static DefaultParser parser;
	private static LineReader reader;
	private static ParsedLine pl;
	private static PrintWriter termout;

	public static Object[] buildSuspectMap(HashMap<String, CardsEnum> map) {
		map.put("Green", CardsEnum.SUSPECT_GREEN);
		map.put("Mustard", CardsEnum.SUSPECT_MUSTARD);
		map.put("Peacock", CardsEnum.SUSPECT_PEACOCK);
		map.put("Plum", CardsEnum.SUSPECT_PLUM);
		map.put("Scarlet", CardsEnum.SUSPECT_SCARLET);
		map.put("White", CardsEnum.SUSPECT_WHITE);

		Object[] nodes = new Object[map.size() + 1];
		nodes[0] = "config";
		int i = 1;
		for (String suspect : map.keySet()) {
			nodes[i] = node(suspect);
			i += 1;
		}

		return nodes;
	}

	public static Object[] buildDirectionMap(
			HashMap<String, DirectionsEnum> map) {
		map.put("north", DirectionsEnum.DIRECTION_NORTH);
		map.put("south", DirectionsEnum.DIRECTION_SOUTH);
		map.put("east", DirectionsEnum.DIRECTION_EAST);
		map.put("west", DirectionsEnum.DIRECTION_WEST);
		map.put("secret", DirectionsEnum.DIRECTION_SECRET);

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

	public static void handleCommand(String line) {
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
						logger.info("You've already selected a suspect!");
						break;
					}

					if (pl.words().size() == 2) {
						if (suspectStrToEnum.get(pl.words().get(1)) == null) {
							termout.println("Problem selecting that suspect."
									+ "  Please try again!");
							break;
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
						termout.println("Must config first!");
						break;
					}
					
					client.sendMessage(Message.startGame());
					
				} catch (Exception e) {
					logger.error("failed starting game");
				}
				break;
			case "move":
				try {
					if (!clientState.isConfigured()) {
						termout.println("Must config first!");
						break;
					}

					if (!clientState.getGameState().isGameActive()) {
						termout.println("Must start first!");
						break;
					}

					if (!clientState.isMyTurn()) {
						termout.println("Must be the active player!");
						break;
					}

					if (clientState.isMoved()) {
						termout.println("Already moved this turn!");
						break;
					}

					if (pl.words().size() != 2) {
						termout.println("Must specify a direction to move in."
								+ "  Please try again!");
						break;
					}

					if (directionsStrToEnum.get(pl.words().get(1)) == null) {
						termout.println("Problem moving in that direction."
								+ "  Please try again!");
						break;
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
	}

	public static void main(String[] args) {
		logger.info("Starting client CLI");

		// Parse command line arguments
		argMap = argumentHandler(args);

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

		suspectStrToEnum = new HashMap<>();
		Object[] suspectNodes = buildSuspectMap(suspectStrToEnum);

		directionsStrToEnum = new HashMap<>();
		Object[] directionNodes = buildDirectionMap(directionsStrToEnum);

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
				node("chat")
		);

		parser = new DefaultParser();
		parser.setEofOnUnclosedQuote(true);

		reader = LineReaderBuilder.builder().terminal(terminal)
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
				terminal.writer().println("\n"
						+ "chat <message>\n"
						+ "    Send a message to all players\n"
						+ "config <"
						+ clientState.getAvailableSuspects().toString()
						+ ">\n"
						+ "    Configure the client\n"
						+ "start\n"
						+ "    Start the game\n"
						+ "move <north|south|east|west|secret>\n"
						+ "    Move in the given direction\n"
						+ "done\n"
						+ "    End your turn\n"
						+ "exit|quit\n"
						+ "    Exit clueless CLI\n");
			}

			pl = reader.getParser().parse(line, 0);
			if (null != pl.word()) {
				handleCommand(line);
			}

		}
	}
}
