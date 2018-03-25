/** */
package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Weapon implements Serializable {

    private static final Logger logger = LogManager.getLogger(Weapon.class);

    private WeaponCard weapon;
    private Room current_location;

    public Weapon(WeaponCard weapon) {
        this.setWeapon(weapon);
        logger.debug("Creating weapon " + weapon.toString());
    }

    /** @return the weapon */
    public WeaponCard getWeapon() {
        return weapon;
    }

    /** @param weapon the weapon to set */
    public final void setWeapon(WeaponCard weapon) {
        this.weapon = weapon;
    }

    /** @return the current_location */
    public Room getCurrent_location() {
        return current_location;
    }

    public void moveForSuggestion(GameBoard board, RoomCard dest) {

        if (current_location != null) {
            // TODO: Make weapons always exist in a room.
            current_location.removeWeapon(this);
        }

        Room room = Room.getById(dest.getId());
        room.placeWeapon(this);
        this.current_location = room;
    }
}
