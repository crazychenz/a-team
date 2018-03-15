/**
 *
 */
package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author tombo
 *
 */
public class Suspect {

	private static final Logger logger
			= LogManager.getLogger(Suspect.class);

	private CardsEnum suspect;
	private Location start_location;
	private Location current_location;
	private boolean active;
	private int userID;

	public Suspect(CardsEnum suspect, Location start_location) {
		this.suspect = suspect;
		this.setStart_location(start_location);
		this.setCurrent_location(start_location);
		start_location.place_suspect(this);
		logger.debug("Creating suspect " + suspect.toString() + " in location " + start_location.toString());
	}

	public boolean move(DirectionsEnum direction) {
		if (current_location.validMove(direction)) {
			current_location.remove_suspect(this);
			Location newLocation = current_location.getAdjacentRoomInDirection(direction);
			newLocation.place_suspect(this);
			setCurrent_location(newLocation);
			return true;
		} else {
			return false;
		}
	}

	public CardsEnum getSuspect() {
		return suspect;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the start_location
	 */
	public Location getStart_location() {
		return start_location;
	}

	/**
	 * @param start_location the start_location to set
	 */
	public void setStart_location(Location start_location) {
		this.start_location = start_location;
	}

	/**
	 * @return the current_location
	 */
	public Location getCurrent_location() {
		return current_location;
	}

	/**
	 * @param current_location the current_location to set
	 */
	public void setCurrent_location(Location current_location) {
		this.current_location = current_location;
	}
}
