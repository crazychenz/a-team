package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Room extends Location {

    private static final Logger logger = LogManager.getLogger(Room.class);

    public static final Room LOCATION_KITCHEN,
            LOCATION_BALLROOM,
            LOCATION_CONSERVATORY,
            LOCATION_LIBRARY,
            LOCATION_HALL,
            LOCATION_STUDY,
            LOCATION_DININGROOM,
            LOCATION_LOUNGE,
            LOCATION_BILLIARDROOM;

    public static final ArrayList<Room> allRooms = new ArrayList<>();

    static {
        allRooms.add(LOCATION_KITCHEN = new Room(0xA1, "Kitchen"));
        allRooms.add(LOCATION_BALLROOM = new Room(0xA2, "Ballroom"));
        allRooms.add(LOCATION_CONSERVATORY = new Room(0xA3, "Conservatory"));
        allRooms.add(LOCATION_LIBRARY = new Room(0xA4, "Library"));
        allRooms.add(LOCATION_HALL = new Room(0xA5, "Hall"));
        allRooms.add(LOCATION_STUDY = new Room(0xA6, "Study"));
        allRooms.add(LOCATION_DININGROOM = new Room(0xA7, "Dining Room"));
        allRooms.add(LOCATION_LOUNGE = new Room(0xA8, "Lounge"));
        allRooms.add(LOCATION_BILLIARDROOM = new Room(0xA9, "Billiard Room"));
    }

    private final ArrayList<WeaponCard> weaponsInside;
    private final ArrayList<SuspectCard> suspectsInside;

    public Room(int id, String name) {
        super(id, name);
        logger.trace("Creating room " + name);
        weaponsInside = new ArrayList<>();
        suspectsInside = new ArrayList<>();
    }

    public static Room getById(int id) {
        for (Room room : allRooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    @Override
    public boolean available() {
        return true;
    }

    public void placeSuspect(Suspect suspect) {
        suspectsInside.add(suspect.getSuspect());
    }

    public void placeWeapon(Weapon weapon) {
        weaponsInside.add(weapon.getWeapon());
    }

    public void removeSuspect(Suspect suspect) {
        suspectsInside.remove(suspect.getSuspect());
    }

    public void removeWeapon(Weapon weapon) {
        weaponsInside.remove(weapon.getWeapon());
    }
}
