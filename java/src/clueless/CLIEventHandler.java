package clueless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CLIEventHandler {

    private static final Logger logger = LogManager.getLogger(CLIEventHandler.class);

    ClientState clientState;
    Watchdog watchdog;

    CLIEventHandler(ClientState state, Watchdog wd) {
        clientState = state;
        watchdog = wd;
    }

    void onMessageEvent(Client client, Message msg) {
        switch (msg.getMessageID()) {
            case MESSAGE_CHAT_FROM_SERVER:
            case MESSAGE_CHAT_FROM_CLIENT:
                logger.info("chat: " + msg.getMessageData());
                break;
                /*case MESSAGE_SERVER_AVAILABLE_SUSPECTS:
                clientState.setAvailableSuspects((AvailableSuspects) msg.getMessageData());
                logger.info("Count: " + clientState.getAvailableSuspects().list.size());
                for (CardsEnum suspect : clientState.getAvailableSuspects().list) {
                    logger.info(suspect);
                }
                break;*/
            case MESSAGE_PULSE:
                logger.trace("Got a watchdog pulse.");
                GameStatePulse gameState = (GameStatePulse) msg.getMessageData();
                clientState.setGameState(gameState);
                watchdog.pulse();
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
                //TODO prompt the user that they need to disprove this suggestion if possible
                //They should only be given a choice of the cards they own that are in the suggestion
                //If they own none of the cards, I think we should just tell them that, and they have to acknowledge
                //It doesn't seem interactive enough to do it automagically
                break;
            default:
                logger.info("Message: " + msg);
                break;
        }
    }
}
