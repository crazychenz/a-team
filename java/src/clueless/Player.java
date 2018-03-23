package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player extends ListItem {

    private static final Logger logger = LogManager.getLogger(Player.class);

    private CardsEnum suspect;
    private String username;
    private ArrayList<Card> cards;
    private boolean
            playing; // This is meant to determine if this player is an active participant in the
    // game, or if they have made a bad accusation
    public String uuid;

    public Player(CardsEnum suspect, String uuid) {
        super();
        setupPlayer(suspect, uuid);
    }

    private void setupPlayer(CardsEnum suspect, String uuid) {
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

    public CardsEnum getSuspect() {
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
}
