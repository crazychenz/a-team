/** */
package clueless;

import java.io.Serializable;

/**
 * Represents a Card object
 *
 * @author ateam
 */
public class Card implements Serializable {

    private final int id;
    private final String name;

    /**
     * Constructor
     *
     * @param id Unique ID of the Card
     * @param name String name of the Card
     */
    protected Card(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "{" + id + "}";
    }

    /**
     * Fetch the ID of the Card.
     *
     * @return id of the card
     */
    public int getId() {
        return id;
    }

    /**
     * Fetch the String name of the Card
     *
     * @return name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Checks for Card equality by comparing IDs
     *
     * @param card Card to check equality against
     * @return true if equal, false if different.
     * @todo This shouldn't be needed if we normalize objects on serialization
     */
    public boolean equals(Card card) {
        return getId() == card.getId();
    }
}
