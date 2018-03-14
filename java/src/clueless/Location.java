package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Hashtable;

public class Location {

    private static final Logger logger =
        LogManager.getLogger(Location.class);

	private CardsEnum location;
	private ArrayList<CardsEnum> suspectsInside;
	private Hashtable<DirectionsEnum,Location> adjacentRooms;
	private ArrayList<CardsEnum> weaponsInside;

	
	public Location(CardsEnum location) {
		this.setLocation(location);
		adjacentRooms = new Hashtable<DirectionsEnum, Location>();
		suspectsInside = new ArrayList<CardsEnum>();
		weaponsInside = new ArrayList<CardsEnum>();
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
		if(adjacentRooms.get(direction) != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public Location getAdjacentRoomInDirection(DirectionsEnum direction) {
		if(validMove(direction)) 
		{
			return adjacentRooms.get(direction);
		}
		else {
			return null;
		}
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
		boolean east = false;
		toReturn += "This location: " + location.toString() + "\n\n";
		Location t = adjacentRooms.get(DirectionsEnum.DIRECTION_NORTH);
		if(t != null) {
			toReturn += "\t\t\tN:" + t.getLocation() + "\n";
		}
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_WEST);
		if(t != null) {
			toReturn += "\tW:" + t.getLocation();
			west = true;
		}
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_EAST);
		if(t != null) {
			east = true;
			if(west) {
				toReturn += "\tE: " + t.getLocation() + "\n";
			}
			else {
				toReturn += "\t\t\t\tE: " + t.getLocation() + "\n";
			}
		}
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_SOUTH);
		if(t != null) {
			if(west && !east) {
				toReturn += "\n\t\t\tS:" + t.getLocation() + "\n";
			}
			else {
				toReturn += "\t\t\tS:" + t.getLocation() + "\n";
			}
		}
		
		t = adjacentRooms.get(DirectionsEnum.DIRECTION_SECRET);
		if(t != null) {
			toReturn += "\nSC:" + t.getLocation() + "\n";
		}
		return toReturn;
	}
}
