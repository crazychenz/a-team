package clueless;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Helper {

    private static final Logger logger = LogManager.getLogger(Helper.class);

    public static CardsEnum GetStartingLocationOfSuspect(CardsEnum suspect) {
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

    public static int GetNumberOfFaceUpCardsForNumberOfUsers(int numUsers, boolean classicClue) {
        if (!classicClue) {
            switch (numUsers) {
                case 3:
                case 4:
                    return 6;
                case 5:
                    return 3;
                case 6:
                    return 6;
                default:
                    return 6;
            }
        }
        if (classicClue) {
            switch (numUsers) {
                case 3:
                    return 0;
                case 4:
                    return 2;
                case 5:
                    return 3;
                case 6:
                    return 0;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public static Random GetRandom() {
        return new Random();
    }
}
