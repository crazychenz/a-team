package clueless;

import java.util.ArrayList;

public class SuspectCard extends Card {

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
        allCards.add(SUSPECT_MUSTARD = new SuspectCard(0x32, "Mustard"));
        allCards.add(SUSPECT_WHITE = new SuspectCard(0x33, "White"));
        allCards.add(SUSPECT_GREEN = new SuspectCard(0x34, "Green"));
        allCards.add(SUSPECT_PEACOCK = new SuspectCard(0x35, "Peacock"));
        allCards.add(SUSPECT_PLUM = new SuspectCard(0x36, "Plum"));
    }

    private SuspectCard(int id, String name) {
        super(id, name);
    }
}
