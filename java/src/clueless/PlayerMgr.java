package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerMgr {

    private static final Logger logger = LogManager.getLogger(PlayerMgr.class);

    // Note: This is an array of activePlayers;
    private ArrayList<Player> activePlayerArray;
    // Note: This is a ListItem linked list.
    private Player activePlayerList;
    // Note: Reference to current player's turn.
    private Player activePlayerRef;

    public PlayerMgr() {
        activePlayerList = null;
        activePlayerRef = null;
        activePlayerArray = new ArrayList<>();
    }

    public int count() {
        return activePlayerArray.size();
    }

    public Player byUuid(String uuid) {
        for (Player player : getArray()) {
            if (player.uuid.equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public Player current() {
        return activePlayerRef;
    }

    public ArrayList<Player> getArray() {
        return activePlayerArray;
    }

    public ArrayList<Player> getActivePlayers() {
        return activePlayerArray;
    }

    private Player getActivePlayer() {
        return activePlayerRef;
    }

    public void add(CardsEnum suspect, String fromUuid) {
        logger.info("Adding new player");

        Player newPlayer = new Player(suspect, fromUuid);
        activePlayerArray.add(newPlayer);

        if (activePlayerList == null) {
            activePlayerList = newPlayer;
        } else {
            Player.addItem(activePlayerList, newPlayer);
        }

        if (activePlayerRef == null) {
            activePlayerRef = newPlayer;
        }
    }

    public void setNextPlayer() {
        activePlayerRef = activePlayerRef.getNext();
        logger.info("Next player is " + activePlayerRef.getSuspect().getLabel());
    }
}
