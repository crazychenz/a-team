/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** @author chenz */
public class GooeyScene implements Initializable {

    @FXML private Label label;

    @FXML private Canvas canvas;

    @FXML private Pane pane;

    @FXML private AnchorPane anchorPane;

    @FXML private TextField cliField;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        // label.setText("Hello World!");

        // anchorPane.getC

        // Rectangle r = new Rectangle(25,25,250,250);
        // r.setFill(Color.BLUE);
        // pane.getChildren().add(r);
    }

    @FXML
    private void handleExitAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleCliAction(ActionEvent event) {
        System.out.println("CLI Command Entered: " + cliField.getText());
    }

    @FXML
    private void onLoad(ActionEvent event) {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // anchorPane.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE,
        // CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
