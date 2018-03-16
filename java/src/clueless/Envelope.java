/** */
package clueless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Envelope {

    private static final Logger logger = LogManager.getLogger(Envelope.class);

    private Card suspect;
    private Card location;
    private Card weapon;

    public Envelope(Card suspect, Card location, Card weapon) {
        this.suspect = suspect;
        this.location = location;
        this.weapon = weapon;
    }

    public Envelope() {}

    public void setSuspect(Card suspect) {
        this.suspect = suspect;
    }

    public void setLocation(Card location) {
        this.location = location;
    }

    public void setWeapon(Card weapon) {
        this.weapon = weapon;
    }
}
