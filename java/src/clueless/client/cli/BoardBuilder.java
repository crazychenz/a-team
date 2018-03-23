/** */
package clueless.client.cli;

import clueless.*;
import clueless.client.*;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

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
                "Study* - " + returnSuspectsInRoom(CardsEnum.LOCATION_STUDY),
                returnSuspectsInRoom(CardsEnum.HALLWAY_STUDY_HALL),
                "Hall - " + returnSuspectsInRoom(CardsEnum.LOCATION_HALL),
                returnSuspectsInRoom(CardsEnum.HALLWAY_HALL_LOUNGE),
                "*Lounge - " + returnSuspectsInRoom(CardsEnum.LOCATION_LOUNGE));
        at.addRow(
                " " + returnSuspectsInRoom(CardsEnum.HALLWAY_STUDY_LIBRARY),
                "",
                " " + returnSuspectsInRoom(CardsEnum.HALLWAY_HALL_BILLIARD),
                "",
                " " + returnSuspectsInRoom(CardsEnum.HALLWAY_LOUNGE_DINING));
        at.addRow(
                "Library - " + returnSuspectsInRoom(CardsEnum.LOCATION_LIBRARY),
                returnSuspectsInRoom(CardsEnum.HALLWAY_LIBRARY_BILLIARD),
                "Billiard Room - " + returnSuspectsInRoom(CardsEnum.LOCATION_BILLIARDROOM),
                returnSuspectsInRoom(CardsEnum.HALLWAY_DINING_BILLIARD),
                "Dining Room - " + returnSuspectsInRoom(CardsEnum.LOCATION_DININGROOM));
        at.addRow(
                " " + returnSuspectsInRoom(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY),
                "",
                " " + returnSuspectsInRoom(CardsEnum.HALLWAY_BALL_BILLIARD),
                "",
                " " + returnSuspectsInRoom(CardsEnum.HALLWAY_DINING_KITCHEN));
        at.addRow(
                "Conservatory* - " + returnSuspectsInRoom(CardsEnum.LOCATION_CONSERVATORY),
                returnSuspectsInRoom(CardsEnum.HALLWAY_BALL_CONSERVATORY),
                "Ballroom - " + returnSuspectsInRoom(CardsEnum.LOCATION_BALLROOM),
                returnSuspectsInRoom(CardsEnum.HALLWAY_KITCHEN_BALL),
                "*Kitchen - " + returnSuspectsInRoom(CardsEnum.LOCATION_KITCHEN));
        at.addRule();
        at.getContext().setWidth(140);
        at.getContext().setGridTheme(TA_GridThemes.OUTSIDE);
        at.setTextAlignment(TextAlignment.CENTER);
        String rend = at.render();
        return rend;
    }

    private String returnSuspectsInRoom(CardsEnum room) {
        // Not sure why this was changed lol it worked before. had to fix it
        String toReturn = "[";
        // if (clientState.getGameState().getSuspectLocations().containsValue(room)) {
        // for (Entry<CardsEnum, CardsEnum> entry :
        //        clientState.getGameState().getSuspectLocations().entrySet()) {
        for (Suspect entry : clientState.getGameState().getSuspectLocations()) {
            CardsEnum location = entry.getCurrent_location();
            if (location == room) {
                toReturn += entry.getSuspect().getLabel() + ",";
                // toReturn += entry.getKey().getLabel() + ",";
            }
        }

        // Remove last comma, only if there are suspects in the room
        if (toReturn.length() > 1) {
            toReturn = toReturn.substring(0, toReturn.length() - 1);
        }

        toReturn += "]";
        toReturn += "\n";
        toReturn += returnWeaponsInRoom(room);
        return toReturn;
    }

    private String returnWeaponsInRoom(CardsEnum room) {
        String toReturn = "{";
        for (Weapon entry : clientState.getGameState().getWeaponLocations()) {
            CardsEnum location = entry.getCurrent_location();
            if (location == room) {
                toReturn += entry.getWeapon().getLabel() + ",";
            }
        }

        // Remove last comma, only if there are weapons in the room
        if (toReturn.length() > 1) {
            toReturn = toReturn.substring(0, toReturn.length() - 1);
        }

        toReturn += "}";
        return toReturn;
    }
}
