package clueless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private final CardDeck cards;
    private final LinkedList<Player> activePlayers;
    public boolean classicMode = false;
    private final HashMap<CardsEnum, Location> locations;
    private final HashMap<CardsEnum, Location> hallways;
    private final HashMap<CardsEnum, Weapon> weapons;
    private final HashMap<CardsEnum, Suspect> suspects;
    private boolean gameStarted = false;
    private Player activePlayer;

    public Game() {
        locations = new HashMap<>();
        weapons = new HashMap<>();
        suspects = new HashMap<>();
        hallways = new HashMap<>();
        activePlayers = new LinkedList<>();
        cards = new CardDeck();
        setupWeapons();
        setupLocations();
        setupSuspects();
        // HeartbeatThread gt = new HeartbeatThread(activePlayers,this);
        // gt.start();
    }

    public LinkedList<Player> getActivePlayers() {
        return activePlayers;
    }

    /*private void sendMessageToAllPlayers(Message message) {
        Iterator<Player> iter = activePlayers.iterator();
        while(iter.hasNext()) {
            iter.next().getThread().send(message);
        }
    }*/
    private void setupWeapons() {
        for (CardsEnum weapon : CardsEnum.values()) {
            if (weapon.getCardType() == CardType.CARD_TYPE_WEAPON) {
                Weapon t = new Weapon(weapon);
                weapons.put(weapon, t);
                cards.add(new Card(weapon));
            }
        }
    }

    private void setupLocations() {
        for (CardsEnum location : CardsEnum.values()) {
            if (location.getCardType() == CardType.CARD_TYPE_LOCATION) {
                Location t = new Location(location);
                locations.put(location, t);
                cards.add(new Card(location));
            }
        }

        for (CardsEnum hall : CardsEnum.values()) {
            if (hall.getCardType() == CardType.CARD_TYPE_HALLWAY) {
                Location t = new Location(hall);
                hallways.put(hall, t);
            }
        }

        locations
                .values()
                .forEach(
                        (location) -> {
                            addHallways(location);
                        });

        locations
                .values()
                .forEach(
                        (location) -> {
                            logger.info(location);
                        });

        hallways.values()
                .forEach(
                        (hall) -> {
                            logger.info(hall);
                        });
    }

    private HashMap<CardsEnum, CardsEnum> getWeaponLocations() {
        HashMap<CardsEnum, CardsEnum> weaponLocations = new HashMap<>();
        weapons.values()
                .forEach(
                        (weapon) -> {
                            weaponLocations.put(weapon.getWeapon(), weapon.getCurrent_location());
                        });
        return weaponLocations;
    }

    private void addHallways(Location location) {
        Location tempLocation;
        switch (location.getLocation()) {
            case LOCATION_STUDY:
                tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_HALL);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_LIBRARY);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_KITCHEN));

                break;
            case LOCATION_HALL:
                tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_HALL);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_LOUNGE);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);

                break;

            case LOCATION_LOUNGE:
                tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_LOUNGE);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_LOUNGE_DINING);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET,
                        locations.get(CardsEnum.LOCATION_CONSERVATORY));

                break;

            case LOCATION_DININGROOM:
                tempLocation = hallways.get(CardsEnum.HALLWAY_LOUNGE_DINING);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_KITCHEN);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);

                break;

            case LOCATION_KITCHEN:
                tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_KITCHEN);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_KITCHEN_BALL);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_STUDY));

                break;

            case LOCATION_BALLROOM:
                tempLocation = hallways.get(CardsEnum.HALLWAY_KITCHEN_BALL);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_CONSERVATORY);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);

                break;

            case LOCATION_CONSERVATORY:
                tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_CONSERVATORY);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_LOUNGE));

                break;

            case LOCATION_LIBRARY:
                tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_LIBRARY);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_LIBRARY_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);

                break;

            case LOCATION_BILLIARDROOM:
                tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);

                tempLocation = hallways.get(CardsEnum.HALLWAY_LIBRARY_BILLIARD);
                tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
                break;
            default:
                break;
        }
    }

    private void setupSuspects() {
        for (CardsEnum suspect : CardsEnum.values()) {
            if (suspect.getCardType() == CardType.CARD_TYPE_SUSPECT) {
                Suspect t =
                        new Suspect(
                                suspect,
                                hallways.get(Helper.GetStartingLocationOfSuspect(suspect)));
                suspects.put(suspect, t);
                cards.add(new Card(suspect));
            }
        }
    }

    public Message processMessage(Message msg) {
        logger.debug("Processing the message");

        switch (msg.getMessageID()) {
            case MESSAGE_CLIENT_CONNECTED:
                // Client just connected
                // Available suspects in pulse since it can change on the fly, shouldn't send it out
                // just once

                // return sendAvailableSuspects();
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
                for (Suspect s : suspects.values()) {
                    if (s.getSuspect() == pickedSuspect) {
                        if (s.getActive()) {
                            logger.info("Suspect already chosen!");
                            return failedConfig("Suspect already chosen!");
                        } else {
                            s.setActive(true);
                        }
                    }
                }
                addPlayer(pickedSuspect, msg.getFromUuid());
                // TODO: Develop acknowledgement
                break;
            case MESSAGE_CLIENT_MOVE:
                // move from active player
                if (activePlayer.uuid.equals(msg.getFromUuid())) {
                    Suspect suspect = suspects.get(activePlayer.getSuspect());
                    // valid move
                    if (suspect.move((DirectionsEnum) msg.getMessageData())) {
                        // the person can now accuse or end their turn
                    }
                    // invalid move
                    else {
                        logger.info("Unable to move there!");
                        return failedMove("Unable to move there!");
                    }
                }
                // move from non-active player
                else {
                    logger.info("Non-active player tried to move!");
                    return failedMove("Non-active player tried to move!");
                }
                break;
            case MESSAGE_CLIENT_SUGGEST:
                // handle the suggestion
                // TODO We need to relay this to each player in turn until they can disprove or not
                //The server can either broadcast this out to everyone, and they check if their uuid matches (add uuid to Message?)
                //Or the server has to send this out directly to each client in succession
                if (activePlayer.uuid.equals(msg.getFromUuid())) {
                    msg.setBroadcast(true);
                    return relaySuggestion((CardWrapper) msg.getMessageData());
                }
                break;
            case MESSAGE_CLIENT_ACCUSE:
                if (activePlayer.uuid.equals(msg.getFromUuid())) {
                    if (handleAccuse((CardWrapper) msg.getMessageData())) {
                        // Win!
                        // End the game, alert everybody
                        gameStarted = false;
                        Message endMessage = sendMessage();
                        endMessage.setBroadcast(true);
                        return endMessage;
                    } else {
                        // Bad accusation!
                        // Don't allow the player to continue making moves, but they must remain to
                        // disprove suggestions
                        activePlayer.setActive(false);
                        setNextPlayer();
                        logger.info("Bad accusation!");
                        return failedMove(
                                "Bad accusation!  You must sit out the rest of the game.");
                    }
                }
                logger.info("Non-active player tried to accuse!");
                return failedMove("Non-active player tried to accuse!");
            case MESSAGE_CLIENT_END_TURN:
                setNextPlayer();
                break;
            case MESSAGE_PULSE:
                // return echo request
                return sendGameStatePulse(msg.getFromUuid());
            default:
                break;
        }

        return null;
    }

    private HashMap<CardsEnum, CardsEnum> getSuspectLocations() {
        HashMap<CardsEnum, CardsEnum> suspectLocations = new HashMap<>();
        suspects.values()
                .forEach(
                        (suspect) -> {
                            suspectLocations.put(
                                    suspect.getSuspect(),
                                    suspect.getCurrent_location().getLocation());
                        });
        return suspectLocations;
    }

    private void startGame() {
        // Don't start again
        if (gameStarted) {
            return;
        }
        if (activePlayers.size() >= 3) {
            cards.setupCardDeckAndDealCards(activePlayers, classicMode);
            gameStarted = true;
            shufflePlayersAndSetActivePlayer();

        } else {
            logger.info("Not enough players to start the game");
        }
    }

    private boolean handleAccuse(CardWrapper cards) {
        return this.cards.envelopeMatch(cards);
    }

    private void shufflePlayersAndSetActivePlayer() {
        // randomly set activePlayer linked list and set activePlayer variable
        Collections.shuffle(activePlayers, Helper.GetRandom());
        setNextPlayer();
    }

    private void setNextPlayer() {
        activePlayer = activePlayers.element();
        activePlayers.add(activePlayers.pop());
    }

    private CardsEnum getActiveSuspect() {
        if (activePlayer == null) {
            return null;
        } else {
            return activePlayer.getSuspect();
        }
    }

    private AvailableSuspects getAvailableSuspects() {
        AvailableSuspects availableSuspects = new AvailableSuspects();
        for (Suspect suspect : suspects.values()) {
            if (!suspect.getActive()) {
                availableSuspects.list.add(suspect.getSuspect());
            }
        }
        return availableSuspects;
    }

    private Message failedConfig(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_CONFIG, message);
    }

    private Message failedMove(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_MOVE, message);
    }

    private Message relaySuggestion(CardWrapper cards) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RELAY_SUGGEST, cards);
    }

    private Message sendMessage() {
        return new Message(MessagesEnum.MESSAGE_CHAT_FROM_SERVER, "The game has been won!");
    }

    private void addPlayer(CardsEnum suspect, String fromUuid) {
        logger.info("Adding new player");
        activePlayers.add(new Player(suspect, fromUuid));
        startGame(); // For now, try to start the game after every user connects.  We need to let
        // the users pick when to start
    }

    private Message sendGameStatePulse(String uuid) {
        ArrayList<Card> individualCards;
        individualCards = new ArrayList<>();
        for (Player player : activePlayers) {
            if (player.uuid.equals(uuid)) {
                individualCards = player.getCards();
            }
        }

        GameStatePulse pulsePayload =
                new GameStatePulse(
                        gameStarted,
                        getActiveSuspect(),
                        getAvailableSuspects(),
                        getSuspectLocations(),
                        getWeaponLocations(),
                        individualCards,
                        cards.getFaceUpCards());

        return new Message(MessagesEnum.MESSAGE_PULSE, pulsePayload);
    }
}
