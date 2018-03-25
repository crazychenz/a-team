package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class SuspectMap {

    private final HashMap<SuspectCard, Suspect> suspects = new HashMap<>();

    public SuspectMap(GameBoard board) {
        createSuspects(board);
    }

    private void createSuspects(GameBoard board) {
        for (SuspectCard card : SuspectCard.allCards) {
            Suspect suspect;
            suspect = new Suspect(card);
            suspects.put(card, suspect);

            // Put new suspect into its location.
            Location location = SuspectMap.getStartLocation(card);
            location.placeSuspect(suspect);
        }
    }

    public Suspect getByCard(SuspectCard value) {
        // Bleh
        for (Entry<SuspectCard, Suspect> entry : suspects.entrySet()) {
            if (entry.getKey().equals(value)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public ArrayList<Suspect> getAllSuspects() {
        return new ArrayList<Suspect>(suspects.values());
    }

    public AvailableSuspects getAvailableSuspects() {
        AvailableSuspects availableSuspects = new AvailableSuspects();
        for (Suspect suspect : suspects.values()) {
            if (!suspect.getActive()) {
                availableSuspects.list.add(suspect.getSuspect());
            }
        }
        return availableSuspects;
    }

    public static Location getStartLocation(SuspectCard suspect) {
        if (suspect.equals(SuspectCard.SUSPECT_PLUM)) {
            return Hallway.HALLWAY_STUDY_LIBRARY;
        }

        if (suspect.equals(SuspectCard.SUSPECT_PEACOCK)) {
            return Hallway.HALLWAY_CONSERVATORY_LIBRARY;
        }

        if (suspect.equals(SuspectCard.SUSPECT_GREEN)) {
            return Hallway.HALLWAY_BALL_CONSERVATORY;
        }

        if (suspect.equals(SuspectCard.SUSPECT_WHITE)) {
            return Hallway.HALLWAY_KITCHEN_BALL;
        }

        if (suspect.equals(SuspectCard.SUSPECT_MUSTARD)) {
            return Hallway.HALLWAY_LOUNGE_DINING;
        }

        if (suspect.equals(SuspectCard.SUSPECT_SCARLET)) {
            return Hallway.HALLWAY_HALL_LOUNGE;
        }

        // TODO: Throw exception
        return null;
    }
}
