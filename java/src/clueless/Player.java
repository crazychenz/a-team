package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

public class Player {

    private static final Logger logger
            = LogManager.getLogger(Player.class);

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
