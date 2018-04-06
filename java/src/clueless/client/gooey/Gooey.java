/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** @author chenz */
public class Gooey extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Gooey");
        Parent root = FXMLLoader.load(getClass().getResource("GooeyScene.fxml"));

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new KeyboardHandler());

        // Create the initial Location
        // Location origin = new Location();

        stage.setScene(scene);
        stage.show();
    }

    /** @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
