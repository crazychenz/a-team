/** */
package clueless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the Envelope of who, with what, and where Dr Black was done in.
 *
 * @author ateam
 */
public class Envelope {

    private static final Logger logger = LogManager.getLogger(Envelope.class);

    private SuspectCard suspect;
    private RoomCard room;
    private WeaponCard weapon;

    /**
     * Constructor
     *
     * @param suspect SuspectCard or who did it.
     * @param location RoomCard or where they did it.
     * @param weapon WeaponCard or weapon of murderer.
     */
    public Envelope(SuspectCard suspect, RoomCard location, WeaponCard weapon) {
        logger.info("ENVELOPE: " + suspect + location + weapon);
        this.suspect = suspect;
        this.room = location;
        this.weapon = weapon;
    }

    /**
     * Method to check if accusation is correct.
     *
     * @param cards The suggestion object comparing to compare with envelope.
     * @return true if correct, false if incorrect.
     */
    public boolean matchEnvelope(Suggestion cards) {
        return (cards.getRoom().equals(room)
                && cards.getSuspect().equals(suspect)
                && cards.getWeapon().equals(weapon));
    }
}
