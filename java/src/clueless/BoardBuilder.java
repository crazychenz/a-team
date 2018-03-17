/** */
package clueless;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.util.Map.Entry;

/** @author tombo */
public class BoardBuilder {
    // TODO show weapon locations also
    private ClientState clientState;

    public BoardBuilder(ClientState clientState) {
        this.clientState = clientState;
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
        String toReturn = "[";
        if (clientState.getGameState().getSuspectLocations().containsValue(room)) {
            for (Entry<CardsEnum, CardsEnum> entry :
                    clientState.getGameState().getSuspectLocations().entrySet()) {
                if (entry.getValue().equals(room)) {
                    toReturn += entry.getKey().getLabel() + ",";
                }
            }
            toReturn = toReturn.substring(0, toReturn.length() - 1);
        }
        toReturn += "]";
        return toReturn;
    }
}
