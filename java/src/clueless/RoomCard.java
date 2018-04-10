package clueless;

import java.util.ArrayList;

/**
 * Represents all possible RoomCards
 *
 * @author ateam
 */
public class RoomCard extends Card {

    /** Enumeration of all possible RoomCards */
    public static final RoomCard LOCATION_KITCHEN,
            LOCATION_BALLROOM,
            LOCATION_CONSERVATORY,
            LOCATION_LIBRARY,
            LOCATION_HALL,
            LOCATION_STUDY,
            LOCATION_DININGROOM,
            LOCATION_LOUNGE,
            LOCATION_BILLIARDROOM;

    public static final ArrayList<RoomCard> allCards = new ArrayList<>();

    static {
        allCards.add(LOCATION_KITCHEN = new RoomCard(0xA1, "Kitchen"));
        Card.register(LOCATION_KITCHEN);
        allCards.add(LOCATION_BALLROOM = new RoomCard(0xA2, "Ballroom"));
        Card.register(LOCATION_BALLROOM);
        allCards.add(LOCATION_CONSERVATORY = new RoomCard(0xA3, "Conservatory"));
        Card.register(LOCATION_CONSERVATORY);
        allCards.add(LOCATION_LIBRARY = new RoomCard(0xA4, "Library"));
        Card.register(LOCATION_LIBRARY);
        allCards.add(LOCATION_HALL = new RoomCard(0xA5, "Hall"));
        Card.register(LOCATION_HALL);
        allCards.add(LOCATION_STUDY = new RoomCard(0xA6, "Study"));
        Card.register(LOCATION_STUDY);
        allCards.add(LOCATION_DININGROOM = new RoomCard(0xA7, "DiningRoom"));
        Card.register(LOCATION_DININGROOM);
        allCards.add(LOCATION_LOUNGE = new RoomCard(0xA8, "Lounge"));
        Card.register(LOCATION_LOUNGE);
        allCards.add(LOCATION_BILLIARDROOM = new RoomCard(0xA9, "BilliardRoom"));
        Card.register(LOCATION_BILLIARDROOM);
    }

    private RoomCard(int id, String name) {
        super(id, name);
    }

    /**
     * Lookup a RoomCard by Id
     *
     * @param id The id of the RoomCard to lookup
     * @return Static allocation of the RoomCard that matches the Id
     */
    public static RoomCard fetch(int id) {
        Card card = Card.fetch(id);
        if (card instanceof RoomCard) {
            return (RoomCard) card;
        }
        return null;
    }
}
