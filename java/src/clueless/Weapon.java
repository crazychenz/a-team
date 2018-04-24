/** */
package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a Weapon piece.
 *
 * @author ateam
 */
public class Weapon implements Serializable {

    private static final Logger logger = LogManager.getLogger(Weapon.class);

    private WeaponCard weapon;
    private Room current_location;

    /**
     * Constructor for Weapon
     *
     * @param weapon WeaponCard that matches this Weapon piece
     */
    public Weapon(WeaponCard weapon) {
        this.setWeapon(weapon);
        logger.trace("Creating weapon " + weapon.toString());
    }

    /**
     * Fetch WeaponCard representing this Weapon type.
     *
     * @return WeaponCard
     */
    public WeaponCard getWeapon() {
        return weapon;
    }

    private final void setWeapon(WeaponCard weapon) {
        this.weapon = weapon;
    }

    /**
     * Fetch the current Room location of the Weapon piece.
     *
     * <p>TODO: Make sure Weapon currentLocation is never null.
     *
     * @return the current_location (possibly null)
     */
    public Room getCurrent_location() {
        return current_location;
    }

    /**
     * Move Weapon to a Room for a suggestion sequence.
     *
     * @param dest The Room to move the Weapon piece to.
     */
    public void moveForSuggestion(RoomCard dest) {

        if (current_location != null) {
            // TODO: Make weapons always exist in a room.
            current_location.removeWeapon(this);
        }

        Room room = Room.getById(dest.getId());
        room.placeWeapon(this);
        this.current_location = room;
    }
}
