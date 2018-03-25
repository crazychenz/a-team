/**
 * Note: The sole reason this class exists is to squelch an unchecked warning when casting from
 * Object to ArrayList<CardsEnum>.
 */
package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AvailableSuspects implements Serializable {

    private static final Logger logger = LogManager.getLogger(AvailableSuspects.class);

    public ArrayList<SuspectCard> list;

    public AvailableSuspects() {
        list = new ArrayList<SuspectCard>();
    }

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
