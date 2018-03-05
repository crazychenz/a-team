package clueless;

import java.util.ArrayList;
import java.util.Hashtable;

public class Location {
	private CardsEnum location;
	private ArrayList<Suspect> suspectsInside;
	private Hashtable<DirectionsEnum,Location> adjacentRooms;
	private ArrayList<Weapon> weaponsInside;

	
	public Location(CardsEnum location) {
		this.location = location;
		adjacentRooms = new Hashtable<DirectionsEnum, Location>();
		System.out.println("Creating location " + location.toString());
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
}
