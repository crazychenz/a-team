/** */
package clueless.io;

/**
 * Enumeration of all possible MessageTypes
 *
 * @author ateam
 * @todo Embed this into Message class
 */
public enum MessagesEnum {
    MESSAGE_PULSE(0x50),
    MESSAGE_CLIENT_START_GAME(0x51),
    MESSAGE_CHAT_FROM_CLIENT(0x52),
    MESSAGE_CHAT_FROM_SERVER(0x53),
    MESSAGE_CARD_FROM_SERVER(0x54),
    MESSAGE_CLIENT_CONFIG(0x55),
    MESSAGE_SERVER_HEARTBEAT(0x56),
    MESSAGE_SERVER_AVAILABLE_SUSPECTS(0x57),
    MESSAGE_CLIENT_MOVE(0x58),
    MESSAGE_CLIENT_CONNECTED(0x59),
    MESSAGE_SERVER_START_GAME(0x60),
    MESSAGE_CLIENT_SUGGEST(0x61),
    MESSAGE_CLIENT_ACCUSE(0x62),
    MESSAGE_SERVER_FAIL_CONFIG(0x63),
    MESSAGE_CLIENT_END_TURN(0x64),
    MESSAGE_SERVER_FAIL_MOVE(0x65),
    MESSAGE_SERVER_RELAY_SUGGEST(0x66),
    MESSAGE_CLIENT_RESPONSE_SUGGEST(0x67),
    MESSAGE_SERVER_RESPONSE_SUGGEST(0x68);

    private final int messageID;

    MessagesEnum(int messageID) {
        this.messageID = messageID;
    }

    /** @return the messageID */
    public int getMessageID() {
        return messageID;
    }
}
