/**
 * 
 */
package clueless;

import java.io.Serializable;

/**
 * @author tombo
 *
 */
public class Card implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4423696825620129194L;
	private CardsEnum card;
	
	public Card(CardsEnum card) {
		this.card = card;
	}
	
	public String toString() {
		return card.getLabel() + " " + card.getUid() + " " + card.name();
	}
	
	public CardType getCardType() {
		return card.getCardType();
	}

}
