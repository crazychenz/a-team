package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Nested Suspect Class
public class Suspect implements Serializable {

    private static final Logger logger = LogManager.getLogger(Suspect.class);

    private SuspectCard suspect;
    private Location startLocation;
    private Location currentLocation;

    private boolean active;

    // public static ArrayList<Suspect> getCollection() {
    //    return new ArrayList<Suspect>(enumMap.values());
    // }

    public Suspect(SuspectCard suspect) {
        this.suspect = suspect;
        Location start = SuspectMap.getStartLocation(suspect);
        this.setStart_location(start);
        this.setCurrent_location(start);

        logger.debug("Creating suspect " + suspect.toString() + " in location " + start.toString());
    }

    public boolean move(GameBoard board, DirectionsEnum direction) {

        if (!currentLocation.validDirection(direction)) {
            return false;
        }

        Location adjacent = currentLocation.getAdjacentRoom(direction);

        if (!adjacent.available()) {
            return false;
        }

        currentLocation.removeSuspect(this);
        adjacent.placeSuspect(this);
        setCurrent_location(adjacent);
        return true;
    }

    public void moveForSuggestion(GameBoard board, RoomCard dest) {
        currentLocation.removeSuspect(this);
		Room room = Room.getById(dest.getId());
        room.placeSuspect(this);
        setCurrent_location(room);
    }

    public SuspectCard getSuspect() {
        return suspect;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /** @return the start_location */
    public Location getStart_location() {
        return startLocation;
    }

    /** @param start_location the start_location to set */
    private void setStart_location(Location start_location) {
        this.startLocation = start_location;
    }

    /** @return the current_location */
    public Location getCurrent_location() {
        return currentLocation;
    }

    /** @param current_location the current_location to set */
    private void setCurrent_location(Location current_location) {
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
