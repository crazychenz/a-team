package clueless;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Location {

    private static final Logger logger = LogManager.getLogger(Location.class);

    int id;
    String name;

    private final HashMap<DirectionsEnum, Location> adjacentRooms;

    public Location(int id, String name) {

        // this.setLocation(location);
        adjacentRooms = new HashMap<>();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setAdjacentRoom(DirectionsEnum direction, Location location) {
        adjacentRooms.put(direction, location);
    }

    public boolean validDirection(DirectionsEnum direction) {
        return adjacentRooms.containsKey(direction);
    }

    // abstract-ish call ... maybe belongs in an interface?
    public boolean available() {
        return false;
    }

    // abstract-ish call ... maybe belongs in an interface?
    public void placeSuspect(Suspect suspect) {
        return;
    }

    // abstract-ish call ... maybe belongs in an interface?
    public void removeSuspect(Suspect suspect) {
        return;
    }

    public Location getAdjacentRoom(DirectionsEnum direction) {
        if (!validDirection(direction)) {
            return null;
        }
        return adjacentRooms.get(direction);
    }

    public String toString() {
        String toReturn = "";
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
