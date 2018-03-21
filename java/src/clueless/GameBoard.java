package clueless;

import java.util.ArrayList;

public class GameBoard {

    public final CardDeck cards;
    public SuspectMap suspects;
    public WeaponMap weapons;
    public LocationMap locations;

    public boolean gameStarted = false;

    public GameBoard() {

        // Locations must be created first so we have places to put things.
        locations = new LocationMap();

        cards = new CardDeck();
        suspects = new SuspectMap(this);
        weapons = new WeaponMap();

        // TODO: Move to cardDeck
        createCards();
    }

    public Suspect getSuspectByEnum(CardsEnum value) {
        return suspects.getByEnum(value);
    }

    public ArrayList<Suspect> getAllSuspects() {
        return suspects.getAllSuspects();
    }

    public AvailableSuspects getAvailableSuspects() {
        return suspects.getAvailableSuspects();
    }

    public Weapon getWeaponByEnum(CardsEnum value) {
        return weapons.getByEnum(value);
    }

    public ArrayList<Weapon> getAllWeapons() {
        return weapons.getAllWeapons();
    }

    public Location getLocationByEnum(CardsEnum value) {
        return locations.getByEnum(value);
    }

    private void createCards() {
        // Create the cards
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() != CardType.CARD_TYPE_HALLWAY) {
                cards.add(new Card(value));
            }
        }
    }
}
