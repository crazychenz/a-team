/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import clueless.Card;
import clueless.SuspectCard;
import clueless.WeaponCard;
import java.util.HashMap;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author chenz */
public class GooeyPiece {

    private static final Logger logger = LogManager.getLogger(GooeyPiece.class);

    private static HashMap<Integer, Image> imgById = new HashMap<>();

    static {
        Image img;
        String prefix = "clueless/client/gooey/sprites/";

        // img = new Image(prefix + "board.png", 600, 600, false, false);
        // imgByName.put("board", img);
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

    public GooeyPiece(Pane pane, Card card, double x, double y) {
        this.pane = pane;
        this.x = x;
        this.y = y;
        this.view = new ImageView(getImageById(card.getId()));
        this.view.setOpacity(0.7);
        this.label = new Label(card.getName());
        this.pane.getChildren().add(this.view);
        // this.pane.getChildren().add(this.label);
        this.view.relocate(x, y);
        this.label.relocate(x, y);
    }

    public void close() {
        pane.getChildren().remove(this.view);
        // pane.getChildren().remove(this.label);
    }
}
