/** */
package clueless;

import java.io.Serializable;
import java.util.ArrayList;

/** @author tombo */
public class CardWrapper implements Serializable {

    /** */
    private ArrayList<CardsEnum> cards;

    private static final long serialVersionUID = -6939758918875965835L;

    public CardWrapper(ArrayList<CardsEnum> cards) {
        setCards(cards);
    }

    /** @return the cards */
    public ArrayList<CardsEnum> getCards() {
        return cards;
    }

    /** @param cards the cards to set */
    public void setCards(ArrayList<CardsEnum> cards) {
        this.cards = cards;
    }
}
