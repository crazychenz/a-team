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

    private static final Logger logger =
        LogManager.getLogger(Suspect.class);

	private CardsEnum suspect;
	private CardsEnum start_location;
	private CardsEnum current_location;
	private boolean active;
	private int userID;
	
	public Suspect(CardsEnum suspect, CardsEnum start_location) {
		this.suspect = suspect;
		this.start_location = start_location;
		logger.debug("Creating suspect " + suspect.toString() + " in location " + start_location.toString());
	}
	
	public void move(CardsEnum location) {
		this.current_location = location;
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
}
