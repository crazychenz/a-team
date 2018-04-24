package clueless;

import java.util.HashSet;

/**
 * Represents all tracked Locations on the GameBoard
 *
 * @author ateam
 */
public class LocationMap {
    private HashSet<Location> locations = new HashSet<>();

    /** Default Constructor */
    public LocationMap() {
        locations.addAll(Hallway.allHallways);
        locations.addAll(Room.allRooms);

        // This provides a way to lookup Locations with enum value
        // and it preallocates all of our locations.
        for (Room room : Room.allRooms) {
            addHallways(room);
        }

        // Connect start locations to adjacent hallways (one-way)
        addStartSpots();
    }

    private void addStartSpots() {
        Hallway start;
        Hallway hallway;

        start = Hallway.HALLWAY_SCARLET_START;
        hallway = Hallway.HALLWAY_HALL_LOUNGE;
        start.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

        start = Hallway.HALLWAY_MUSTARD_START;
        hallway = Hallway.HALLWAY_LOUNGE_DINING;
        start.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

        start = Hallway.HALLWAY_WHITE_START;
        hallway = Hallway.HALLWAY_KITCHEN_BALL;
        start.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

        start = Hallway.HALLWAY_GREEN_START;
        hallway = Hallway.HALLWAY_BALL_CONSERVATORY;
        start.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

        start = Hallway.HALLWAY_PEACOCK_START;
        hallway = Hallway.HALLWAY_CONSERVATORY_LIBRARY;
        start.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

        start = Hallway.HALLWAY_PLUM_START;
        hallway = Hallway.HALLWAY_STUDY_LIBRARY;
        start.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);
    }

    private void addHallways(Room location) {
        Hallway hallway;

        if (location == Room.LOCATION_STUDY) {

            hallway = Hallway.HALLWAY_STUDY_HALL;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

            hallway = Hallway.HALLWAY_STUDY_LIBRARY;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, Room.LOCATION_KITCHEN);

            return;
        }
        if (location == Room.LOCATION_HALL) {
            hallway = Hallway.HALLWAY_STUDY_HALL;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

            hallway = Hallway.HALLWAY_HALL_LOUNGE;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

            hallway = Hallway.HALLWAY_HALL_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

            return;
        }

        if (location == Room.LOCATION_LOUNGE) {
            hallway = Hallway.HALLWAY_HALL_LOUNGE;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

            hallway = Hallway.HALLWAY_LOUNGE_DINING;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, Room.LOCATION_CONSERVATORY);

            return;
        }

        if (location == Room.LOCATION_DININGROOM) {
            hallway = Hallway.HALLWAY_LOUNGE_DINING;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

            hallway = Hallway.HALLWAY_DINING_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

            hallway = Hallway.HALLWAY_DINING_KITCHEN;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

            return;
        }

        if (location == Room.LOCATION_KITCHEN) {

            hallway = Hallway.HALLWAY_DINING_KITCHEN;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

            hallway = Hallway.HALLWAY_KITCHEN_BALL;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, Room.LOCATION_STUDY);

            return;
        }

        if (location == Room.LOCATION_BALLROOM) {
            hallway = Hallway.HALLWAY_KITCHEN_BALL;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

            hallway = Hallway.HALLWAY_BALL_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

            hallway = Hallway.HALLWAY_BALL_CONSERVATORY;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);

            return;
        }

        if (location == Room.LOCATION_CONSERVATORY) {
            hallway = Hallway.HALLWAY_BALL_CONSERVATORY;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

            hallway = Hallway.HALLWAY_CONSERVATORY_LIBRARY;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, Room.LOCATION_LOUNGE);

            return;
        }

        if (location == Room.LOCATION_LIBRARY) {
            hallway = Hallway.HALLWAY_STUDY_LIBRARY;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

            hallway = Hallway.HALLWAY_LIBRARY_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

            hallway = Hallway.HALLWAY_CONSERVATORY_LIBRARY;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

            return;
        }

        if (location == Room.LOCATION_BILLIARDROOM) {

            hallway = Hallway.HALLWAY_HALL_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, hallway);

            hallway = Hallway.HALLWAY_DINING_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, hallway);

            hallway = Hallway.HALLWAY_BALL_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, hallway);

            hallway = Hallway.HALLWAY_LIBRARY_BILLIARD;
            hallway.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
            location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, hallway);
            return;
        }
    }
}
