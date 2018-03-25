package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player extends ListItem implements Comparable {

    private static final Logger logger = LogManager.getLogger(Player.class);

    private SuspectCard suspect;
    private String username;
    private ArrayList<Card> cards;
    private boolean
            playing; // This is meant to determine if this player is an active participant in the
    // game, or if they have made a bad accusation
    public String uuid;

    public Player(SuspectCard suspect, String uuid) {
        super();
        setupPlayer(suspect, uuid);
    }

    private void setupPlayer(SuspectCard suspect, String uuid) {
        this.suspect = suspect;
        cards = new ArrayList<>();
        this.uuid = uuid;
        playing = true;
    }

    @Override
    public Player getNext() {
        return (Player) super.getNext();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public SuspectCard getSuspect() {
        return suspect;
    }

    /** @return the playing */
    public boolean isPlaying() {
        return playing;
    }

    /** @param playing the playing to set */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getUuid() {
        return uuid;
    }

    // @Override
    // public int compareTo(Player other) {
    //    return suspect.getId() - other.suspect.getId();
    // }

    @Override
    public int compareTo(Object o) {
        return suspect.getId() - ((Player) o).suspect.getId();
    }
}
