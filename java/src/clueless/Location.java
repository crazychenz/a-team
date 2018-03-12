package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Hashtable;

public class Location {

    private static final Logger logger =
        LogManager.getLogger(Location.class);

	private CardsEnum location;
	private ArrayList<Suspect> suspectsInside;
	private Hashtable<DirectionsEnum,Location> adjacentRooms;
	private ArrayList<Weapon> weaponsInside;

	
	public Location(CardsEnum location) {
		this.setLocation(location);
		adjacentRooms = new Hashtable<DirectionsEnum, Location>();
		logger.debug("Creating location " + location.toString());
	}
	
	public void place_suspect(Suspect suspect) {
		
	}
	
	public void place_weapon(Weapon weapon) {
		weaponsInside.add(weapon);
		
	}
	
	public void remove_suspect(Suspect suspect) {
		
	}
	
	public void remove_weapon(Weapon weapon) {
		weaponsInside.remove(weapon);
	}
	
	public void setAdjacentRoom(DirectionsEnum direction, Location location) {
		adjacentRooms.put(direction, location);
	}

	/**
	 * @return the location
	 */
	public CardsEnum getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(CardsEnum location) {
		this.location = location;
	}
	
	public String toString() {
		String toReturn = "";
		boolean west = false;
		toReturn += "This location: " + location.toString() + "\n\n";
		Location t = adjacentRooms.get(DirectionsEnum.DIRECTION_NORTH);
		if(t != null) {
			toReturn += "\t\t\tN:" + t.getLocation() + "\n";
		}
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_WEST);
		if(t != null) {
			toReturn += "W:" + t.getLocation();
			west = true;
		}
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_EAST);
		if(t != null) {
			if(west) {
				toReturn += "\tE: " + t.getLocation() + "\n";
			}
			else {
				toReturn += "\t\t\t\tE: " + t.getLocation() + "\n";
			}
		}
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_SOUTH);
		if(t != null) {
			toReturn += "\t\t\tS:" + t.getLocation() + "\n";
		}
		
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_SECRET);
		if(t != null) {
			toReturn += "\nSC:" + t.getLocation() + "\n";
		}
		return toReturn;
	}
}
