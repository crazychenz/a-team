package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Represents storage of all possible Suspect objects and actions related to management of Suspect
 * objects.
 *
 * @author ateam
 */
public class SuspectMap {

    private final HashMap<SuspectCard, Suspect> suspects = new HashMap<>();

    /** Constructor for SuspectMap */
    public SuspectMap() {
        createSuspects();
    }

    private void createSuspects() {
        for (SuspectCard card : SuspectCard.cards()) {
            Suspect suspect;
            suspect = new Suspect(card);
            suspects.put(card, suspect);

            // Put new suspect into its location.
            Location location = SuspectMap.getStartLocation(card);
            location.placeSuspect(suspect);
        }
    }

    /**
     * Lookup Suspect object with SuspectCard
     *
     * @param value SuspectCard used to lookup Suspect
     * @return First Suspect found or null.
     */
    public Suspect getByCard(SuspectCard value) {
        // Bleh
        for (Entry<SuspectCard, Suspect> entry : suspects.entrySet()) {
            if (entry.getKey().equals(value)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Get array of all Suspect objects.
     *
     * @return ArrayList of all Suspect objects.
     */
    public ArrayList<Suspect> getAllSuspects() {
        return new ArrayList<>(suspects.values());
    }

    /**
     * Get suspects not currently marked active.
     *
     * @return AvailableSuspects object containing all non-active Suspects
     */
    public AvailableSuspects getAvailableSuspects() {
        AvailableSuspects availableSuspects = new AvailableSuspects();
        for (Suspect suspect : suspects.values()) {
            if (!suspect.getActive()) {
                availableSuspects.list.add(suspect.getSuspect());
            }
        }
        return availableSuspects;
    }

    /**
     * Lookup the start location of each Suspect by SuspectCard.
     *
     * @param suspect SuspectCard of the Suspect start location to lookup.
     * @return The start Location of the Suspect
     */
    public static Location getStartLocation(SuspectCard suspect) {
        if (suspect.equals(SuspectCard.SUSPECT_PLUM)) {
            return Hallway.HALLWAY_PLUM_START;
        }

        if (suspect.equals(SuspectCard.SUSPECT_PEACOCK)) {
            return Hallway.HALLWAY_PEACOCK_START;
        }

        if (suspect.equals(SuspectCard.SUSPECT_GREEN)) {
            return Hallway.HALLWAY_GREEN_START;
        }

        if (suspect.equals(SuspectCard.SUSPECT_WHITE)) {
            return Hallway.HALLWAY_WHITE_START;
        }

        if (suspect.equals(SuspectCard.SUSPECT_MUSTARD)) {
            return Hallway.HALLWAY_MUSTARD_START;
        }

        if (suspect.equals(SuspectCard.SUSPECT_SCARLET)) {
            return Hallway.HALLWAY_SCARLET_START;
        }

        // TODO: Throw exception
        return null;
    }
}
