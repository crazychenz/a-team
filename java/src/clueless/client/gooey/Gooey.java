/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import clueless.client.cli.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author chenz */
public class Gooey extends Application {

    private static final Logger logger = LogManager.getLogger(Gooey.class);

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Gooey");
        Parent root = FXMLLoader.load(getClass().getResource("GooeyScene.fxml"));

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new KeyboardHandler());

        // Actually die when exiting from title bar
        stage.setOnCloseRequest(
                e -> {
                    System.exit(0);
                });

        stage.setScene(scene);
        stage.show();
    }

    /** @param args the command line arguments */
    public static void main(String[] args) {
        logger.info("Initializing the CLI environment (from gooey).");
        CLI.init(args);

        launch(args);
    }
}
