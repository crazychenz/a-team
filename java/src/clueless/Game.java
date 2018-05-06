package clueless;

import clueless.io.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private GameBoard board;
    private boolean gameStarted;
    private int difficulty =
            0; // 0 (easy), 1 (medium), 2 (hard) for setting number of face up cards.  Probably
    // should be an enum
    // TODO make this a server command line argument configurable or make it configurable by clients
    public PlayerMgr players;

    private long prngSeed;

    Suggestion suggestion;

    public Game(long seed, int difficulty) {
        gameStarted = false;
        prngSeed = seed;
        this.difficulty = difficulty;
        players = new PlayerMgr();
        board = new GameBoard();
    }

    public Message processMessage(Message msg) {
        Player player;
        logger.trace("Processing the message");

        switch (msg.getMessageID()) {
            case MESSAGE_CLIENT_CONNECTED:
                // Client just connected
                // Available suspects in pulse since it can change on the fly, shouldn't send it out
                // just once

                // return sendAvailableSuspects();
                break;
            case MESSAGE_CLIENT_START_GAME:
                // Client chose to start the game
                // Should check with all other clients before starting
                startGame();
                break;
            case MESSAGE_CHAT_FROM_CLIENT:
                // Chat from client
                logger.info("Chat from client: " + msg.getMessageData());

                String messageWithUsername =
                        players.byUuid(msg.getFromUuid()).getUsername() + ": " + msg.asString();

                Message override = Message.chatMessage(messageWithUsername);
                override.setBroadcast(true);
                return override;
            case MESSAGE_CHAT_FROM_SERVER:
                // This shouldn't happen
                logger.info("Chat from server: " + msg.getMessageData());
                break;
            case MESSAGE_CLIENT_CONFIG:
                // Client picked a suspect (and username, any other configurables, etc)
                Configuration config = msg.asConfigData();
                SuspectCard pickedSuspect = config.getSuspectCard();
                String username = config.getUsername();

                for (Suspect s : board.getAllSuspects()) {
                    if (s.getSuspect().equals(pickedSuspect)) {
                        // BUG: I feel a race condition here.
                        if (s.getActive()) {
                            logger.info("Suspect already chosen!");
                            return Message.failedConfig("Suspect already chosen!");
                        } else {
                            s.setActive(true);
                        }
                    }
                }
                players.add(pickedSuspect, msg.getFromUuid(), username);
                // TODO: Develop acknowledgement
                break;
            case MESSAGE_CLIENT_MOVE:
                // move from active player
                player = players.current();
                if (player.uuid.equals(msg.getFromUuid())) {
                    Suspect suspect = board.getSuspectByCard(player.getSuspect());
                    DirectionsEnum dir = msg.asDirectionsEnum();
                    if (suspect.move(dir)) {
                        // valid move
                        // the person can now accuse or end their turn
                    } else {
                        // invalid move
                        logger.info("Unable to move there!");
                        return Message.failedMove("Unable to move there!");
                    }
                }
                // move from non-active player
                else {
                    logger.info("Non-active player tried to move!");
                    return Message.serverMessage("Non-active player tried to move!");
                }
                break;
            case MESSAGE_CLIENT_SUGGEST:

                // handle the suggestion
                if (players.current().uuid.equals(msg.getFromUuid())) {
                    logger.debug("Suggesting...");
                    players.setSuggestionPlayer();

                    suggestion = msg.asSuggestion();
                    logger.debug(suggestion);

                    board.getSuspectByCard(suggestion.getSuspect())
                            .moveForSuggestion(suggestion.getRoom());
                    board.getWeaponByCard(suggestion.getWeapon())
                            .moveForSuggestion(suggestion.getRoom());

                    Message response =
                            Message.relaySuggestion(suggestion, players.getNextDisprovePlayer());
                    response.setBroadcast(true);
                    return response;
                }

                break;
            case MESSAGE_CLIENT_ACCUSE:
                if (players.current().uuid.equals(msg.getFromUuid())) {
                    if (handleAccuse(msg.asSuggestion())) {
                        // Win!
                        // End the game, alert everybody
                        // @todo Perhaps a client ID or username would be better?
                        gameStarted = false;
                        Message endMessage =
                                Message.serverMessage(
                                        "The game has been won by "
                                                + players.current().getSuspect()
                                                + "!");
                        endMessage.setBroadcast(true);
                        return endMessage;
                    } else {
                        // Bad accusation!
                        // Don't allow the player to continue making moves, but they must remain to
                        // disprove suggestions
                        players.current().setPlaying(false);
                        if (!players.setNextPlayer()) {
                            logger.info("No more active players!  Game is ending!");
                            gameStarted = false;
                            Message endMessage =
                                    Message.serverMessage(
                                            "All players have made incorrect accusations! The game is over!");
                            endMessage.setBroadcast(true);
                            return endMessage;
                        }
                        logger.info("Bad accusation!");
                        return Message.serverMessage(
                                "Bad accusation!  You must sit out the rest of the game.");
                    }
                }
                logger.info("Non-active player tried to accuse!");
                return Message.serverMessage("Non-active player tried to accuse!");
            case MESSAGE_CLIENT_END_TURN:
                if (players.current().uuid.equals(msg.getFromUuid())) {
                    players.setNextPlayer();
                }
                break;
            case MESSAGE_PULSE:
                // return echo request
                player = players.byUuid(msg.getFromUuid());
                if (player != null) {
                    player.setPulseTime();
                }
                return Message.sendGameStatePulse(
                        new GameStatePulse(gameStarted, board, players, player));
            case MESSAGE_CLIENT_RESPONSE_SUGGEST:
                Card proof = msg.asCard();
                if (proof != null) {
                    // The disproving player was able to disprove
                    // Let the suggesting player know

                    Message response = Message.serverRespondSuggestion(proof);
                    response.setBroadcast(true);
                    response.setToUuid(players.getSuggestionPlayer().uuid.toString());
                    return response;
                }

                Player next = players.getNextDisprovePlayer();
                if (next != null) {
                    // try the next player
                    Message response = Message.relaySuggestion(suggestion, next);
                    response.setBroadcast(true);
                    return response;
                } else {
                    // No more players left to disprove
                    // Let the suggesting player know
                    Message response = Message.serverRespondSuggestion(null);
                    response.setBroadcast(true);
                    response.setToUuid(players.getSuggestionPlayer().uuid.toString());
                    return response;
                }
            case MESSAGE_INTERNAL_SERVER_REMOVE_PLAYER:
                Player playerToRemove = msg.asPlayer();
                for (Suspect s : board.getAllSuspects()) {
                    if (s.getSuspect().equals(playerToRemove.getSuspect())) {
                        if (!s.getActive()) {
                            logger.info("Suspect not active!");
                        } else {
                            s.setActive(false);
                            players.getArray().remove(playerToRemove);
                        }
                    }
                }
                break;
            case MESSAGE_INTERNAL_SERVER_END_GAME:
                setGameStarted(false);
                break;
            default:
                break;
        }

        return null;
    }

    private boolean startGame() {
        if (!gameStarted) {
            if (players.count() < 3) {
                logger.info("Not enough players to start the game");
                return gameStarted;
            }

            board.dealCards(players, prngSeed, difficulty);
            setGameStarted(true);
            // TODO: Make scarlet the activePlayerRef
            // TODO: All users should be using peice nearest to them
            //       and all peices have a starting location
            //       and all turns go to the left starting with scarlet,
            //       therefore the turn ordering is always the same.
            // players.setNextPlayer();
        }
        return getGameStarted();
    }

    private void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean getGameStarted() {
        return gameStarted;
    }

    // TODO: Seems out of scope
    private boolean handleAccuse(Suggestion cards) {
        return board.accuse(cards);
    }
}
