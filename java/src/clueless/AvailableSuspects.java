/**
 * Note: The sole reason this class exists is to squelch an unchecked warning when casting from
 * Object to ArrayList<CardsEnum>.
 */
package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a list of all untracked Suspects.
 *
 * @author ateam
 */
public class AvailableSuspects implements Serializable {

    private static final Logger logger = LogManager.getLogger(AvailableSuspects.class);

    /** List of all untracked Suspects */
    public ArrayList<SuspectCard> list;

    /** Default Constructor */
    public AvailableSuspects() {
        list = new ArrayList<SuspectCard>();
    }

    public void normalize() {
        ArrayList<SuspectCard> _list = new ArrayList<SuspectCard>();
        for (SuspectCard card : list) {
            _list.add(SuspectCard.fetch(card.getId()));
        }
        list = _list;
    }

    @Override
    public String toString() {
        if (list.isEmpty()) {
            return "";
        }
        String toString = "";
        for (SuspectCard suspect : list) {
            toString += suspect.getName() + "|";
        }
        return toString.substring(0, toString.length() - 1);
    }
}
