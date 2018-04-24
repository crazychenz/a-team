package clueless;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a location on the GameBoard
 *
 * @author ateam
 */
public class Location {

    private static final Logger logger = LogManager.getLogger(Location.class);

    int id;
    String name;

    private final HashMap<DirectionsEnum, Location> adjacentRooms;

    /**
     * Constructor
     *
     * @param id Unique ID of the location
     * @param name String name of the Location
     */
    public Location(int id, String name) {

        // this.setLocation(location);
        adjacentRooms = new HashMap<>();
        this.id = id;
        this.name = name;
    }

    /**
     * Fetch unique id of Location
     *
     * @return unique id of Location
     */
    public int getId() {
        return id;
    }

    /**
     * Fetch the String name of location
     *
     * @return String name of location
     */
    public String getName() {
        return name;
    }

    /**
     * Set an adjacent link to this Location
     *
     * @param direction Direction of the link
     * @param location Location linked to this Location
     */
    public void setAdjacentRoom(DirectionsEnum direction, Location location) {
        adjacentRooms.put(direction, location);
    }

    /**
     * Check if a DirectionEnum has a valid link to this Location
     *
     * @param direction DirectionEnum to check validity of
     * @return true if link is valid, false is link is invalid
     */
    public boolean validDirection(DirectionsEnum direction) {
        return adjacentRooms.containsKey(direction);
    }

    /**
     * Check if location is available to place a piece.
     *
     * <p>This method should be treated as an abstract method that must be overridden by a
     * sub-class.
     *
     * <p>TODO: maybe belongs in an interface?
     *
     * @return true if available, false if unavailable.
     */
    public boolean available() {
        return false;
    }

    /**
     * Place a Suspect piece into this Location.
     *
     * <p>This method should be treated as an abstract method that must be overridden by a
     * sub-class.
     *
     * <p>TODO: maybe belongs in an interface?
     *
     * @param suspect Suspect to place
     */
    public void placeSuspect(Suspect suspect) {
        return;
    }

    /**
     * Remove a Suspect piece from this Location
     *
     * <p>This method should be treated as an abstract method that must be overridden by a
     * sub-class.
     *
     * <p>TODO: maybe belongs in an interface?
     *
     * @param suspect Suspect to remove
     */
    public void removeSuspect(Suspect suspect) {
        return;
    }

    /**
     * Fetch an adjacently linked Location
     *
     * @param direction DirectionEnum to fetch link of.
     * @return The Location linked in the DirectionEnum direction.
     */
    public Location getAdjacentRoom(DirectionsEnum direction) {
        if (!validDirection(direction)) {
            return null;
        }
        return adjacentRooms.get(direction);
    }

    @Override
    public String toString() {
        String toReturn = name;
        /* TODO: Disabled until we stabilize the refactor.
        boolean west = false;
        boolean east = false;
        toReturn += "This location: " + location.toString() + "\n\n";
        Location t = adjacentRooms.get(DirectionsEnum.DIRECTION_NORTH);
        if (t != null) {
            toReturn += "\t\t\tN:" + t.getLocation() + "\n";
        }
        t = adjacentRooms.get(DirectionsEnum.DIRECTION_WEST);
        if (t != null) {
            toReturn += "\tW:" + t.getLocation();
            west = true;
        }
        t = adjacentRooms.get(DirectionsEnum.DIRECTION_EAST);
        if (t != null) {
            east = true;
            if (west) {
                toReturn += "\tE: " + t.getLocation() + "\n";
            } else {
                toReturn += "\t\t\t\tE: " + t.getLocation() + "\n";
            }
        }
        t = adjacentRooms.get(DirectionsEnum.DIRECTION_SOUTH);
        if (t != null) {
            if (west && !east) {
                toReturn += "\n\t\t\tS:" + t.getLocation() + "\n";
            } else {
                toReturn += "\t\t\tS:" + t.getLocation() + "\n";
            }
        }

        t = adjacentRooms.get(DirectionsEnum.DIRECTION_SECRET);
        if (t != null) {
            toReturn += "\nSC:" + t.getLocation() + "\n";
        }*/
        return toReturn;
    }
}
