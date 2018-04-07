/** */
package clueless;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a Card object
 *
 * @author ateam
 */
public class Card implements Serializable {

    private final int id;
    private final String name;

    private static final HashMap<Integer, Card> deck = new HashMap<>();

    public static Card fetch(int id) {
        if (deck.containsKey(id)) {
            return deck.get(id);
        }
        // throw Exception("Card not found");
        return null;
    }

    protected static Card register(Card card) {
        if (!deck.containsKey(card.getId())) {
            deck.put(card.getId(), card);
        }
        return fetch(card.getId());
    }

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
     * <p>TODO: This shouldn't be needed if we normalize objects on serialization
     *
     * @param card Card to check equality against
     * @return true if equal, false if different.
     */
    public boolean equals(Card card) {
        return getId() == card.getId();
    }

    /*private void writeObject(ObjectOutputStream out) throws IOException {
    	out.writeObject(new Integer(getId()));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	Integer id = (Integer) in.readObject();

    }*/
}
