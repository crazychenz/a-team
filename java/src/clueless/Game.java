package clueless;

import clueless.io.Message;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private GameBoard board;
    private boolean gameStarted;
    public boolean classicMode = false;
    public PlayerMgr players;

    // TODO: Seems out if scope of Game
    private CardWrapper cardsToDisprove;

    public Game() {
        players = new PlayerMgr();
        board = new GameBoard();
        cardsToDisprove = new CardWrapper(new ArrayList<CardsEnum>());
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
                if (players.current().uuid.equals(msg.getFromUuid())) {
                    players.setSuggestionPlayer();
                    // TODO Move the suspect and weapon into the suggestion room
                    cardsToDisprove = (CardWrapper) msg.getMessageData();
                    CardsEnum suggestion_suspect = null;
                    CardsEnum suggestion_location = null;
                    CardsEnum suggestion_weapon = null;
                    for (CardsEnum card : cardsToDisprove.getCards()) {
                        if (card.getCardType() == CardType.CARD_TYPE_SUSPECT) {
                            suggestion_suspect = card;
                        }
                        if (card.getCardType() == CardType.CARD_TYPE_LOCATION) {
                            suggestion_location = card;
                        }
                        if (card.getCardType() == CardType.CARD_TYPE_WEAPON) {
                            suggestion_weapon = card;
                        }
                    }

                    board.getSuspectByEnum(suggestion_suspect)
                            .moveForSuggestion(board, suggestion_location);
                    board.getWeaponByEnum(suggestion_weapon)
                            .moveForSuggestion(board, suggestion_location);

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
                        gameStarted = false;
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
                return Message.sendGameStatePulse(
                        new GameStatePulse(gameStarted, board, players, player));
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

    private boolean startGame() {
        if (!gameStarted) {
            if (players.count() < 3) {
                logger.info("Not enough players to start the game");
                return gameStarted;
            }

            board.dealCards(players);
            gameStarted = true;
            // TODO: Make scarlet the activePlayerRef
            // TODO: All users should be using peice nearest to them
            //       and all peices have a starting location
            //       and all turns go to the left starting with scarlet,
            //       therefore the turn ordering is always the same.
            // setNextPlayer();
        }
        return gameStarted;
    }

    // TODO: Seems out of scope
    private boolean handleAccuse(CardWrapper cards) {
        return board.accuse(cards);
    }
}
