/** */
package clueless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the Dealer of the Cards
 *
 * @author ateam
 */
public class Dealer {

    private static final Logger logger = LogManager.getLogger(Dealer.class);

    private final ArrayList<SuspectCard> allSuspectCards;
    private final ArrayList<RoomCard> allLocationCards;
    private final ArrayList<WeaponCard> allWeaponCards;
    private final Random prng;

    /**
     * Constructor
     *
     * @param seed The seed for the PRNG
     */
    public Dealer(long seed) {
        logger.debug("Setting up CardDeck");

        // Allocate all the things
        prng = new Random(seed);
        allSuspectCards = new ArrayList<>(SuspectCard.allCards);
        allLocationCards = new ArrayList<>(RoomCard.allCards);
        allWeaponCards = new ArrayList<>(WeaponCard.allCards);

        // Shuffle each deck
        Collections.shuffle(allSuspectCards, prng);
        Collections.shuffle(allLocationCards, prng);
        Collections.shuffle(allWeaponCards, prng);
    }

    /**
     * Creates and populates the Envelope
     *
     * @return A new populated Envelope
     */
    public Envelope populateEnvelope() {
        // grab 1 random card from each card type array and put it in envelope
        // remove that card from the list
        SuspectCard suspect = allSuspectCards.remove(0);
        RoomCard location = allLocationCards.remove(0);
        WeaponCard weapon = allWeaponCards.remove(0);
        return new Envelope(suspect, location, weapon);
    }

    /**
     * Helper to determine the number of face up cards to display.
     *
     * @param numUsers Number of Players being dealt cards
     * @param difficulty 0 (easy), 1 (medium), 2 (hard) difficulty to specify how many face up cards
     * @return number of cards faceup
     */
    public static int FaceUpCardsByPlayerCount(int numUsers, int difficulty) {
        if (difficulty == 0) {
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
        if (difficulty == 1) {
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
        if (difficulty == 2) {
            return 0;
        }
        return 0;
    }

    /**
     * Deals the card to the Player objects and the face up pile
     *
     * @param players Players to deal cards to.
     * @param faceUpCards Face up card pile list.
     * @param difficulty 0 (easy), 1 (medium), 2 (hard) difficulty to specify how many face up cards
     */
    public void dealCards(PlayerMgr players, ArrayList<Card> faceUpCards, int difficulty) {

        logger.debug("Dealing to " + players.count() + " players.");
        int numberOfFaceUpCards = Dealer.FaceUpCardsByPlayerCount(players.count(), difficulty);

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
