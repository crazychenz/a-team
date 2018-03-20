package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player extends ListItem {

    private static final Logger logger = LogManager.getLogger(Player.class);

    private CardsEnum suspect;
    private String username;
    private ArrayList<Card> cards;
    private boolean active;
    String uuid;

    public Player(CardsEnum suspect, String uuid) {
        super();
        setupPlayer(suspect, uuid);
    }

    private void setupPlayer(CardsEnum suspect, String uuid) {
        this.suspect = suspect;
        cards = new ArrayList<>();
        this.uuid = uuid;
        active = true;
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

    /** @return the active */
    public boolean isActive() {
        return active;
    }

    /** @param active the active to set */
    public void setActive(boolean active) {
        this.active = active;
    }
}
