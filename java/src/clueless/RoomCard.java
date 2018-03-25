package clueless;

import java.util.ArrayList;

public class RoomCard extends Card {

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
        allCards.add(LOCATION_BALLROOM = new RoomCard(0xA2, "Ballroom"));
        allCards.add(LOCATION_CONSERVATORY = new RoomCard(0xA3, "Conservatory"));
        allCards.add(LOCATION_LIBRARY = new RoomCard(0xA4, "Library"));
        allCards.add(LOCATION_HALL = new RoomCard(0xA5, "Hall"));
        allCards.add(LOCATION_STUDY = new RoomCard(0xA6, "Study"));
        allCards.add(LOCATION_DININGROOM = new RoomCard(0xA7, "Dining Room"));
        allCards.add(LOCATION_LOUNGE = new RoomCard(0xA8, "Lounge"));
        allCards.add(LOCATION_BILLIARDROOM = new RoomCard(0xA9, "Billiard Room"));
    }

    private RoomCard(int id, String name) {
        super(id, name);
    }

    public static RoomCard getById(int id) {
        for (RoomCard card : allCards) {
            if (card.getId() == id) {
                return card;
            }
        }
        return null;
    }
}
