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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.Callback;
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
    @FXML private ImageView cluelessLogo;
    @FXML private TextField cliField;
    @FXML public TextField suspectField;
    @FXML private ListView<String> logList;
    @FXML private TextArea notesArea;
    @FXML private TextArea asciiNotebook;

    @FXML public Pane boardPane;
    @FXML private Pane myCardPane;
    @FXML private Pane otherCardPane;
    @FXML private Pane actionPane;
    @FXML private ImageView boardOverlay;

    private HashMap<String, Image> imgByName;

    private ArrayList<String> logArray;

    public HashMap<Integer, GooeySpace> spaces;
    public HashMap<Integer, GooeyCard> myCards;
    public HashMap<Integer, GooeyCard> otherCards;

    @FXML
    private void handleDone(ActionEvent event) {
        String cmd = "done";
        addToLogList(cmd);
        Message msg = ClientCommand.processCommand(clientState, cmd);
        handleInternalMessage(msg);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void handleAccuse(ActionEvent event) {
        Dialog<Suggestion> dialog = new Dialog<>();
        dialog.setTitle("Accuse Dialog");
        dialog.setHeaderText("Accuse suspect in room with weapon.");
        dialog.setResizable(true);

        Label suspectLabel = new Label("Suspect: ");
        Label roomLabel = new Label("Room: ");
        Label weaponLabel = new Label("Weapon: ");

        ChoiceBox<SuspectCard> suspectChoice = new ChoiceBox<SuspectCard>();
        for (SuspectCard card : SuspectCard.allCards) {
            suspectChoice.getItems().add(card);
        }
        suspectChoice.getSelectionModel().selectFirst();

        ChoiceBox<RoomCard> roomChoice = new ChoiceBox<RoomCard>();
        for (RoomCard card : RoomCard.allCards) {
            roomChoice.getItems().add(card);
        }
        roomChoice.getSelectionModel().selectFirst();

        ChoiceBox<WeaponCard> weaponChoice = new ChoiceBox<WeaponCard>();
        for (WeaponCard card : WeaponCard.allCards) {
            weaponChoice.getItems().add(card);
        }
        weaponChoice.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();
        grid.add(suspectLabel, 1, 1);
        grid.add(suspectChoice, 2, 1);

        grid.add(roomLabel, 1, 2);
        grid.add(roomChoice, 2, 2);

        grid.add(weaponLabel, 1, 3);
        grid.add(weaponChoice, 2, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeAccuse = new ButtonType("Accuse", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeAccuse);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(
                new Callback<ButtonType, Suggestion>() {
                    @Override
                    public Suggestion call(ButtonType b) {
                        if (b == buttonTypeAccuse) {
                            return new Suggestion(
                                    suspectChoice.getValue(),
                                    roomChoice.getValue(),
                                    weaponChoice.getValue());
                        }
                        return null;
                    }
                });

        Bounds bounds = boardPane.localToScreen(boardPane.getBoundsInLocal());
        dialog.setX(bounds.getMinX());
        dialog.setY(bounds.getMinY());

        Optional<Suggestion> result = dialog.showAndWait();
        if (result.isPresent()) {
            Suggestion accusal = result.get();
            String cmd =
                    "accuse "
                            + accusal.getSuspect().getName()
                            + " "
                            + accusal.getRoom().getName()
                            + " "
                            + accusal.getWeapon().getName();
            addToLogList(cmd);
            Message msg = ClientCommand.processCommand(clientState, cmd);
            handleInternalMessage(msg);
        }
    }

    @FXML
    private void handleSuggestion(ActionEvent event) {

        Card roomCard = Card.fetch(clientState.getMyLocation());
        if (roomCard == null) {
            // No card means no room
            return;
        }
        if (!(roomCard instanceof RoomCard)) {
            // Suggestion is only allowed from within a room.
            return;
        }

        Dialog<Suggestion> dialog = new Dialog<>();
        dialog.setTitle("Suggestion Dialog");
        dialog.setHeaderText("Suggest suspect with weapon.");
        dialog.setResizable(true);

        Label suspectLabel = new Label("Suspect: ");
        Label roomLabel = new Label("Room: ");
        Label weaponLabel = new Label("Weapon: ");

        ChoiceBox<SuspectCard> suspectChoice = new ChoiceBox<SuspectCard>();
        for (SuspectCard card : SuspectCard.allCards) {
            suspectChoice.getItems().add(card);
        }
        suspectChoice.getSelectionModel().selectFirst();

        Label roomValue = new Label(roomCard.getName());

        ChoiceBox<WeaponCard> weaponChoice = new ChoiceBox<WeaponCard>();
        for (WeaponCard card : WeaponCard.allCards) {
            weaponChoice.getItems().add(card);
        }
        weaponChoice.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();

        grid.add(roomLabel, 1, 1);
        grid.add(roomValue, 2, 1);

        grid.add(suspectLabel, 1, 2);
        grid.add(suspectChoice, 2, 2);

        grid.add(weaponLabel, 1, 3);
        grid.add(weaponChoice, 2, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeSuggest = new ButtonType("Suggest", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeSuggest);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(
                new Callback<ButtonType, Suggestion>() {
                    @Override
                    public Suggestion call(ButtonType b) {
                        if (b == buttonTypeSuggest) {
                            return new Suggestion(
                                    suspectChoice.getValue(),
                                    (RoomCard) roomCard,
                                    weaponChoice.getValue());
                        }
                        return null;
                    }
                });

        Bounds bounds = boardPane.localToScreen(boardPane.getBoundsInLocal());
        dialog.setX(bounds.getMinX());
        dialog.setY(bounds.getMinY());

        Optional<Suggestion> result = dialog.showAndWait();
        if (result.isPresent()) {
            Suggestion suggestion = result.get();
            String cmd =
                    "suggest "
                            + suggestion.getSuspect().getName()
                            + " "
                            + suggestion.getWeapon().getName();
            addToLogList(cmd);
            Message msg = ClientCommand.processCommand(clientState, cmd);
            handleInternalMessage(msg);
        }
    }

    @FXML
    private void handleStartAction(ActionEvent event) {
        String cmd = "start";
        addToLogList(cmd);
        Message msg = ClientCommand.processCommand(clientState, cmd);
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveNorthAction(ActionEvent event) {
        String cmd = "move north";
        addToLogList(cmd);
        Message msg = ClientCommand.processCommand(clientState, cmd);
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveWestAction(ActionEvent event) {
        String cmd = "move west";
        addToLogList(cmd);
        Message msg = ClientCommand.processCommand(clientState, "move west");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveSecretAction(ActionEvent event) {
        String cmd = "move secret";
        addToLogList(cmd);
        Message msg = ClientCommand.processCommand(clientState, "move secret");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveEastAction(ActionEvent event) {
        String cmd = "move east";
        addToLogList(cmd);
        Message msg = ClientCommand.processCommand(clientState, "move east");
        handleInternalMessage(msg);
    }

    @FXML
    private void handleMoveSouthAction(ActionEvent event) {
        String cmd = "move south";
        addToLogList(cmd);
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

    public void handleInternalMessage(Message msg) {
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

    public void addOtherCard(Card card) {
        int numCards = otherCards.size();
        GooeyCard gcard = new GooeyCard(otherCardPane, card, numCards * 50, 20);
        otherCards.put(card.getId(), gcard);
    }

    public void addMyCard(Card card) {
        int numCards = myCards.size();
        GooeyCard gcard = new GooeyCard(myCardPane, card, numCards * 50, 20);
        myCards.put(card.getId(), gcard);
    }

    public void clearCards() {
        otherCards.clear();
        myCards.clear();
    }

    private void addRoom(Location location, double x, double y, Image img) {
        String name = location.getName();
        GooeyRoom room = new GooeyRoom(boardPane, name, x, y, img);
        spaces.put(location.getId(), room);
    }

    private void addHall(Location location, double x, double y) {
        String name = location.getName();
        GooeyHallway room = new GooeyHallway(boardPane, name, x, y, "gray");
        spaces.put(location.getId(), room);
    }

    private void addStart(Location location, double x, double y) {
        String name = location.getName();
        GooeyHallway room = new GooeyHallway(boardPane, name, x, y, null);
        spaces.put(location.getId(), room);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // logList.setBackground(Background.EMPTY);
        // notesArea.setStyle("-fx-text-inner-color: white; -fx-background-color: black;");
        // asciiNotebook.setStyle("-fx-text-inner-color: white; -fx-background-color: black;");
        // All initial dynamic scene graph stuff must be done here.

        // Rooms
        addRoom(Room.LOCATION_STUDY, 0, 0, imgByName.get("study"));
        addRoom(Room.LOCATION_HALL, 150, 0, imgByName.get("hall"));
        addRoom(Room.LOCATION_LOUNGE, 300, 0, imgByName.get("lounge"));
        addRoom(Room.LOCATION_LIBRARY, 0, 150, imgByName.get("library"));
        addRoom(Room.LOCATION_BILLIARDROOM, 150, 150, imgByName.get("billiards"));
        addRoom(Room.LOCATION_DININGROOM, 300, 150, imgByName.get("dining"));
        addRoom(Room.LOCATION_CONSERVATORY, 0, 300, imgByName.get("conservatory"));
        addRoom(Room.LOCATION_BALLROOM, 150, 300, imgByName.get("ballroom"));
        addRoom(Room.LOCATION_KITCHEN, 300, 300, imgByName.get("kitchen"));

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

        // Start Spots
        addStart(Hallway.HALLWAY_SCARLET_START, 250, 0);
        addStart(Hallway.HALLWAY_MUSTARD_START, 350, 100);
        addStart(Hallway.HALLWAY_WHITE_START, 250, 350);
        addStart(Hallway.HALLWAY_GREEN_START, 100, 350);
        addStart(Hallway.HALLWAY_PEACOCK_START, 0, 250);
        addStart(Hallway.HALLWAY_PLUM_START, 0, 100);

        // spaces.add(new GooeySpace(boardPane, "game", 300, 30, 0));

        /*// Instantiating the Light.Spot class
        Light.Spot light = new Light.Spot();

        // Setting the color of the light
        light.setColor(Color.GRAY);
        // light.setSpecularExponent(2);

        // setting the position of the light
        light.setX(200);
        light.setY(200);
        light.setZ(40);
        light.setPointsAtX(200);
        light.setPointsAtX(200);
        light.setPointsAtZ(0);

        // Instantiating the Lighting class
        Lighting lighting = new Lighting();

        // Setting the light source
        lighting.setLight(light);
        lighting.setSurfaceScale(-3.0);

        //boardPane.setEffect(lighting);*/

        boardOverlay.setImage(imgByName.get("overlay"));
        cluelessLogo.setImage(imgByName.get("clueless"));

        Label myCardsLabel = new Label("My Cards");
        myCardPane.getChildren().add(myCardsLabel);

        Label otherCardsLabel = new Label("Face Up Cards");
        otherCardPane.getChildren().add(otherCardsLabel);

        Label actionLabel = new Label("Actions");
        actionPane.getChildren().add(actionLabel);

        suspectField.setEditable(false);
        Tooltip ttp = new Tooltip();
        ttp.setText("Your suspect");
        suspectField.setTooltip(ttp);

        addToLogList("Type 'exit' or 'quit' to return to shell.\n" + "Type 'help' for more info.");
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

        img = new Image(prefix + "overlay.png", 400, 400, false, false);
        imgByName.put("overlay", img);

        img = new Image(prefix + "kitchen.png", 100, 100, false, false);
        imgByName.put("kitchen", img);
        img = new Image(prefix + "ballroom.png", 100, 100, false, false);
        imgByName.put("ballroom", img);
        img = new Image(prefix + "conservatory.png", 100, 100, false, false);
        imgByName.put("conservatory", img);
        img = new Image(prefix + "library.png", 100, 100, false, false);
        imgByName.put("library", img);
        img = new Image(prefix + "hall.png", 100, 100, false, false);
        imgByName.put("hall", img);
        img = new Image(prefix + "study.png", 100, 100, false, false);
        imgByName.put("study", img);
        img = new Image(prefix + "dining.png", 100, 100, false, false);
        imgByName.put("dining", img);
        img = new Image(prefix + "lounge.png", 100, 100, false, false);
        imgByName.put("lounge", img);
        img = new Image(prefix + "billiards.png", 100, 100, false, false);
        imgByName.put("billiards", img);

        img = new Image(prefix + "clueless.png", 162, 39, false, false);
        imgByName.put("clueless", img);
    }

    public GooeyScene() {
        logArray = new ArrayList<>();

        loadImages();

        spaces = new HashMap<>();

        myCards = new HashMap<Integer, GooeyCard>();
        otherCards = new HashMap<Integer, GooeyCard>();

        clientState = new ClientState();
        startup("127.0.0.1", "2323");
    }

    public void startup(String addr, String port) {
        EventHandler evtHdlr = new GooeyEventHandler(clientState, this);
        clientState.startup(evtHdlr, addr, port);
    }

    @FXML
    private void handleConfig(ActionEvent event) {
        Dialog<Configuration> dialog = new Dialog<>();
        dialog.setTitle("Configuration");
        dialog.setHeaderText("Pick a username and suspect!");
        dialog.setResizable(true);

        Label usernameLabel = new Label("Username: ");
        Label suspectLabel = new Label("Suspect: ");

        TextField usernameText = new TextField();

        ChoiceBox<SuspectCard> suspectChoice = new ChoiceBox<SuspectCard>();
        for (SuspectCard card : clientState.getAvailableSuspects().list) {
            suspectChoice.getItems().add(card);
        }
        suspectChoice.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();

        grid.add(usernameLabel, 1, 1);
        grid.add(usernameText, 2, 1);

        grid.add(suspectLabel, 1, 2);
        grid.add(suspectChoice, 2, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter(
                new Callback<ButtonType, Configuration>() {
                    @Override
                    public Configuration call(ButtonType b) {
                        if (b == buttonTypeOk) {
                            Configuration config =
                                    new Configuration(
                                            suspectChoice.getValue(), usernameText.getText());
                            return config;
                        } else {
                            return null;
                        }
                    }
                });

        Optional<Configuration> result = dialog.showAndWait();
        if (result.isPresent()) {
            Configuration config = result.get();
            String cmd = "config " + config.getSuspectCard().getName() + " " + config.getUsername();
            addToLogList(cmd);
            Message msg = ClientCommand.processCommand(clientState, cmd);
            handleInternalMessage(msg);
        }
    }
}
