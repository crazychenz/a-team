package clueless;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents all possible SuspectCards
 *
 * @author ateam
 */
public class SuspectCard extends Card {

    /** Enumeration of a possible SuspectCards */
    public static final SuspectCard SUSPECT_PLUM,
            SUSPECT_PEACOCK,
            SUSPECT_WHITE,
            SUSPECT_SCARLET,
            SUSPECT_GREEN,
            SUSPECT_MUSTARD;

    public static final ArrayList<SuspectCard> allCards = new ArrayList<>();

    static {
        // Note: The suspect ids are ordered according to the rules.
        allCards.add(SUSPECT_SCARLET = new SuspectCard(0x31, "Scarlet"));
        Card.register(SUSPECT_SCARLET);
        allCards.add(SUSPECT_MUSTARD = new SuspectCard(0x32, "Mustard"));
        Card.register(SUSPECT_MUSTARD);
        allCards.add(SUSPECT_WHITE = new SuspectCard(0x33, "White"));
        Card.register(SUSPECT_WHITE);
        allCards.add(SUSPECT_GREEN = new SuspectCard(0x34, "Green"));
        Card.register(SUSPECT_GREEN);
        allCards.add(SUSPECT_PEACOCK = new SuspectCard(0x35, "Peacock"));
        Card.register(SUSPECT_PEACOCK);
        allCards.add(SUSPECT_PLUM = new SuspectCard(0x36, "Plum"));
        Card.register(SUSPECT_PLUM);
    }

    private SuspectCard(int id, String name) {
        super(id, name);
    }

    public static Iterable<SuspectCard> cards() {
        return Collections.unmodifiableList(allCards);
    }

    public static SuspectCard fetch(int id) {
        return (SuspectCard) Card.fetch(id);
    }
}
