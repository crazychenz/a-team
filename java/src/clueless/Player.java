package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {

    private static final Logger logger = LogManager.getLogger(Player.class);

    private CardsEnum suspect;
    private String username;
    private ArrayList<Card> cards;
    String uuid;

    public Player(CardsEnum suspect, String uuid) {
        this.suspect = suspect;
        cards = new ArrayList<Card>();
        this.uuid = uuid;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public CardsEnum getSuspect() {
        return suspect;
    }
}
