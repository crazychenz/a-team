package clueless;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration implements Serializable {

    private static final Logger logger = LogManager.getLogger(AvailableSuspects.class);

    private SuspectCard suspectCard;
    private String username;

    public Configuration(SuspectCard suspectCard, String username) {
        this.setSuspectCard(suspectCard);
        this.setUsername(username);
    }

    /** @return the suspectCard */
    public SuspectCard getSuspectCard() {
        return suspectCard;
    }

    /** @param suspectCard the suspectCard to set */
    public void setSuspectCard(SuspectCard suspectCard) {
        this.suspectCard = suspectCard;
    }

    /** @return the username */
    public String getUsername() {
        return username;
    }

    /** @param username the username to set */
    public void setUsername(String username) {
        this.username = username;
    }
}
