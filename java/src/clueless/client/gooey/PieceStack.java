package clueless.client.gooey;

import java.util.ArrayList;
import javafx.scene.image.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class PieceStack {

    private static final Logger logger = LogManager.getLogger(GooeyScene.class);

    private ArrayList<ImageView> views;

    public PieceStack() {
        views = new ArrayList<>();
    }

    public void update() {
        // Create and draw the ImageViews
    }
}
