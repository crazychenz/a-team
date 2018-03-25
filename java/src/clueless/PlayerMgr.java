package clueless;

import java.util.ArrayList;
import java.util.Collections;
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

    private Player suggestionPlayerRef;
    private Player disprovingPlayerRef;

    public PlayerMgr() {
        activePlayerList = null;
        activePlayerRef = null;
        suggestionPlayerRef = null;
        disprovingPlayerRef = null;
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

    public void add(SuspectCard suspect, String fromUuid) {
        logger.info("Adding new player");

        Player newPlayer = new Player(suspect, fromUuid);
        activePlayerArray.add(newPlayer);

        Collections.sort(activePlayerArray);

        // lazily just rebuild the linked list each time
        activePlayerList = null;
        for (Player player : activePlayerArray) {
            player.init();
            if (activePlayerList == null) {
                activePlayerList = player;
            } else {
                Player.addToTail(player, activePlayerList);
            }
        }
        activePlayerRef = activePlayerList;
    }

    public void setSuggestionPlayer() {
        suggestionPlayerRef = activePlayerRef;
        disprovingPlayerRef = suggestionPlayerRef.getNext();
    }

    public Player getSuggestionPlayer() {
        return suggestionPlayerRef;
    }

    public Player getNextDisprovePlayer() {
        Player next = disprovingPlayerRef;
        if (next.equals(suggestionPlayerRef)) {
            logger.info("No more players to disprove");
            return null;
        }
        disprovingPlayerRef = disprovingPlayerRef.getNext();
        return next;
    }

    // This needs to check whether the player has been marked inactive, as in they made a bad
    // accusation
    public void setNextPlayer() {
        activePlayerRef = activePlayerRef.getNext();
        logger.info("Next player is " + activePlayerRef.getSuspect());
    }
}
