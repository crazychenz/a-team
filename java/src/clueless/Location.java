package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Location {

    private static final Logger logger = LogManager.getLogger(Location.class);

    private CardsEnum location;
    private final ArrayList<CardsEnum> suspectsInside;
    private final HashMap<DirectionsEnum, Location> adjacentRooms;
    private final ArrayList<CardsEnum> weaponsInside;

    private static HashMap<CardsEnum, Location> enumMap = new HashMap<>();

    static {
        // This provides a way to lookup Locations with enum value
        // and it preallocates all of our locations.
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_LOCATION
                    || value.getCardType() == CardType.CARD_TYPE_HALLWAY) {
                enumMap.put(value, new Location(value));
            }
        }

        // Link all the hallways to the locations
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_LOCATION) {
                addHallways(getByEnum(value));
            }
        }
    }

    public static Location getByEnum(CardsEnum value) {
        return enumMap.get(value);
    }

    private static void addHallways(Location location) {
        Location hallway;
        switch (location.getLocation()) {
            case LOCATION_STUDY:
                hallway = getByEnum(CardsEnum.HALLWAY_STUDY_HALL);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_STUDY_LIBRARY);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET, getByEnum(CardsEnum.LOCATION_KITCHEN));

                break;
            case LOCATION_HALL:
                hallway = getByEnum(CardsEnum.HALLWAY_STUDY_HALL);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_HALL_LOUNGE);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_HALL_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

                break;

            case LOCATION_LOUNGE:
                hallway = getByEnum(CardsEnum.HALLWAY_HALL_LOUNGE);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_LOUNGE_DINING);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET,
                        getByEnum(CardsEnum.LOCATION_CONSERVATORY));

                break;

            case LOCATION_DININGROOM:
                hallway = getByEnum(CardsEnum.HALLWAY_LOUNGE_DINING);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_DINING_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_DINING_KITCHEN);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

                break;

            case LOCATION_KITCHEN:
                hallway = getByEnum(CardsEnum.HALLWAY_DINING_KITCHEN);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_KITCHEN_BALL);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET, getByEnum(CardsEnum.LOCATION_STUDY));

                break;

            case LOCATION_BALLROOM:
                hallway = getByEnum(CardsEnum.HALLWAY_KITCHEN_BALL);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_BALL_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_BALL_CONSERVATORY);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

                break;

            case LOCATION_CONSERVATORY:
                hallway = getByEnum(CardsEnum.HALLWAY_BALL_CONSERVATORY);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

                location.setAdjacentRoom(
                        DirectionsEnum.DIRECTION_SECRET, getByEnum(CardsEnum.LOCATION_LOUNGE));

                break;

            case LOCATION_LIBRARY:
                hallway = getByEnum(CardsEnum.HALLWAY_STUDY_LIBRARY);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_LIBRARY_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

                break;

            case LOCATION_BILLIARDROOM:
                hallway = getByEnum(CardsEnum.HALLWAY_HALL_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_DINING_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_BALL_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

                hallway = getByEnum(CardsEnum.HALLWAY_LIBRARY_BILLIARD);
                hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
                location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);
                break;
            default:
                break;
        }
    }

    public Location(CardsEnum location) {
        this.setLocation(location);
        adjacentRooms = new HashMap<>();
        suspectsInside = new ArrayList<>();
        weaponsInside = new ArrayList<>();
        logger.debug("Creating location " + location.toString());
    }

    public void place_suspect(Suspect suspect) {
        suspectsInside.add(suspect.getSuspect());
    }

    public void place_weapon(Weapon weapon) {
        weaponsInside.add(weapon.getWeapon());
    }

    public void remove_suspect(Suspect suspect) {
        suspectsInside.remove(suspect.getSuspect());
    }

    public void remove_weapon(Weapon weapon) {
        weaponsInside.remove(weapon.getWeapon());
    }

    public void setAdjacentRoom(DirectionsEnum direction, Location location) {
        adjacentRooms.put(direction, location);
    }

    public boolean validMove(DirectionsEnum direction) {
        Location adjacent = adjacentRooms.get(direction);
        boolean toReturn = false;
        if (adjacent != null) {
            if (adjacent.getLocation().getCardType().equals(CardType.CARD_TYPE_HALLWAY)
                    && adjacent.numberOfSuspectsInside() == 0) {
                toReturn = true;
            }
            if (adjacent.getLocation().getCardType().equals(CardType.CARD_TYPE_LOCATION)) {
                toReturn = true;
            }
        }
        return toReturn;
    }

    public Location getAdjacentRoomInDirection(DirectionsEnum direction) {
        if (validMove(direction)) {
            return adjacentRooms.get(direction);
        } else {
            return null;
        }
    }

    /** @return the location */
    public CardsEnum getLocation() {
        return location;
    }

    /** @param location the location to set */
    public final void setLocation(CardsEnum location) {
        this.location = location;
    }

    public int numberOfSuspectsInside() {
        return suspectsInside.size();
    }

    public String toString() {
        String toReturn = "";
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
        }
        return toReturn;
    }
}
