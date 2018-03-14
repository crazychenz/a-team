package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CLIEventHandler {

    private static final Logger logger
            = LogManager.getLogger(CLIEventHandler.class);

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
                logger.info("chat: " + msg);
                break;
            /*case MESSAGE_SERVER_AVAILABLE_SUSPECTS:
                clientState.setAvailableSuspects((AvailableSuspects) msg.getMessageData());
                logger.info("Count: " + clientState.getAvailableSuspects().list.size());
                for (CardsEnum suspect : clientState.getAvailableSuspects().list) {
                    logger.info(suspect);
                }
                break;*/
            case MESSAGE_PULSE:
                // BUG: log4j is printing \n when trace is disabled.
                //logger.trace("Got a watchdog pulse.");
            	GameStatePulse gameState = (GameStatePulse)msg.getMessageData();
            	clientState.setGameState(gameState);
                watchdog.pulse();
                break;
            case MESSAGE_SERVER_FAIL_ACTION:
            	logger.info(msg);
            	break;
            default:
                logger.info("Message: " + msg);
                break;
        }
    }

}
