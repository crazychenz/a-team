/**
 * Note: The sole reason this class exists is to squelch an unchecked
 * warning when casting from Object to ArrayList<CardsEnum>.
 */
package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.Serializable;
import java.util.ArrayList;

public class AvailableSuspects implements Serializable {
    private static final Logger logger =
        LogManager.getLogger(AvailableSuspects.class);
        
    public ArrayList<CardsEnum> list;
    
    public AvailableSuspects() {
        list = new ArrayList<CardsEnum>();
    }
}