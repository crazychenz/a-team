/** */
package clueless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Dealer {

    private static final Logger logger = LogManager.getLogger(Dealer.class);

    private final ArrayList<Card> allSuspectCards;
    private final ArrayList<Card> allLocationCards;
    private final ArrayList<Card> allWeaponCards;
    private final Random prng;

    public Dealer(long seed) {
        logger.debug("Setting up CardDeck");

        // Allocate all the things
        prng = new Random(seed);
        allSuspectCards = new ArrayList<>();
        allLocationCards = new ArrayList<>();
        allWeaponCards = new ArrayList<>();

        // Organize cards into piles by type
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() != CardType.CARD_TYPE_HALLWAY) {
                this.add(new Card(value));
            }
        }

        // Shuffle each deck
        Collections.shuffle(allSuspectCards, prng);
        Collections.shuffle(allLocationCards, prng);
        Collections.shuffle(allWeaponCards, prng);
    }

    public Envelope populateEnvelope() {
        // grab 1 random card from each card type array and put it in envelope
        // remove that card from the list
        Card suspect = allSuspectCards.remove(0);
        Card location = allLocationCards.remove(0);
        Card weapon = allWeaponCards.remove(0);
        return new Envelope(suspect, location, weapon);
    }

    private void add(Card add) {
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

    public static int FaceUpCardsByPlayerCount(int numUsers, boolean classicClue) {
        if (!classicClue) {
            switch (numUsers) {
                case 3:
                case 4:
                    return 6;
                case 5:
                    return 3;
                case 6:
                    return 6;
                default:
                    return 6;
            }
        }
        if (classicClue) {
            switch (numUsers) {
                case 3:
                    return 0;
                case 4:
                    return 2;
                case 5:
                    return 3;
                case 6:
                    return 0;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public void dealCards(PlayerMgr players, ArrayList<Card> faceUpCards) {

        logger.debug("Dealing to " + players.count() + " players.");
        int numberOfFaceUpCards = Dealer.FaceUpCardsByPlayerCount(players.count(), true);

        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(allLocationCards);
        allCards.addAll(allSuspectCards);
        allCards.addAll(allWeaponCards);

        for (int i = 0; i < numberOfFaceUpCards; i++) {
            faceUpCards.add(allCards.remove(0));
        }

        // Deal cards until we have no more
        Player player = players.current();
        while (allCards.size() > 0) {
            player.getCards().add(allCards.remove(0));
            player = player.getNext();
        }
    }
}
