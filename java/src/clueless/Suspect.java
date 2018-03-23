package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Nested Suspect Class
public class Suspect implements Serializable {

    private static final Logger logger = LogManager.getLogger(Suspect.class);

    private CardsEnum suspect;
    private CardsEnum startLocation;
    private CardsEnum currentLocation;

    private boolean active;

    // public static ArrayList<Suspect> getCollection() {
    //    return new ArrayList<Suspect>(enumMap.values());
    // }

    public Suspect(CardsEnum suspect) {
        this.suspect = suspect;
        CardsEnum start = SuspectMap.getStartLocation(suspect);
        this.setStart_location(start);
        this.setCurrent_location(start);

        logger.debug("Creating suspect " + suspect.toString() + " in location " + start.toString());
    }

    public boolean move(GameBoard board, DirectionsEnum direction) {
        Location location = board.getLocationByEnum(currentLocation);
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

    public void moveForSuggestion(GameBoard board, CardsEnum new_location) {
        Location location = board.getLocationByEnum(currentLocation);
        location.remove_suspect(this);
        Location newLocation = board.getLocationByEnum(new_location);
        newLocation.place_suspect(this);
        setCurrent_location(new_location);
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
