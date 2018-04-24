package clueless.client.gooey;

import clueless.*;
import clueless.client.*;
import clueless.io.*;
import java.util.Map;
import javafx.application.Platform;
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
                                scene.addToLogList("chat: " + msg.asString());
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
                clientState.disprove(
                        (Suggestion) msg.getMessageData(),
                        msg.getToUuid().equals(client.uuid.toString()));
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
