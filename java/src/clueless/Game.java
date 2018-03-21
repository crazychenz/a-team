package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private GameBoard board;
    public boolean classicMode = false;
    public PlayerMgr players;
    private CardWrapper cardsToDisprove;

    public Game() {
        players = new PlayerMgr();
        board = new GameBoard();
        cardsToDisprove = new CardWrapper(new ArrayList<CardsEnum>());
        /*try {
            Weapon.class.newInstance();
            Suspect.class.newInstance();
            Location.class.newInstance();
        } catch (Exception e) {

        }*/
    }

    public Message processMessage(Message msg) {
        Player player;
        logger.debug("Processing the message");

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
                msg.setBroadcast(true);
                return msg;
            case MESSAGE_CHAT_FROM_SERVER:
                // This shouldn't happen
                logger.info("Chat from server: " + msg.getMessageData());
                break;
            case MESSAGE_CLIENT_CONFIG:
                // Client picked a suspect (and username, any other configurables, etc)
                CardsEnum pickedSuspect = (CardsEnum) msg.getMessageData();
                for (Suspect s : board.getAllSuspects()) {
                    if (s.getSuspect() == pickedSuspect) {
                        // BUG: I feel a race condition here.
                        if (s.getActive()) {
                            logger.info("Suspect already chosen!");
                            return Message.failedConfig("Suspect already chosen!");
                        } else {
                            s.setActive(true);
                        }
                    }
                }
                players.add(pickedSuspect, msg.getFromUuid());
                // TODO: Develop acknowledgement
                break;
            case MESSAGE_CLIENT_MOVE:
                // move from active player
                player = players.current();
                if (player.uuid.equals(msg.getFromUuid())) {
                    Suspect suspect = board.getSuspectByEnum(player.getSuspect());
                    DirectionsEnum dir = (DirectionsEnum) msg.getMessageData();
                    if (suspect.move(board, dir)) {
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
                    return Message.failedMove("Non-active player tried to move!");
                }
                break;
            case MESSAGE_CLIENT_SUGGEST:
                // handle the suggestion
                // TODO We need to relay this to each player in turn until they can disprove or not
                // The server can either broadcast this out to everyone, and they check if their
                // uuid matches (add uuid to Message?)
                // Or the server has to send this out directly to each client in succession
                if (players.current().uuid.equals(msg.getFromUuid())) {
                    players.setSuggestionPlayer();
                    cardsToDisprove = (CardWrapper) msg.getMessageData();
                    Message response =
                            Message.relaySuggestion(
                                    cardsToDisprove, players.getNextDisprovePlayer());
                    response.setBroadcast(true);
                    return response;
                }
                break;
            case MESSAGE_CLIENT_ACCUSE:
                if (players.current().uuid.equals(msg.getFromUuid())) {
                    if (handleAccuse((CardWrapper) msg.getMessageData())) {
                        // Win!
                        // End the game, alert everybody
                        board.gameStarted = false;
                        Message endMessage = Message.winMessage(players.current().getSuspect());
                        endMessage.setBroadcast(true);
                        return endMessage;
                    } else {
                        // Bad accusation!
                        // Don't allow the player to continue making moves, but they must remain to
                        // disprove suggestions
                        players.current().setPlaying(false);
                        players.setNextPlayer();
                        logger.info("Bad accusation!");
                        return Message.failedMove(
                                "Bad accusation!  You must sit out the rest of the game.");
                    }
                }
                logger.info("Non-active player tried to accuse!");
                return Message.failedMove("Non-active player tried to accuse!");
            case MESSAGE_CLIENT_END_TURN:
                players.setNextPlayer();
                break;
            case MESSAGE_PULSE:
                // return echo request
                player = players.byUuid(msg.getFromUuid());
                return Message.sendGameStatePulse(new GameStatePulse(board, players, player));
            case MESSAGE_CLIENT_RESPONSE_SUGGEST:
                CardWrapper cards = (CardWrapper) msg.getMessageData();
                if (cards.getCards().size() == 1) {
                    // The disproving player was able to disprove
                    // Let the suggesting player know
                    Message response = Message.serverRespondSuggestion(cards);
                    response.setBroadcast(true);
                    response.setToUuid(players.getSuggestionPlayer().uuid.toString());
                    return response;
                } else if (cards.getCards().size() == 0) {
                    Player next = players.getNextDisprovePlayer();
                    if (next != null) {
                        // try the next player
                        Message response = Message.relaySuggestion(cardsToDisprove, next);
                        response.setBroadcast(true);
                        return response;
                    } else {
                        // No more players left to disprove
                        // Let the suggesting player know
                        Message response =
                                Message.serverRespondSuggestion(
                                        new CardWrapper(new ArrayList<CardsEnum>()));
                        response.setBroadcast(true);
                        response.setToUuid(players.getSuggestionPlayer().uuid.toString());
                        return response;
                    }
                } else {
                    // Something bad happened
                    logger.error("error with suggestion");
                }
            default:
                break;
        }

        return null;
    }

    private void startGame() {
        // Don't start again
        if (board.gameStarted) {
            return;
        }
        if (players.count() >= 3) {
            board.cards.setupCardDeckAndDealCards(players.getArray(), classicMode);
            board.gameStarted = true;
            // Shuffle players
            // TODO: This has no effect on ListItem list order.
            // Collections.shuffle(players, Helper.GetRandom());
            // TODO: Make scarlet the activePlayerRef
            // setNextPlayer();

        } else {
            logger.info("Not enough players to start the game");
        }
    }

    private boolean handleAccuse(CardWrapper cards) {
        return board.cards.envelopeMatch(cards);
    }
}
