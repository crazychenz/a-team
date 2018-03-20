package clueless;

import java.util.ArrayList;
import java.util.HashMap;

public class SuspectMap {

    private final HashMap<CardsEnum, Suspect> suspects = new HashMap<>();
    private GameBoard board;

    public SuspectMap(GameBoard board) {
        this.board = board;
        createSuspects();
    }

    private void createSuspects() {
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_SUSPECT) {
                Suspect newSuspect;
                newSuspect = new Suspect(value);
                suspects.put(value, newSuspect);

                // Put new suspect into its location.
                CardsEnum start = SuspectMap.getStartLocation(value);
                Location location = board.getLocationByEnum(start);
                location.place_suspect(newSuspect);
            }
        }
    }

    public Suspect getByEnum(CardsEnum value) {
        return suspects.get(value);
    }

    public ArrayList<Suspect> getAllSuspects() {
        return new ArrayList<Suspect>(suspects.values());
    }

    public AvailableSuspects getAvailableSuspects() {
        AvailableSuspects availableSuspects = new AvailableSuspects();
        for (Suspect suspect : suspects.values()) {
            if (!suspect.getActive()) {
                availableSuspects.list.add(suspect.getSuspect());
            }
        }
        return availableSuspects;
    }

    public static CardsEnum getStartLocation(CardsEnum suspect) {
        switch (suspect) {
            case SUSPECT_PLUM:
                return CardsEnum.HALLWAY_STUDY_LIBRARY;
            case SUSPECT_PEACOCK:
                return CardsEnum.HALLWAY_CONSERVATORY_LIBRARY;
            case SUSPECT_GREEN:
                return CardsEnum.HALLWAY_BALL_CONSERVATORY;
            case SUSPECT_WHITE:
                return CardsEnum.HALLWAY_KITCHEN_BALL;
            case SUSPECT_MUSTARD:
                return CardsEnum.HALLWAY_LOUNGE_DINING;
            case SUSPECT_SCARLET:
                return CardsEnum.HALLWAY_HALL_LOUNGE;
            default:
                return CardsEnum.HALLWAY_STUDY_HALL;
        }
    }
}
