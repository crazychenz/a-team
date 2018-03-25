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
        allCards.add(SUSPECT_PLUM = new SuspectCard(0x31, "Plum"));
        allCards.add(SUSPECT_PEACOCK = new SuspectCard(0x32, "Peacock"));
        allCards.add(SUSPECT_WHITE = new SuspectCard(0x33, "White"));
        allCards.add(SUSPECT_SCARLET = new SuspectCard(0x34, "Scarlet"));
        allCards.add(SUSPECT_GREEN = new SuspectCard(0x35, "Green"));
        allCards.add(SUSPECT_MUSTARD = new SuspectCard(0x36, "Mustard"));
    }

    private SuspectCard(int id, String name) {
        super(id, name);
    }
}
