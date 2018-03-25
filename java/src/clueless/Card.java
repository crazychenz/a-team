/** */
package clueless;

import java.io.Serializable;

/** @author tombo */
public class Card implements Serializable {

    private final int id;
    private final String name;

    protected Card(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "{" + id + "}";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Card card) {
        return getId() == card.getId();
    }
}
