/**
 * Note: The sole reason this class exists is to squelch an unchecked
 * warning when casting from Object to ArrayList<CardsEnum>.
 */
package clueless;

import java.io.Serializable;
import java.util.ArrayList;

public class AvailableSuspects implements Serializable {
    public ArrayList<CardsEnum> list;
    
    public AvailableSuspects() {
        list = new ArrayList<CardsEnum>();
    }
}