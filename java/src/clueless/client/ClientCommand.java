package clueless.client;

import clueless.Card;
import clueless.DirectionsEnum;
import clueless.Hallway;
import clueless.RoomCard;
import clueless.Suggestion;
import clueless.SuspectCard;
import clueless.WeaponCard;
import clueless.client.cli.BoardBuilder;
import clueless.io.Message;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.DefaultParser;

public class ClientCommand {

    private static final Logger logger = LogManager.getLogger(ClientCommand.class);

    private static DefaultParser parser = new DefaultParser();;

    // TODO: These should be made immutable
    public static HashMap<String, DirectionsEnum> directionsStrToEnum;
    public static HashMap<String, Card> suspectStrToEnum;
    public static HashMap<String, Card> accuseStrToEnum;
    public static HashMap<String, Card> suggestStrToEnum;

    public static Message processCommand(ClientState clientState, String cmd) {
        // Client client = cli.client;
        Message retval = null;

        ParsedLine pl;
        // This doesn't handle apostrophies
        pl = parser.parse(cmd, 0);
        if (null == pl.word()) {
            return null;
        }

        switch (pl.word()) {
            case "help":
                if (clientState.getGameState() != null) {
                    return Message.info(
                            "\n"
                                    + "chat <message>\n"
                                    + "    Send a message to all players\n"
                                    + "config <"
                                    + clientState.getAvailableSuspects().toString()
                                    + "> <username>\n"
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
                                    + "note <card> <note>\n"
                                    + "    Make a note on the card, either as a result from a suggestion or from your great deductive skills!\n"
                                    + "note\n"
                                    + "    View notes on all cards!\n"
                                    + "exit|quit\n"
                                    + "    Exit clueless CLI\n");
                } else {
                    return Message.info("Connect to the server first!");
                }
            case "chat":
                try {
                    String chatMsg = cmd.split(" ", 2)[1];
                    return Message.chatMessage(chatMsg);
                } catch (Exception e) {
                    logger.error("failed chat");
                }
                break;
            case "done":
                try {
                    if (!clientState.isConfigured()) {
                        return Message.error("Must config first!");
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return Message.error("Must start first!");
                    }

                    if (!clientState.isMyTurn()) {
                        return Message.error("Must be the active player!");
                    }

                    return Message.endTurn();
                } catch (Exception e) {
                    logger.error("failed chat");
                }
                break;
            case "config":
                try {
                    if (clientState.isConfigured()) {
                        return Message.error("You've already selected a suspect!");
                    }

                    if (pl.words().size() == 3) {
                        if (suspectStrToEnum.get(pl.words().get(1)) == null) {
                            return Message.error(
                                    "Problem selecting that suspect." + "  Please try again!");
                        }

                        logger.info("Selected " + suspectStrToEnum.get(pl.words().get(1)));
                        String card = pl.words().get(1);
                        SuspectCard suspect = (SuspectCard) suspectStrToEnum.get(card);
                        retval = Message.clientConfig(suspect, pl.words().get(2));
                        // TODO: This is untrue until the Server acknowledges it.
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
                        return Message.error("Must config first!");
                    }

                    return Message.startGame();

                } catch (Exception e) {
                    logger.error("failed starting game");
                }
                break;
            case "disprove":
                try {
                    if (!clientState.isConfigured()) {
                        return Message.error("Must config first!");
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return Message.error("Must start first!");
                    }

                    if (!clientState.isDisproving()) {
                        return Message.error("Must be actively disproving!");
                    }

                    if (pl.words().size() == 2) {
                        HashMap<String, Card> disproveStrToEnum = new HashMap<String, Card>();

                        for (Card card : clientState.getDisproveCards()) {
                            disproveStrToEnum.put(card.getName(), card);
                        }

                        if (disproveStrToEnum.get(pl.words().get(1)) == null) {
                            return Message.error(
                                    "Problem selecting that card.  Please try again!"
                                            + disproveStrToEnum);
                        }

                        String card1 = pl.words().get(1);
                        logger.info("Disproving " + card1);
                        Card ce1 = disproveStrToEnum.get(card1);

                        retval = Message.clientRespondSuggestion(ce1);
                        clientState.setDisproving(false);
                    } else if (pl.words().size() == 1
                            && clientState.getDisproveCards().size() == 0) {
                        retval = Message.clientRespondSuggestion(null);
                    } else {
                        return Message.error("Must pick a card to disprove the suggestion!");
                    }
                } catch (Exception e) {
                    logger.error("failed making suggestion");
                }
                break;
            case "cards":
                if (!clientState.isConfigured()) {
                    return Message.error("Must config first!");
                } else if (!clientState.getGameState().isGameActive()) {
                    return Message.error("Must start first!");
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
                return Message.info(toReturn);
            case "note":
                if (!clientState.isConfigured()) {
                    return Message.error("Must config first!");
                } else if (!clientState.getGameState().isGameActive()) {
                    return Message.error("Must start first!");
                }

                if (pl.words().size() >= 3) {
                    // Reuse accuseStrToEnum since it has all the cards
                    if (accuseStrToEnum.get(pl.words().get(1)) == null) {
                        return Message.error(
                                "Problem selecting card \""
                                        + pl.words().get(1)
                                        + "\". Please try again!");
                    }

                    String card1 = pl.words().get(1);
                    Card noteCard = accuseStrToEnum.get(card1);
                    clientState.getNotebook().makeNote(noteCard, pl.line());
                } else if (pl.words().size() == 2) {
                    return Message.error("Please enter a note to make about the card!");
                } else if (pl.words().size() == 1) {
                    return Message.info(clientState.getNotebook().getNotes());
                } else {
                    return Message.error("Please enter a card to make notes about!");
                }
                break;
            case "board":
                if (!clientState.isConfigured()) {
                    return Message.error("Must config first!");
                } else if (!clientState.getGameState().isGameActive()) {
                    return Message.error("Must start first!");
                }
                BoardBuilder bb = new BoardBuilder(clientState);
                return Message.info(bb.generateBoard());
            case "accuse":
                try {
                    if (!clientState.isConfigured()) {
                        return Message.error("Must config first!");
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return Message.error("Must start first!");
                    }

                    if (!clientState.isMyTurn()) {
                        return Message.error("Must be the active player!");
                    }

                    if (pl.words().size() == 4) {

                        if (accuseStrToEnum.get(pl.words().get(1)) == null) {
                            return Message.error(
                                    "Problem selecting card \""
                                            + pl.words().get(1)
                                            + "\". Please try again!");
                        }
                        if (accuseStrToEnum.get(pl.words().get(2)) == null) {
                            return Message.error(
                                    "Problem selecting card \""
                                            + pl.words().get(2)
                                            + "\". Please try again!");
                        }
                        if (accuseStrToEnum.get(pl.words().get(3)) == null) {
                            return Message.error(
                                    "Problem selecting card \""
                                            + pl.words().get(3)
                                            + "\". Please try again!");
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
                            return Message.accusation(suggestion);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        return Message.error(
                                "Must specify a suspect, location, and weapon to accuse!");
                    }
                } catch (Exception e) {
                    logger.error("failed making accusation");
                }
                break;
            case "suggest":
                try {
                    if (!clientState.isConfigured()) {
                        return Message.error("Must config first!");
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return Message.error("Must start first!");
                    }

                    if (!clientState.isMyTurn()) {
                        return Message.error("Must be the active player!");
                    }

                    if (clientState.isSuggested()) {
                        return Message.error("Already made a suggestion this turn!");
                    }

                    if ((clientState.getMyLocation() != null)
                            && Hallway.isHallwayId(clientState.getMyLocation())) {
                        return Message.error("Cannot make a suggestion in a hallway!");
                    }

                    if ((clientState.getMyLocation() != null)
                            && (!clientState.isMovedBySuggestion() && !clientState.isMoved())) {
                        return Message.error(
                                "Must move to another room before making a suggestion!");
                    }

                    if (pl.words().size() == 3) {

                        if (suggestStrToEnum.get(pl.words().get(1)) == null) {
                            return Message.error("Problem selecting that card.  Please try again!");
                        }
                        if (suggestStrToEnum.get(pl.words().get(2)) == null) {
                            return Message.error("Problem selecting that card.  Please try again!");
                        }
                        String card1 = pl.words().get(1);
                        String card2 = pl.words().get(2);
                        logger.info("Suggesting " + card1 + " " + card2);
                        Card ce1 = suggestStrToEnum.get(card1);
                        Card ce2 = suggestStrToEnum.get(card2);
                        Card location = RoomCard.fetch(clientState.getMyLocation());
                        if (ce1 == null) {
                            logger.error("Missing ce1");
                        }
                        if (ce2 == null) {
                            logger.error("Missing ce2");
                        }
                        if (location == null) {
                            logger.error("Missing location");
                        }
                        Suggestion cards = new Suggestion(ce1, ce2, location);
                        retval = Message.suggestion(cards);
                        // TODO: Not true until confirmed by server
                        clientState.setSuggested(true);
                        clientState.setMovedBySuggestion(false);

                    } else {
                        return Message.error("Must specify a suspect and a weapon to suggest!");
                    }
                } catch (Exception e) {
                    logger.error("failed making suggestion");
                    e.printStackTrace();
                }
                break;
            case "move":
                try {
                    if (!clientState.isConfigured()) {
                        return Message.error("Must config first!");
                    }

                    if (!clientState.getGameState().isGameActive()) {
                        return Message.error("Must start first!");
                    }

                    if (!clientState.isMyTurn()) {
                        return Message.error("Must be the active player!");
                    }

                    if (clientState.isMoved()) {
                        return Message.error("Already moved this turn!");
                    }

                    if (pl.words().size() != 2) {
                        return Message.error(
                                "Must specify a direction to move in.  Please try again!");
                    }

                    if (directionsStrToEnum.get(pl.words().get(1)) == null) {
                        return Message.error(
                                "Problem moving in that direction.  Please try again!");
                    }

                    logger.info("Moving direction: " + directionsStrToEnum.get(pl.words().get(1)));

                    String dirStr = pl.words().get(1);
                    DirectionsEnum dir = directionsStrToEnum.get(dirStr);
                    retval = Message.moveClient(dir);
                    // TODO: Not true until confirmed by server
                    // If the server returns failed move, this gets reset.  Should be fine as is
                    clientState.setMoved(true);
                } catch (Exception e) {
                    logger.error("failed moving");
                }
                break;
            default:
                return Message.error("Please enter a valid command!");
        }
        return retval;
    }

    static {
        // Setup some static mappings
        suspectStrToEnum = new HashMap<>();
        buildCardsMap(suspectStrToEnum, true, false, false);

        accuseStrToEnum = new HashMap<>();
        buildCardsMap(accuseStrToEnum, true, true, true);

        suggestStrToEnum = new HashMap<>();
        buildCardsMap(suggestStrToEnum, true, false, true);

        directionsStrToEnum = new HashMap<>();
        buildDirectionMap(directionsStrToEnum);

        parser.setEofOnUnclosedQuote(true);
    }

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

    public static void buildDirectionMap(HashMap<String, DirectionsEnum> map) {
        map.put("north", DirectionsEnum.DIRECTION_NORTH);
        map.put("south", DirectionsEnum.DIRECTION_SOUTH);
        map.put("east", DirectionsEnum.DIRECTION_EAST);
        map.put("west", DirectionsEnum.DIRECTION_WEST);
        map.put("secret", DirectionsEnum.DIRECTION_SECRET);
    }
}
