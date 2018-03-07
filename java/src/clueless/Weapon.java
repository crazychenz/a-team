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
public class Weapon {

    private static final Logger logger =
        LogManager.getLogger(Weapon.class);

	private CardsEnum weapon;
	private CardsEnum current_location;
	
	public Weapon(CardsEnum weapon) {
		this.weapon = weapon;
		logger.debug("Creating weapon " + weapon.toString());
	}
	
	public void SetLocation(CardsEnum location) {
		current_location = location;
	}

}
