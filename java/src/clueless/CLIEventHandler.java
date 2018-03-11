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
                logger.info("chat: " + msg);
                break;
            case MESSAGE_SERVER_AVAILABLE_SUSPECTS:
                AvailableSuspects suspects;
                suspects = (AvailableSuspects) msg.getMessageData();
                logger.info("Count: " + suspects.list.size());
                for (CardsEnum suspect : suspects.list) {
                    logger.info(suspect);
                }
                break;
            case MESSAGE_PULSE:
                // BUG: log4j is printing \n when trace is disabled.
                //logger.trace("Got a watchdog pulse.");
                watchdog.pulse();
                break;
            default:
                logger.info("Message: " + msg);
                break;
        }
    }

}
