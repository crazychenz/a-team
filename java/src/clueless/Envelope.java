/** */
package clueless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Envelope {

    private static final Logger logger = LogManager.getLogger(Envelope.class);

    private SuspectCard suspect;
    private RoomCard room;
    private WeaponCard weapon;

    public Envelope(SuspectCard suspect, RoomCard location, WeaponCard weapon) {
        logger.debug("ENVELOPE: " + suspect + location + weapon);
        this.suspect = suspect;
        this.room = location;
        this.weapon = weapon;
    }

    public boolean matchEnvelope(Suggestion cards) {
        return (cards.getRoom().equals(room)
                && cards.getSuspect().equals(suspect)
                && cards.getWeapon().equals(weapon));
    }
}
