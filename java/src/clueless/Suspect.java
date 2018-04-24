package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a Suspect piece on the GameBoard
 *
 * @author ateam
 */
public class Suspect implements Serializable {

    private static final Logger logger = LogManager.getLogger(Suspect.class);

    private SuspectCard suspect;
    private Location startLocation;
    private Location currentLocation;

    private boolean active;

    /**
     * Suspect Constructor
     *
     * @param suspect SuspectCard that represents the Suspect piece.
     */
    public Suspect(SuspectCard suspect) {
        this.suspect = suspect;
        Location start = SuspectMap.getStartLocation(suspect);
        this.setStart_location(start);
        this.setCurrent_location(start);

        logger.trace("Creating suspect " + suspect.toString() + " in location " + start.toString());
    }

    /**
     * Operation to move the Suspect piece to a Hallway or Room.
     *
     * @param direction The direction the piece should move.
     * @return Returns true if piece moved, false for invalid movement request.
     */
    public boolean move(DirectionsEnum direction) {

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

    /**
     * Move a Suspect piece due to a suggestion sequence.
     *
     * @param dest Room to move a Suspect piece to.
     */
    public void moveForSuggestion(RoomCard dest) {
        currentLocation.removeSuspect(this);
        Room room = Room.getById(dest.getId());
        room.placeSuspect(this);
        setCurrent_location(room);
    }

    /**
     * Get the SuspectCard that represents this Suspect piece.
     *
     * @return SuspectCard
     */
    public SuspectCard getSuspect() {
        return suspect;
    }

    /**
     * Get whether this Suspect is tracked by a Player.
     *
     * @return true is tracked by Player, otherwise false
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Set whether this Suspect is tracked by Player
     *
     * @param active true if tracked, false if untracked
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the start location of the player
     *
     * @return the start_location
     */
    public Location getStart_location() {
        return startLocation;
    }

    private void setStart_location(Location start_location) {
        this.startLocation = start_location;
    }

    /**
     * Get the current location of the player
     *
     * @return the current_location
     */
    public Location getCurrent_location() {
        return currentLocation;
    }

    private void setCurrent_location(Location current_location) {
        this.currentLocation = current_location;
    }
}
