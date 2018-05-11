package clueless.client.cli;

import clueless.Card;
import clueless.GameStatePulse;
import clueless.Suggestion;
import clueless.client.*;
import clueless.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CLIEventHandler extends EventHandler {

    private static final Logger logger = LogManager.getLogger(CLIEventHandler.class);

    ClientState clientState;

    CLIEventHandler(ClientState state) {
        clientState = state;
    }

    @Override
    public void onMessageEvent(Client client, Message msg) {
        switch (msg.getMessageID()) {
            case MESSAGE_CHAT_FROM_SERVER:
            case MESSAGE_CHAT_FROM_CLIENT:
                logger.info("chat: " + msg.getMessageData());
                break;
            case MESSAGE_PULSE:
                logger.trace("Got a watchdog pulse.");
                GameStatePulse gameState = (GameStatePulse) msg.getMessageData();
                String statusStr = clientState.setGameState(gameState);
                if (statusStr != null) {
                    System.out.println(statusStr);
                }
                clientState.pulse();
                break;
            case MESSAGE_SERVER_FAIL_CONFIG:
                logger.info(msg);
                clientState.setConfigured(false);
                clientState.setMySuspect(null);
                break;
            case MESSAGE_SERVER_FAIL_MOVE:
                logger.info(msg);
                clientState.setMoved(false);
                break;
            case MESSAGE_SERVER_RELAY_SUGGEST:
                // TODO prompt the user that they need to disprove this suggestion if possible
                // They should only be given a choice of the cards they own that are in the
                // suggestion
                // If they own none of the cards, I think we should just tell them that, and they
                // have to acknowledge
                // It doesn't seem interactive enough to do it automagically
                logger.info(msg);
                clientState.disprove(
                        (Suggestion) msg.getMessageData(),
                        msg.getToUuid().equals(client.uuid.toString()));
                break;
            case MESSAGE_SERVER_RESPONSE_SUGGEST:
                logger.info(msg);
                String resp =
                        clientState.suggestResponse(
                                (Card) msg.getMessageData(),
                                msg.getToUuid().equals(client.uuid.toString()));
                logger.info(resp);
                break;
            default:
                logger.info("Message: " + msg);
                break;
        }
    }
}
