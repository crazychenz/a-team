/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import clueless.*;
import clueless.client.*;
import clueless.client.cli.*;
import clueless.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author chenz */
public class GooeyScene implements Initializable {

    private static final Logger logger = LogManager.getLogger(GooeyScene.class);

    private Client client;
    private ClientState clientState;
    private Heartbeat heartbeat;
    private Thread heartbeatThread;

    @FXML private AnchorPane anchorPane;
    @FXML private TextField cliField;
    @FXML private ListView logList;

    @FXML private Pane boardPane;

    private HashMap<String, Image> imgByName;

    private ArrayList<String> logArray;

    public HashMap<Integer, GooeySpace> spaces;

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
    private void handleMoveNorthAction(ActionEvent event) {
        Message msg = ClientCommand.processCommand(clientState, "move north");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveWestAction(ActionEvent event) {
        Message msg = ClientCommand.processCommand(clientState, "move west");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveSecretAction(ActionEvent event) {
        Message msg = ClientCommand.processCommand(clientState, "move secret");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveEastAction(ActionEvent event) {
        Message msg = ClientCommand.processCommand(clientState, "move east");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveSouthAction(ActionEvent event) {
        Message msg = ClientCommand.processCommand(clientState, "move south");
        handleInternalMessage(msg);
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
        Message msg = ClientCommand.processCommand(clientState, cmd);
        handleInternalMessage(msg);
    }

    @FXML
    private void onLoad(ActionEvent event) {}

    private void handleInternalMessage(Message msg) {
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
            clientState.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Failed to send Message: " + msg.getMessageID());
        }
    }

    private void addRoom(Location location, double x, double y) {
        String name = location.getName();
        GooeyRoom room = new GooeyRoom(boardPane, name, x, y);
        spaces.put(location.getId(), room);
    }

    private void addHall(Location location, double x, double y) {
        String name = location.getName();
        GooeyHallway room = new GooeyHallway(boardPane, name, x, y);
        spaces.put(location.getId(), room);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // All initial dynamic scene graph stuff must be done here.

        // Rooms
        addRoom(Room.LOCATION_STUDY, 0, 0);
        addRoom(Room.LOCATION_HALL, 150, 0);
        addRoom(Room.LOCATION_LOUNGE, 300, 0);
        addRoom(Room.LOCATION_LIBRARY, 0, 150);
        addRoom(Room.LOCATION_BILLIARDROOM, 150, 150);
        addRoom(Room.LOCATION_DININGROOM, 300, 150);
        addRoom(Room.LOCATION_CONSERVATORY, 0, 300);
        addRoom(Room.LOCATION_BALLROOM, 150, 300);
        addRoom(Room.LOCATION_KITCHEN, 300, 300);

        // Hallways
        addHall(Hallway.HALLWAY_STUDY_HALL, 100, 25);
        addHall(Hallway.HALLWAY_HALL_LOUNGE, 250, 25);
        addHall(Hallway.HALLWAY_STUDY_LIBRARY, 25, 100);
        addHall(Hallway.HALLWAY_HALL_BILLIARD, 175, 100);
        addHall(Hallway.HALLWAY_LOUNGE_DINING, 325, 100);
        addHall(Hallway.HALLWAY_LIBRARY_BILLIARD, 100, 175);
        addHall(Hallway.HALLWAY_DINING_BILLIARD, 250, 175);
        addHall(Hallway.HALLWAY_CONSERVATORY_LIBRARY, 25, 250);
        addHall(Hallway.HALLWAY_BALL_BILLIARD, 175, 250);
        addHall(Hallway.HALLWAY_DINING_KITCHEN, 325, 250);
        addHall(Hallway.HALLWAY_BALL_CONSERVATORY, 100, 325);
        addHall(Hallway.HALLWAY_KITCHEN_BALL, 250, 325);

        // spaces.add(new GooeySpace(boardPane, "game", 300, 30, 0));
    }

    private void loadImages() {
        Image img;
        String prefix = "clueless/client/gooey/sprites/";
        imgByName = new HashMap<>();
        // img = new Image(prefix + "board.png", 600, 600, false, false);
        // imgByName.put("board", img);
        img = new Image(prefix + "mustard.jpg", 40, 40, false, false);
        imgByName.put("mustard", img);
        img = new Image(prefix + "peacock.png", 40, 40, false, false);
        imgByName.put("peacock", img);
        img = new Image(prefix + "plum.png", 40, 40, false, false);
        imgByName.put("plum", img);
        img = new Image(prefix + "green.png", 40, 40, false, false);
        imgByName.put("green", img);
        img = new Image(prefix + "white.png", 40, 40, false, false);
        imgByName.put("white", img);
        img = new Image(prefix + "scarlet.jpg", 40, 40, false, false);
        imgByName.put("scarlet", img);
    }

    public GooeyScene() {
        logArray = new ArrayList<>();

        loadImages();

        spaces = new HashMap<>();

        clientState = new ClientState();
        startup("127.0.0.1", "2323");
    }

    public void startup(String addr, String port) {
        EventHandler evtHdlr = new GooeyEventHandler(clientState, this);
        clientState.startup(evtHdlr, addr, port);
    }
}
