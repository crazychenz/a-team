package clueless;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * For managing the addition of players and selection of players for actions
 *
 * @author ateam
 */
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

    /** Default Constructor */
    public PlayerMgr() {
        activePlayerList = null;
        activePlayerRef = null;
        suggestionPlayerRef = null;
        disprovingPlayerRef = null;
        activePlayerArray = new ArrayList<>();
    }

    /**
     * Returns the number of currently tracked players.
     *
     * @return Returns the number of currently tracked players.
     */
    public int count() {
        return activePlayerArray.size();
    }

    /**
     * Return a Player object with a given String form UUID
     *
     * @param uuid The string form of the UUID of the player
     * @return Player object
     */
    public Player byUuid(String uuid) {
        for (Player player : getArray()) {
            if (player.uuid.equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Fetch the Player object for who's turn it is.
     *
     * @return Player object
     */
    public Player current() {
        return activePlayerRef;
    }

    /**
     * Get the array object of all tracked Player objects
     *
     * @return ArrayList of Player objects
     */
    public ArrayList<Player> getArray() {
        return activePlayerArray;
    }

    /**
     * Create a Player to be tracked.
     *
     * @param suspect The SuspectCard that this player will represent.
     * @param fromUuid The string UUID of the client representing this Player.
     */
    public void add(SuspectCard suspect, String fromUuid, String username) {
        logger.info("Adding new player");

        // Create the Player and add to the ArrayList.
        Player newPlayer = new Player(suspect, fromUuid, username);
        activePlayerArray.add(newPlayer);

        // Sort the players according to the SuspectCard id value.
        Collections.sort(activePlayerArray);

        // (Lazily) Rebuild the (internal) linked list each time.
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

    /** Setup a new (internal) iterator for tracking Suggestion sequence */
    public void setSuggestionPlayer() {
        suggestionPlayerRef = activePlayerRef;
        disprovingPlayerRef = suggestionPlayerRef.getNext();
    }

    /**
     * Fetch the current Suggestion sequence iteration.
     *
     * @return Current Player to disprove a suggestion.
     */
    public Player getSuggestionPlayer() {
        return suggestionPlayerRef;
    }

    /**
     * Advance the Suggestion sequence iteration.
     *
     * @return The next player to attempt to disprove a suggestion.
     */
    public Player getNextDisprovePlayer() {
        Player next = disprovingPlayerRef;
        if (next.equals(suggestionPlayerRef)) {
            logger.info("No more players to disprove");
            return null;
        }
        disprovingPlayerRef = disprovingPlayerRef.getNext();
        return next;
    }

    /**
     * Advancement of the Player turn sequence.
     *
     * @return True if another active player exists. False if there are no more active players.
     */
    public boolean setNextPlayer() {
        Player next = activePlayerRef.getNext();
        while (!next.isPlaying()) {
            if (next.equals(activePlayerRef)) {
                return false;
            }
            next = next.getNext();
        }
        activePlayerRef = next;
        logger.info("Next player is " + activePlayerRef.getSuspect());
        return true;
    }
}
