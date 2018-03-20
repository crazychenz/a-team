package clueless;

public class GameState {

    public final CardDeck cards;
    public boolean gameStarted = false;

    public GameState() {
        cards = new CardDeck();
        createCards();
    }

    private void createCards() {
        // Create the cards
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() != CardType.CARD_TYPE_HALLWAY) {
                cards.add(new Card(value));
            }
        }
    }

    /*public CardsEnum getActiveSuspect() {
        if (players.current() == null) {
            return null;
        } else {
            return players.current().getSuspect();
        }
    }*/
}
