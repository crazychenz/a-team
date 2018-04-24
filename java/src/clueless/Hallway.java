package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hallway extends Location {

    private static final Logger logger = LogManager.getLogger(Hallway.class);

    public static final ArrayList<Hallway> allHallways = new ArrayList<>();

    public static final Hallway HALLWAY_STUDY_HALL,
            HALLWAY_STUDY_LIBRARY,
            HALLWAY_HALL_LOUNGE,
            HALLWAY_HALL_BILLIARD,
            HALLWAY_LOUNGE_DINING,
            HALLWAY_DINING_BILLIARD,
            HALLWAY_DINING_KITCHEN,
            HALLWAY_KITCHEN_BALL,
            HALLWAY_BALL_BILLIARD,
            HALLWAY_BALL_CONSERVATORY,
            HALLWAY_CONSERVATORY_LIBRARY,
            HALLWAY_LIBRARY_BILLIARD,
            HALLWAY_SCARLET_START,
            HALLWAY_GREEN_START,
            HALLWAY_MUSTARD_START,
            HALLWAY_PLUM_START,
            HALLWAY_PEACOCK_START,
            HALLWAY_WHITE_START;

    static {
        allHallways.add(HALLWAY_STUDY_HALL = new Hallway(0xB1, "Study - Hall"));
        allHallways.add(HALLWAY_STUDY_LIBRARY = new Hallway(0xB2, "Study - Library"));
        allHallways.add(HALLWAY_HALL_LOUNGE = new Hallway(0xB3, "Hall - Lounge"));
        allHallways.add(HALLWAY_HALL_BILLIARD = new Hallway(0xB4, "Hall - Billiard Room"));
        allHallways.add(HALLWAY_LOUNGE_DINING = new Hallway(0xB5, "Lounge - Dining Room"));
        allHallways.add(HALLWAY_DINING_BILLIARD = new Hallway(0xB6, "Dining Room - Billiard Room"));
        allHallways.add(HALLWAY_DINING_KITCHEN = new Hallway(0xB7, "Dining Room - Kitchen"));
        allHallways.add(HALLWAY_KITCHEN_BALL = new Hallway(0xB8, "Kitchen - Ballroom"));
        allHallways.add(HALLWAY_BALL_BILLIARD = new Hallway(0xB9, "Ballroom - Billiard Room"));
        allHallways.add(HALLWAY_BALL_CONSERVATORY = new Hallway(0xBA, "Ballroom - Conservatory"));
        allHallways.add(HALLWAY_CONSERVATORY_LIBRARY = new Hallway(0xBB, "Conservatory - Library"));
        allHallways.add(HALLWAY_LIBRARY_BILLIARD = new Hallway(0xBC, "Library - Billiard Room"));
        HALLWAY_SCARLET_START = new Hallway(0xC0, "Scarlet Start");
        HALLWAY_GREEN_START = new Hallway(0xC1, "Green Start");
        HALLWAY_MUSTARD_START = new Hallway(0xC2, "Mustard Start");
        HALLWAY_PLUM_START = new Hallway(0xC3, "Plum Start");
        HALLWAY_PEACOCK_START = new Hallway(0xC4, "Peacock Start");
        HALLWAY_WHITE_START = new Hallway(0xC5, "White Start");
    }

    SuspectCard suspect;
    int id;
    String name;

    public Hallway(int id, String name) {
        super(id, name);
        logger.trace("Creating hallway " + name);
        suspect = null;
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean available() {
        return suspect == null;
    }

    public void placeSuspect(Suspect suspect) {
        this.suspect = suspect.getSuspect();
    }

    public void removeSuspect(Suspect suspect) {
        this.suspect = null;
    }

    public static boolean isHallwayId(int id) {
        for (Hallway hallway : allHallways) {
            if (hallway.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
