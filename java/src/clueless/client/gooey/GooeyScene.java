/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import clueless.client.*;
import clueless.client.cli.*;
import clueless.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author chenz */
public class GooeyScene implements Initializable {

    private static final Logger logger = LogManager.getLogger(GooeyScene.class);

    private ClientState clientState;
    private Watchdog watchdog;
    private GooeyEventHandler evtHandler;
    private CLI cli;

    @FXML private Label label;
    @FXML private Canvas canvas;
    @FXML private Pane pane;
    @FXML private AnchorPane anchorPane;
    @FXML private TextField cliField;
    @FXML private ListView logList;

    private ArrayList<String> logArray;

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

    public void addToLogList(String str) {
        logArray.add(str);
        logList.setItems(FXCollections.observableArrayList(logArray));
        logList.scrollTo(logArray.size() - 1);
    }

    @FXML
    private void handleCliAction(ActionEvent event) {

        // Grab the command from text field
        String cmd = cliField.getText();

        // Reset text field
        cliField.setText("");

        // Add command to log view
        logger.info("CLI Command Entered: " + cmd);
        addToLogList(cmd);

        // Attempt to parse command
        Message msg = CLI.processCommand(cli, cmd);
        if (msg == null) {
            // Nothing to do if there is no message
            return;
        }

        if (msg.getMessageID() == MessagesEnum.MESSAGE_ERROR) {
            // Added bad command response to list view
            addToLogList(msg.asString());
            return;
        }

        if (msg.getMessageID() == MessagesEnum.MESSAGE_INFO) {
            // Add informative response to list view
            addToLogList(msg.asString());
            return;
        }

        // If its a message for the server, send it!
        try {
            cli.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Failed to send Message: " + msg.getMessageID());
        }
    }

    @FXML
    private void onLoad(ActionEvent event) {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // anchorPane.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE,
        // CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public GooeyScene() {
        logArray = new ArrayList<>();

        clientState = new ClientState();
        watchdog = new Watchdog(10000);
        watchdog.pulse();
        evtHandler = new GooeyEventHandler(clientState, watchdog, this);
        cli = new CLI(evtHandler, clientState, watchdog);
    }
}
