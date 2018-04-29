package clueless.client.gooey;

import clueless.*;
import clueless.client.*;
import clueless.io.*;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GooeyEventHandler extends EventHandler {

    private static final Logger logger = LogManager.getLogger(GooeyEventHandler.class);

    ClientState clientState;
    GooeyScene scene;
    boolean alerted;

    GooeyEventHandler(ClientState state, GooeyScene scene) {
        clientState = state;
        this.scene = scene;
        alerted = false;
    }

    private void handleDisprove(Suggestion suggestion, boolean active) {
        if (active) {
            clientState.setDisproving(true);
            handleLocalDisprove(suggestion);
            return;
        }

        scene.addToLogList("Suggestion " + suggestion + " being disproved.");
    }

    private void handleLocalDisprove(Suggestion suggestion) {

        // Cherry pick the relevant cards to disapprove with.
        boolean found = false;
        ChoiceBox cardChoice = new ChoiceBox();
        for (Card card : clientState.getCards()) {
            if (suggestion.contains(card)) {
                found = true;
                clientState.disproveCards.add(card);
                cardChoice.getItems().add(card);
            }
        }
        cardChoice.getSelectionModel().selectFirst();

        Dialog<Card> dialog = new Dialog<>();
        dialog.setTitle("Disprove Dialog");
        dialog.setHeaderText("Disprove suggestion with card in hand.");
        dialog.setResizable(true);

        Label cardLabel = new Label("Cards: ");

        GridPane grid = new GridPane();
        grid.add(cardLabel, 1, 1);
        grid.add(cardChoice, 2, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeDisprove = new ButtonType("Disprove", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeDisprove);

        dialog.setResultConverter(
                new Callback<ButtonType, Card>() {
                    @Override
                    public Card call(ButtonType b) {
                        return (Card) cardChoice.getValue();
                    }
                });

        Bounds bounds = scene.boardPane.localToScreen(scene.boardPane.getBoundsInLocal());
        dialog.setX(bounds.getMinX());
        dialog.setY(bounds.getMinY());

        Optional<Card> result = dialog.showAndWait();
        if (result.isPresent()) {
            Card card = (Card) result.get();
            String cmd = "disprove " + card.getName();
            scene.addToLogList(cmd);
            Message msg = ClientCommand.processCommand(clientState, cmd);
            scene.handleInternalMessage(msg);
        } else {
            String cmd = "disprove";
            scene.addToLogList(cmd);
            Message msg = ClientCommand.processCommand(clientState, cmd);
            scene.handleInternalMessage(msg);
        }
    }

    @Override
    public void onMessageEvent(Client client, Message msg) {
        switch (msg.getMessageID()) {
            case MESSAGE_CHAT_FROM_SERVER:
            case MESSAGE_CHAT_FROM_CLIENT:
                logger.info("chat: " + msg.asString());

                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                scene.addToLogList("(chat) " + msg.asString());
                            }
                        });

                break;
            case MESSAGE_PULSE:
                logger.trace("Got a watchdog pulse.");
                GameStatePulse gameState = (GameStatePulse) msg.getMessageData();
                String statusStr = clientState.setGameState(gameState);
                clientState.pulse();

                // Add output to log list
                if (statusStr != null) {
                    Platform.runLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    scene.addToLogList(statusStr);
                                }
                            });
                }

                // Update the visual board state.
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {

                                // Clear out all the GooeySpace pieces.
                                for (Map.Entry<Integer, GooeySpace> entry :
                                        scene.spaces.entrySet()) {
                                    entry.getValue().clearPieces();
                                }

                                // Add the GooeySpace suspect pieces back.
                                for (Map.Entry<SuspectCard, Integer> entry :
                                        gameState.getSuspectLocations().entrySet()) {
                                    SuspectCard suspectCard = entry.getKey();
                                    Integer locationId = entry.getValue();

                                    // Get the GooeySpace mapped to locationId
                                    GooeySpace space = scene.spaces.get(locationId);
                                    space.addSuspect(suspectCard);
                                }

                                // Add the GooeySpace weapon pieces back.
                                for (Map.Entry<WeaponCard, Integer> entry :
                                        gameState.getWeaponLocations().entrySet()) {
                                    WeaponCard card = entry.getKey();
                                    Integer locationId = entry.getValue();

                                    // Get the GooeySpace mapped to locationId
                                    GooeySpace space = scene.spaces.get(locationId);
                                    space.addWeapon(card);
                                }
                                if (gameState.isGameActive()) {
                                    scene.clearCards();
                                    // Populate the face up cards and my cards
                                    for (Card card : gameState.getFaceUpCards()) {
                                        scene.addOtherCard(card);
                                    }

                                    for (Card card : gameState.getCards()) {
                                        scene.addMyCard(card);
                                    }
                                }

                                if (clientState.isConfigured()) {
                                    scene.suspectField.setText(
                                            clientState.getMySuspect().getName());
                                }
                            }
                        });

                break;
            case MESSAGE_SERVER_FAIL_CONFIG:
                logger.info(msg);
                clientState.setConfigured(false);
                clientState.setMySuspect(null);
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                scene.addToLogList("info: " + msg.asString());
                            }
                        });
                break;
            case MESSAGE_SERVER_FAIL_MOVE:
                logger.info(msg);
                clientState.setMoved(false);
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                scene.addToLogList("info: " + msg.asString());
                            }
                        });
                break;
            case MESSAGE_SERVER_RELAY_SUGGEST:
                logger.info(msg);
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                handleDisprove(
                                        (Suggestion) msg.getMessageData(),
                                        msg.getToUuid().equals(client.uuid.toString()));
                            }
                        });
                // clientState.disprove(
                //        (Suggestion) msg.getMessageData(),
                //        msg.getToUuid().equals(client.uuid.toString()));
                break;
            case MESSAGE_SERVER_RESPONSE_SUGGEST:
                logger.info(msg);
                String response =
                        clientState.suggestResponse(
                                (Card) msg.getMessageData(),
                                msg.getToUuid().equals(client.uuid.toString()));
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                scene.addToLogList("info: " + response);
                            }
                        });
                break;
            default:
                logger.info("Message: " + msg);
                break;
        }
    }
}
