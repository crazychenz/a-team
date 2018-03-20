package clueless;

import java.util.HashMap;

// This file is meant to represent the cards from the board game and nothing else

public enum CardsEnum {
    LOCATION_KITCHEN(0xA1, "Kitchen", CardType.CARD_TYPE_LOCATION),
    LOCATION_BALLROOM(0xA2, "Ballroom", CardType.CARD_TYPE_LOCATION),
    LOCATION_CONSERVATORY(0xA3, "Conservatory", CardType.CARD_TYPE_LOCATION),
    LOCATION_LIBRARY(0xA4, "Library", CardType.CARD_TYPE_LOCATION),
    LOCATION_HALL(0xA5, "Hall", CardType.CARD_TYPE_LOCATION),
    LOCATION_STUDY(0xA6, "Study", CardType.CARD_TYPE_LOCATION),
    LOCATION_DININGROOM(0xA7, "Dining Room", CardType.CARD_TYPE_LOCATION),
    LOCATION_LOUNGE(0xA8, "Lounge", CardType.CARD_TYPE_LOCATION),
    LOCATION_BILLIARDROOM(0xA9, "Billiard Room", CardType.CARD_TYPE_LOCATION),

    HALLWAY_STUDY_HALL(0xB1, "Study - Hall", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_STUDY_LIBRARY(0xB2, "Study - Library", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_HALL_LOUNGE(0xB3, "Hall - Lounge", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_HALL_BILLIARD(0xB4, "Hall - Billiard Room", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_LOUNGE_DINING(0xB5, "Lounge - Dining Room", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_DINING_BILLIARD(0xB6, "Dining Room - Billiard Room", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_DINING_KITCHEN(0xB7, "Dining Room - Kitchen", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_KITCHEN_BALL(0xB8, "Kitchen - Ballroom", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_BALL_BILLIARD(0xB9, "Ballroom - Billiard Room", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_BALL_CONSERVATORY(0xBA, "Ballroom - Conservatory", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_CONSERVATORY_LIBRARY(0xBB, "Conservatory - Library", CardType.CARD_TYPE_HALLWAY),
    HALLWAY_LIBRARY_BILLIARD(0xBC, "Library - Billiard Room", CardType.CARD_TYPE_HALLWAY),

    WEAPON_LEADPIPE(0x21, "Lead Pipe", CardType.CARD_TYPE_WEAPON),
    WEAPON_REVOLVER(0x22, "Revolver", CardType.CARD_TYPE_WEAPON),
    WEAPON_ROPE(0x23, "Rope", CardType.CARD_TYPE_WEAPON),
    WEAPON_DAGGER(0x24, "Dagger", CardType.CARD_TYPE_WEAPON),
    WEAPON_WRENCH(0x25, "Wrench", CardType.CARD_TYPE_WEAPON),
    WEAPON_CANDLESTICK(0x26, "Candlestick", CardType.CARD_TYPE_WEAPON),

    SUSPECT_PLUM(0x31, "Plum", CardType.CARD_TYPE_SUSPECT),
    SUSPECT_PEACOCK(0x32, "Peacock", CardType.CARD_TYPE_SUSPECT),
    SUSPECT_WHITE(0x33, "White", CardType.CARD_TYPE_SUSPECT),
    SUSPECT_SCARLET(0x34, "Scarlet", CardType.CARD_TYPE_SUSPECT),
    SUSPECT_GREEN(0x35, "Green", CardType.CARD_TYPE_SUSPECT),
    SUSPECT_MUSTARD(0x36, "Mustard", CardType.CARD_TYPE_SUSPECT);

    private final int uid;
    private final String label;
    private final CardType cardType;

    private static final HashMap<Integer, CardsEnum> uidMap = new HashMap<>();

    static {
        for (CardsEnum cardsEnum : CardsEnum.values()) {
            uidMap.put(cardsEnum.getUid(), cardsEnum);
        }
    }

    public static CardsEnum getByUid(Integer uid) {
        return uidMap.get(uid);
    }

    public static boolean isLocation(CardsEnum val) {
        int high = val.getUid() & 0xF0;
        return (high == 0xB0 || high == 0xA0);
    }

    CardsEnum(int uid, String label, CardType cardType) {
        this.uid = uid;
        this.label = label;
        this.cardType = cardType;
    }

    public int getUid() {
        return uid;
    }

    public String getLabel() {
        return label;
    }

    public CardType getCardType() {
        return cardType;
    }
}
