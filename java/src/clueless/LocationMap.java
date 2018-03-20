package clueless;

import java.util.HashMap;

public class LocationMap {
    private HashMap<CardsEnum, Location> locations = new HashMap<>();

    public LocationMap() {
        // This provides a way to lookup Locations with enum value
        // and it preallocates all of our locations.
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_LOCATION
                    || value.getCardType() == CardType.CARD_TYPE_HALLWAY) {
                locations.put(value, new Location(value));
            }
        }

        // Link all the hallways to the locations
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_LOCATION) {
                addHallways(getByEnum(value));
            }
        }
    }

    public Location getByEnum(CardsEnum value) {
        return locations.get(value);
    }

    private void addHallways(Location location) {
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
}
