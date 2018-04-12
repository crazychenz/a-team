/** */
package clueless.io;

/**
 * Enumeration of all possible MessageTypes
 *
 * <p>TODO: Embed this into Message class
 *
 * @author ateam
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
    MESSAGE_SERVER_START_GAME(0x5A),
    MESSAGE_CLIENT_SUGGEST(0x5B),
    MESSAGE_CLIENT_ACCUSE(0x5C),
    MESSAGE_SERVER_FAIL_CONFIG(0x5D),
    MESSAGE_CLIENT_END_TURN(0x5E),
    MESSAGE_SERVER_FAIL_MOVE(0x5F),
    MESSAGE_SERVER_RELAY_SUGGEST(0x60),
    MESSAGE_CLIENT_RESPONSE_SUGGEST(0x61),
    MESSAGE_SERVER_RESPONSE_SUGGEST(0x62),
    MESSAGE_INFO(0x6E),
    MESSAGE_ERROR(0x6F),
    MESSAGE_INTERNAL_SERVER_END_GAME(0x63),
    MESSAGE_INTERNAL_SERVER_REMOVE_PLAYER(0x64);

    private final int messageID;

    MessagesEnum(int messageID) {
        this.messageID = messageID;
    }

    /** @return the messageID */
    public int getMessageID() {
        return messageID;
    }
}
