/** */
package clueless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Weapon {

    private static final Logger logger = LogManager.getLogger(Weapon.class);

    private CardsEnum weapon;
    private CardsEnum current_location;

    public Weapon(CardsEnum weapon) {
        this.setWeapon(weapon);
        logger.debug("Creating weapon " + weapon.toString());
    }

    public void SetLocation(CardsEnum location) {
        setCurrent_location(location);
    }

    /** @return the weapon */
    public CardsEnum getWeapon() {
        return weapon;
    }

    /** @param weapon the weapon to set */
    public final void setWeapon(CardsEnum weapon) {
        this.weapon = weapon;
    }

    /** @return the current_location */
    public CardsEnum getCurrent_location() {
        return current_location;
    }

    /** @param current_location the current_location to set */
    public void setCurrent_location(CardsEnum current_location) {
        this.current_location = current_location;
    }
}
