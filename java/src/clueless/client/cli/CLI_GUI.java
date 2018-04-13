package clueless.client.cli;

// import clueless.DirectionsEnum;
// import clueless.RoomCard;
// import clueless.SuspectCard;
// import clueless.WeaponCard;
// import clueless.client.cli.CLI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CLI_GUI extends Application {
    // Declaring the TextArea for Logging
    TextArea logging;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create the TextArea
        logging = new TextArea();
        logging.setMaxWidth(370);
        logging.setMaxHeight(300);

        // Create the Labels
        Label suspectsLbl = new Label("Select Suspect: ");
        Label locationsLbl = new Label("Select Location: ");
        Label weaponsLbl = new Label("Select Weapon: ");
        Label directionsLbl = new Label("Select Direction: ");
        Label othersLbl = new Label("Select: ");

        // Create the Lists for the ListViews
        ObservableList<String> suspectsList =
                FXCollections.<String>observableArrayList(
                        "Green", "Mustard", "Peacock", "Plum", "Scarlet", "White");
        ObservableList<String> locationsList =
                FXCollections.<String>observableArrayList(
                        "Ballroom",
                        "Billiard",
                        "Conservatory",
                        "Dining",
                        "Hall",
                        "Kitchen",
                        "Library",
                        "Lounge",
                        "Study");
        ObservableList<String> weaponsList =
                FXCollections.<String>observableArrayList(
                        "Revolver", "Pipe", "Rope", "Candlestick", "Wrench", "Dagger");
        ObservableList<String> directionsList =
                FXCollections.<String>observableArrayList("north", "south", "east", "west");
        ObservableList<String> othersList =
                FXCollections.<String>observableArrayList(
                        "exit", "quit", "help", "start", "done", "chat", "cards", "board",
                        "diprove");

        // Create the ListView for the suspects
        ListView<String> suspects = new ListView<>(suspectsList);
        // Set the Orientation of the ListView
        suspects.setOrientation(Orientation.VERTICAL);
        // Set the Size of the ListView
        suspects.setPrefSize(120, 100);

        // Update the TextArea when the selected suspects changes
        suspects.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            public void changed(
                                    ObservableValue<? extends String> ov,
                                    final String oldvalue,
                                    final String newvalue) {
                                suspectsChanged(ov, oldvalue, newvalue);
                            }
                        });

        // Create the ListView for the locations
        ListView<String> locations = new ListView<String>();
        // Set the Orientation of the ListView
        locations.setOrientation(Orientation.VERTICAL);
        // Set the Size of the ListView
        locations.setPrefSize(120, 100);
        // Add the items to the ListView
        locations.getItems().addAll(locationsList);

        // Update the message Label when the selected locations changes
        locations
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            public void changed(
                                    ObservableValue<? extends String> ov,
                                    final String oldvalue,
                                    final String newvalue) {
                                locationsChanged(ov, oldvalue, newvalue);
                            }
                        });

        // Create the ListView for the weapons
        ListView<String> weapons = new ListView<>(weaponsList);
        // Set the Orientation of the ListView
        weapons.setOrientation(Orientation.VERTICAL);
        // Set the Size of the ListView
        weapons.setPrefSize(120, 100);

        // Update the TextArea when the selected weapons changes
        weapons.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            public void changed(
                                    ObservableValue<? extends String> ov,
                                    final String oldvalue,
                                    final String newvalue) {
                                weaponsChanged(ov, oldvalue, newvalue);
                            }
                        });

        // Create the ListView for the directions
        ListView<String> directions = new ListView<String>();
        // Set the Orientation of the ListView
        directions.setOrientation(Orientation.VERTICAL);
        // Set the Size of the ListView
        directions.setPrefSize(120, 100);
        // Add the items to the ListView
        directions.getItems().addAll(directionsList);

        // Update the message Label when the selected direction changes
        directions
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            public void changed(
                                    ObservableValue<? extends String> ov,
                                    final String oldvalue,
                                    final String newvalue) {
                                directionsChanged(ov, oldvalue, newvalue);
                            }
                        });

        // Create the ListView for others
        ListView<String> others = new ListView<String>();
        // Set the Orientation of the ListView
        others.setOrientation(Orientation.HORIZONTAL);
        // Set the Size of the ListView
        others.setPrefSize(200, 100);
        // Add the items to the ListView
        others.getItems().addAll(othersList);

        // Update the message Label when the selected others changes
        others.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            public void changed(
                                    ObservableValue<? extends String> ov,
                                    final String oldvalue,
                                    final String newvalue) {
                                othersChanged(ov, oldvalue, newvalue);
                            }
                        });

        // Create the Suspect VBox
        VBox suspectsSelection = new VBox();
        // Set Spacing to 10 pixels
        suspectsSelection.setSpacing(10);
        // Add the Label and the List to the VBox
        suspectsSelection.getChildren().addAll(suspectsLbl, suspects);

        // Create the Location VBox
        VBox locationsSelection = new VBox();
        // Set Spacing to 10 pixels
        locationsSelection.setSpacing(10);
        // Add the Label and the List to the VBox
        locationsSelection.getChildren().addAll(locationsLbl, locations);

        // Create the Weapon VBox
        VBox weaponsSelection = new VBox();
        // Set Spacing to 10 pixels
        weaponsSelection.setSpacing(10);
        // Add the Label and the List to the VBox
        weaponsSelection.getChildren().addAll(weaponsLbl, weapons);

        // Create the Direction VBox
        VBox directionsSelection = new VBox();
        // Set Spacing to 10 pixels
        directionsSelection.setSpacing(10);
        // Add the Label and the List to the VBox
        directionsSelection.getChildren().addAll(directionsLbl, directions);

        // Create the Other VBox
        VBox othersSelection = new VBox();
        // Set Spacing to 10 pixels
        othersSelection.setSpacing(10);
        // Add the Label and the List to the VBox
        othersSelection.getChildren().addAll(othersLbl, others);

        // Create the GridPane
        GridPane pane = new GridPane();
        // Set the horizontal and vertical gaps between children
        pane.setHgap(10);
        pane.setVgap(5);
        // Add the Suspect List at position 0
        pane.addColumn(0, suspectsSelection);
        // Add the Location List at position 1
        pane.addColumn(1, locationsSelection);
        // Add the Weapon List at position 0
        pane.addColumn(0, weaponsSelection);
        // Add the Direction List at position 1
        pane.addColumn(1, directionsSelection);
        // Add the TextArea at position 2
        pane.addColumn(2, logging);
        // Add the Others List at position 1
        pane.addColumn(2, othersSelection);

        // Set the Style-properties of the GridPane
        pane.setStyle(
                "-fx-padding: 10;"
                        + "-fx-border-style: solid inside;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-insets: 5;"
                        + "-fx-border-radius: 5;"
                        + "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(pane);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title
        stage.setTitle("CLUE_LESS");
        // Display the Stage
        stage.show();
    }

    // Method to display the Suspect, which has been changed
    public void suspectsChanged(
            ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        logging.appendText("Suspect changed: old = " + oldText + ", new = " + newText + "\n");
    }

    // Method to display the Location, which has been changed
    public void locationsChanged(
            ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        logging.appendText("Location changed: old = " + oldText + ", new = " + newText + "\n");
    }

    // Method to display the Weapon, which has been changed
    public void weaponsChanged(
            ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        logging.appendText("Weapon changed: old = " + oldText + ", new = " + newText + "\n");
    }

    // Method to display the Direction, which has been changed
    public void directionsChanged(
            ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        logging.appendText("Direction changed: old = " + oldText + ", new = " + newText + "\n");
    }

    // Method to display the Other, which has been changed
    public void othersChanged(
            ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        logging.appendText("Other changed: old = " + oldText + ", new = " + newText + "\n");
    }
}
