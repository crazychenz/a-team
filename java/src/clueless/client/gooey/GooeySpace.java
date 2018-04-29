package clueless.client.gooey;

import clueless.*;
import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author ateam */
public class GooeySpace {

    private static final Logger logger = LogManager.getLogger(GooeySpace.class);

    Pane pane;
    double x;
    double y;
    ImageView view;

    Rectangle rect;
    Label label;

    ArrayList<GooeyPiece> suspects;
    ArrayList<GooeyPiece> weapons;

    public GooeySpace(
            Pane pane,
            String name,
            double x,
            double y,
            double w,
            double h,
            String color,
            Image sprite) {

        suspects = new ArrayList<>();
        weapons = new ArrayList<>();

        this.x = x;
        this.y = y;
        this.pane = pane;
        if (color != null) {
            this.rect = new Rectangle(w, h, Paint.valueOf(color));
            this.rect.setStroke(Paint.valueOf("black"));
            this.pane.getChildren().add(this.rect);
        }
        if (sprite != null) {
            this.view = new ImageView();
            this.view.setImage(sprite);
            this.pane.getChildren().add(this.view);
            this.view.relocate(x, y);
        }
        this.label = new Label(name);
        this.pane.getChildren().add(this.label);
        this.label.relocate(x + 10, y + 80);
        // this.label.getGraphic().setStyle("-fx-text-fill: white; -fx-background-color: #d3d3d3;");
        this.label.setStyle("-fx-text-fill: green; -fx-background-color: #d3d3d3;");
        if (color != null) {
            this.rect.relocate(x, y);
        }
    }

    public void clearPieces() {
        for (GooeyPiece piece : suspects) {
            piece.close();
        }
        suspects.clear();
        for (GooeyPiece piece : weapons) {
            piece.close();
        }
        weapons.clear();
    }

    public void addSuspect(SuspectCard card) {
        double yOffset = (suspects.size() * 8) + 8;
        logger.trace("Adding suspect to " + x + ", " + (y + yOffset));
        suspects.add(new GooeyPiece(pane, card, x, y + yOffset));
    }

    public void addWeapon(WeaponCard card) {
        double yOffset = (weapons.size() * 8) + 8;
        logger.trace("Adding weapon to " + (x + 50) + ", " + (y + yOffset));
        weapons.add(new GooeyPiece(pane, card, x + 50, y + yOffset));
    }
}
