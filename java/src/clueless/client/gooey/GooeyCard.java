/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import clueless.Card;
import clueless.RoomCard;
import clueless.SuspectCard;
import clueless.WeaponCard;
import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GooeyCard {

    private static final Logger logger = LogManager.getLogger(GooeyCard.class);

    private static HashMap<Integer, Image> imgById = new HashMap<>();

    static {
        Image img;
        String prefix = "clueless/client/gooey/sprites/";

        img = new Image(prefix + "mustard.jpg", 40, 40, false, false);
        imgById.put(SuspectCard.SUSPECT_MUSTARD.getId(), img);
        img = new Image(prefix + "peacock.png", 40, 40, false, false);
        imgById.put(SuspectCard.SUSPECT_PEACOCK.getId(), img);
        img = new Image(prefix + "plum.png", 40, 40, false, false);
        imgById.put(SuspectCard.SUSPECT_PLUM.getId(), img);
        img = new Image(prefix + "green.png", 40, 40, false, false);
        imgById.put(SuspectCard.SUSPECT_GREEN.getId(), img);
        img = new Image(prefix + "white.png", 40, 40, false, false);
        imgById.put(SuspectCard.SUSPECT_WHITE.getId(), img);
        img = new Image(prefix + "scarlet.jpg", 40, 40, false, false);
        imgById.put(SuspectCard.SUSPECT_SCARLET.getId(), img);

        img = new Image(prefix + "candle.png", 40, 40, false, false);
        imgById.put(WeaponCard.WEAPON_CANDLESTICK.getId(), img);
        img = new Image(prefix + "dagger.png", 40, 40, false, false);
        imgById.put(WeaponCard.WEAPON_DAGGER.getId(), img);
        img = new Image(prefix + "pipe.png", 40, 40, false, false);
        imgById.put(WeaponCard.WEAPON_LEADPIPE.getId(), img);
        img = new Image(prefix + "gun.png", 40, 40, false, false);
        imgById.put(WeaponCard.WEAPON_REVOLVER.getId(), img);
        img = new Image(prefix + "rope.jpg", 40, 40, false, false);
        imgById.put(WeaponCard.WEAPON_ROPE.getId(), img);
        img = new Image(prefix + "wrench.png", 40, 40, false, false);
        imgById.put(WeaponCard.WEAPON_WRENCH.getId(), img);

        img = new Image(prefix + "kitchen.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_KITCHEN.getId(), img);
        img = new Image(prefix + "ballroom.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_BALLROOM.getId(), img);
        img = new Image(prefix + "conservatory.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_CONSERVATORY.getId(), img);
        img = new Image(prefix + "library.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_LIBRARY.getId(), img);
        img = new Image(prefix + "hall.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_HALL.getId(), img);
        img = new Image(prefix + "study.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_STUDY.getId(), img);
        img = new Image(prefix + "dining.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_DININGROOM.getId(), img);
        img = new Image(prefix + "lounge.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_LOUNGE.getId(), img);
        img = new Image(prefix + "billiards.png", 40, 40, false, false);
        imgById.put(RoomCard.LOCATION_BILLIARDROOM.getId(), img);
    };

    public static Image getImageById(Integer id) {
        return imgById.get(id);
    }

    double x;
    double y;

    Pane pane;
    ImageView view;
    Label label;

    /*public GooeyPiece(
            Pane pane, String name, double x, double y, double w, double h, String color) {
        this.pane = pane;
        this.rect = new Rectangle(w, h, Paint.valueOf(color));
        this.label = new Label(name);
        this.pane.getChildren().add(this.rect);
        this.pane.getChildren().add(this.label);
        this.label.relocate(x, y);
        this.rect.relocate(x, y);
    }*/

    public GooeyCard(Pane pane, Card card, double x, double y) {
        this.pane = pane;
        this.x = x;
        this.y = y;
        this.view = new ImageView(getImageById(card.getId()));
        this.view.setClip(null);
        // I cant get the cards to have a border.. lol. not sure what settings need to be set
        this.view.setStyle(
                "-fx-padding: 15; -fx-background-color: white; -fx-background-radius:5;");
        this.view.setOnMousePressed(
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent arg0) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Card Info");

                        if (Card.fetch(card.getId()).getClass() == RoomCard.class) {
                            alert.setHeaderText("Room: " + card.getName());
                        } else if (Card.fetch(card.getId()).getClass() == WeaponCard.class) {
                            alert.setHeaderText("Weapon: " + card.getName());
                        } else {
                            alert.setHeaderText("Suspect: " + card.getName());
                        }
                        alert.setResizable(false);
                        alert.showAndWait();
                    }
                });
        this.label = new Label(card.getName());
        this.pane.getChildren().add(this.view);
        this.view.relocate(x, y);
        this.label.relocate(x, y);
    }

    public void close() {
        pane.getChildren().remove(this.view);
    }
}
