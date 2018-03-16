/** */
package clueless;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class CardDeck {

    private static final Logger logger = LogManager.getLogger(CardDeck.class);

    private final Envelope envelope;
    private final ArrayList<Card> faceUpCards;
    private final ArrayList<Card> allSuspectCards;
    private final ArrayList<Card> allLocationCards;
    private final ArrayList<Card> allWeaponCards;
    private final Random rngjesus;

    public CardDeck() {
        rngjesus = new Random(System.currentTimeMillis());
        allSuspectCards = new ArrayList<>();
        allLocationCards = new ArrayList<>();
        allWeaponCards = new ArrayList<>();
        faceUpCards = new ArrayList<>();
        envelope = new Envelope();
        logger.debug("Setting up CardDeck");
    }

    public void add(Card add) {
        logger.debug("Adding new card " + add.toString());
        if (null != add.getCardType()) {
            switch (add.getCardType()) {
                case CARD_TYPE_LOCATION:
                    allLocationCards.add(add);
                    break;
                case CARD_TYPE_SUSPECT:
                    allSuspectCards.add(add);
                    break;
                case CARD_TYPE_WEAPON:
                    allWeaponCards.add(add);
                    break;
                default:
                    break;
            }
        }
    }

    public void shuffleCards() {
        // shuffle all the cards
    }

    public void setupCardDeckAndDealCards(LinkedList<Player> players, boolean classicClue) {
        populateEnvelope();
        int numUsers = players.size();
        logger.debug("Dealing to " + numUsers + " players.");
        int numberOfFaceUpCards =
                Helper.GetNumberOfFaceUpCardsForNumberOfUsers(numUsers, classicClue);
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(allLocationCards);
        allCards.addAll(allSuspectCards);
        allCards.addAll(allWeaponCards);

        for (int i = 0; i < numberOfFaceUpCards; i++) {
            faceUpCards.add(allCards.remove(rngjesus.nextInt(allCards.size())));
        }

        int numberOfRemainingCards = allCards.size();
        int numberOfCardsPerUser = numberOfRemainingCards / numUsers;

        for (int i = 0; i < numUsers; i++) {
            ArrayList<Card> playerCardList = players.get(i).getCards();

            for (int j = 0; j < numberOfCardsPerUser; j++) {
                // Message cardMessage = new Message(MessagesEnum.MESSAGE_CARD_FROM_SERVER, );
                Card card = allCards.remove(rngjesus.nextInt(allCards.size()));
                playerCardList.add(card);
                // TODO: Send the card message to client
            }
        }

        logger.warn(
                "Number of cards left in the CardDeck after dealing all cards " + allCards.size());
        // There should be no more Cards in allCards now
    }

    private void populateEnvelope() {
        // grab 1 random card from each card type array and put it in envelope
        // remove that card from the list
        Card suspect = allSuspectCards.remove(rngjesus.nextInt(allSuspectCards.size()));
        Card location = allLocationCards.remove(rngjesus.nextInt(allLocationCards.size()));
        Card weapon = allWeaponCards.remove(rngjesus.nextInt(allWeaponCards.size()));
        envelope.setLocation(location);
        envelope.setSuspect(suspect);
        envelope.setWeapon(weapon);
    }

    public ArrayList<Card> getFaceUpCards() {
        return faceUpCards;
    }
}
