package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * For managing the attributes of an active Player.
 *
 * @author ateam
 */
public class Player extends ListItem implements Comparable {

    private static final Logger logger = LogManager.getLogger(Player.class);

    private SuspectCard suspect;
    private String username;
    private ArrayList<Card> cards;
    public String uuid;
    private long lastPulseTime = 0;

    // Indicates whether a player can move or suggest (due to bad accusation).
    private boolean playing;

    /**
     * Creates a Player object.
     *
     * @param suspect - SuspectCard that Player represents.
     * @param uuid - String UUID of the Client playing Player
     */
    public Player(SuspectCard suspect, String uuid, String username) {
        super();
        setupPlayer(suspect, uuid, username);
    }

    private void setupPlayer(SuspectCard suspect, String uuid, String username) {
        this.suspect = suspect;
        cards = new ArrayList<>();
        this.uuid = uuid;
        setUsername(username);
        playing = true;
    }

    /**
     * Get the next Player's turn
     *
     * @return Next Player in (internal) linked list.
     */
    @Override
    public Player getNext() {
        return (Player) super.getNext();
    }

    /**
     * Get the previous Player's turn.
     *
     * @return Previous Player in (internal) linked list.
     */
    @Override
    public Player getPrev() {
        return (Player) super.getPrev();
    }

    /**
     * Get the cards dealt to the Player
     *
     * @return Array of Card objects dealt to Player
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Get the SuspectCard Player represents.
     *
     * @return SuspectCard Player represents.
     */
    public SuspectCard getSuspect() {
        return suspect;
    }

    /**
     * Fetch whether the Player is playing or not. A false accusation causes a player to sit out
     * 'playing' the game but must remain to perform suggestion sequences.
     *
     * @return boolean indicating whether Player can move or suggest
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets whether the Player can move or suggest.
     *
     * @param playing the playing to set
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Fetch the UUID of the client controlling Player.
     *
     * @return Returns player's UUID as a String
     */
    public String getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(Object o) {
        return suspect.getId() - ((Player) o).suspect.getId();
    }

    /** @return the lastPulseTime */
    public long getPulseTime() {
        return lastPulseTime;
    }

    /** */
    public void setPulseTime() {
        this.lastPulseTime = System.currentTimeMillis();
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @param username the username to set */
    public void setUsername(String username) {
        this.username = username;
    }
}
