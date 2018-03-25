/** */
package clueless.client.cli;

import clueless.*;
import clueless.client.*;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.util.HashMap;
import java.util.Map.Entry;

/** @author tombo */
public class BoardBuilder {
    // TODO show weapon locations also
    private final ClientState clientState;

    public BoardBuilder(ClientState cs) {
        this.clientState = cs;
    }

    public String generateBoard() {

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow(
                "Study* - " + returnSuspectsInRoom(Room.LOCATION_STUDY),
                returnSuspectsInRoom(Hallway.HALLWAY_STUDY_HALL),
                "Hall - " + returnSuspectsInRoom(Room.LOCATION_HALL),
                returnSuspectsInRoom(Hallway.HALLWAY_HALL_LOUNGE),
                "*Lounge - " + returnSuspectsInRoom(Room.LOCATION_LOUNGE));
        at.addRow(
                " " + returnSuspectsInRoom(Hallway.HALLWAY_STUDY_LIBRARY),
                "",
                " " + returnSuspectsInRoom(Hallway.HALLWAY_HALL_BILLIARD),
                "",
                " " + returnSuspectsInRoom(Hallway.HALLWAY_LOUNGE_DINING));
        at.addRow(
                "Library - " + returnSuspectsInRoom(Room.LOCATION_LIBRARY),
                returnSuspectsInRoom(Hallway.HALLWAY_LIBRARY_BILLIARD),
                "Billiard Room - " + returnSuspectsInRoom(Room.LOCATION_BILLIARDROOM),
                returnSuspectsInRoom(Hallway.HALLWAY_DINING_BILLIARD),
                "Dining Room - " + returnSuspectsInRoom(Room.LOCATION_DININGROOM));
        at.addRow(
                " " + returnSuspectsInRoom(Hallway.HALLWAY_CONSERVATORY_LIBRARY),
                "",
                " " + returnSuspectsInRoom(Hallway.HALLWAY_BALL_BILLIARD),
                "",
                " " + returnSuspectsInRoom(Hallway.HALLWAY_DINING_KITCHEN));
        at.addRow(
                "Conservatory* - " + returnSuspectsInRoom(Room.LOCATION_CONSERVATORY),
                returnSuspectsInRoom(Hallway.HALLWAY_BALL_CONSERVATORY),
                "Ballroom - " + returnSuspectsInRoom(Room.LOCATION_BALLROOM),
                returnSuspectsInRoom(Hallway.HALLWAY_KITCHEN_BALL),
                "*Kitchen - " + returnSuspectsInRoom(Room.LOCATION_KITCHEN));
        at.addRule();
        at.getContext().setWidth(140);
        at.getContext().setGridTheme(TA_GridThemes.OUTSIDE);
        at.setTextAlignment(TextAlignment.CENTER);
        String rend = at.render();

        return rend;
    }

    private String returnSuspectsInRoom(Location room) {
        // Not sure why this was changed lol it worked before. had to fix it
        String toReturn = "[";
        HashMap<SuspectCard, Integer> map;

        map = clientState.getGameState().getSuspectLocations();
        if (map.containsValue(room.getId())) {
            for (Entry<SuspectCard, Integer> entry : map.entrySet()) {
                if (entry.getValue() == room.getId()) {
                    toReturn += entry.getKey().getName() + ",";
                }
            }

            // Remove last comma, only if there are suspects in the room
            if (toReturn.length() > 1) {
                toReturn = toReturn.substring(0, toReturn.length() - 1);
            }
        }

        toReturn += "]";
        toReturn += "\n";
        toReturn += returnWeaponsInRoom(room);
        return toReturn;
    }

    private String returnWeaponsInRoom(Location room) {
        String toReturn = "{";

        HashMap<WeaponCard, Integer> map;
        map = clientState.getGameState().getWeaponLocations();
        if (map.containsValue(room.getId())) {
            for (Entry<WeaponCard, Integer> entry : map.entrySet()) {
                if (entry.getValue() == room.getId()) {
                    toReturn += entry.getKey().getName() + ",";
                }
            }

            // Remove last comma, only if there are suspects in the room
            if (toReturn.length() > 1) {
                toReturn = toReturn.substring(0, toReturn.length() - 1);
            }
        }

        toReturn += "}";
        return toReturn;
    }
}
