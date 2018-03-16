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
            if (numUsers == 3 || numUsers == 4) {
                return 6;
            } else if (numUsers == 5) {
                return 3;
            } else if (numUsers == 6) {
                return 6;
            } else {
                return 6;
            }
        }
        if (classicClue) {
            if (numUsers == 3) {
                return 0;
            } else if (numUsers == 4) {
                return 2;
            } else if (numUsers == 5) {
                return 3;
            } else if (numUsers == 6) {
                return 0;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public static Random GetRandom() {
        return new Random();
    }
}
