/** */
package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Suspect implements Serializable {

    private static final Logger logger = LogManager.getLogger(Suspect.class);

    private CardsEnum suspect;
    private CardsEnum startLocation;
    private CardsEnum currentLocation;

    private boolean active;
    private int userID;

    private static HashMap<CardsEnum, Suspect> enumMap = new HashMap<>();

    static {
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_SUSPECT) {
                Suspect newSuspect;
                newSuspect = new Suspect(value, getStartLocation(value));
                enumMap.put(value, newSuspect);
            }
        }
    }

    public static Suspect getByEnum(CardsEnum value) {
        return enumMap.get(value);
    }

    private static CardsEnum getStartLocation(CardsEnum suspect) {
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

    public static ArrayList<Suspect> getCollection() {
        return new ArrayList<Suspect>(enumMap.values());
    }

    public static AvailableSuspects getAvailableSuspects() {
        AvailableSuspects availableSuspects = new AvailableSuspects();
        for (Suspect suspect : Suspect.getCollection()) {
            if (!suspect.getActive()) {
                availableSuspects.list.add(suspect.getSuspect());
            }
        }
        return availableSuspects;
    }

    public Suspect(CardsEnum suspect, CardsEnum start_location) {
        this.suspect = suspect;
        this.setStart_location(start_location);
        this.setCurrent_location(start_location);

        Location location = Location.getByEnum(start_location);
        location.place_suspect(this);
        logger.debug(
                "Creating suspect "
                        + suspect.toString()
                        + " in location "
                        + start_location.toString());
    }

    public boolean move(DirectionsEnum direction) {
        Location location = Location.getByEnum(currentLocation);
        if (location.validMove(direction)) {
            location.remove_suspect(this);
            Location newLocation = location.getAdjacentRoomInDirection(direction);
            newLocation.place_suspect(this);
            setCurrent_location(newLocation.getLocation());
            return true;
        } else {
            return false;
        }
    }

    public CardsEnum getSuspect() {
        return suspect;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /** @return the start_location */
    public CardsEnum getStart_location() {
        return startLocation;
    }

    /** @param start_location the start_location to set */
    private void setStart_location(CardsEnum start_location) {
        this.startLocation = start_location;
    }

    /** @return the current_location */
    public CardsEnum getCurrent_location() {
        return currentLocation;
    }

    /** @param current_location the current_location to set */
    private void setCurrent_location(CardsEnum current_location) {
        this.currentLocation = current_location;
    }

    /*private void writeObject(final ObjectOutputStream out) throws IOException {
    	out.writeInt(suspect.getUid());
    	out.writeInt(currentLocation.getUid());
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    	suspect = CardsEnum.getByUid(in.readInt());
    	currentLocation = CardsEnum.getByUid(in.readInt());
    }

    private void readObjectNoData() throws ObjectStreamException {
    	throw new InvalidObjectException("Stream data required.");
    }*/
}
