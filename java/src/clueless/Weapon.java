/** */
package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Weapon implements Serializable {

    private static final Logger logger = LogManager.getLogger(Weapon.class);

    private CardsEnum weapon;
    private CardsEnum current_location;

    public Weapon(CardsEnum weapon) {
        this.setWeapon(weapon);
        logger.debug("Creating weapon " + weapon.toString());
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

    /**
     * @param new_location the new location to set
     * @param board the game board to set
     */
    public void moveForSuggestion(GameBoard board, CardsEnum new_location) {
        Location location = board.getLocationByEnum(current_location);
        if (location != null) {
            location.remove_weapon(this);
        }
        Location newLocation = board.getLocationByEnum(new_location);
        newLocation.place_weapon(this);
        this.current_location = new_location;
    }
}
