/** */
package clueless.io;

import clueless.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for managing serialization to and from buffers for Message objects.
 *
 * @author ateam
 */
public class Message implements Serializable {

    private static final Logger logger = LogManager.getLogger(Message.class);

    private final MessagesEnum messageID;
    private final Object messageData;
    private String fromUuid;
    private boolean broadcast;
    private String toUuid;

    /**
     * Client-side constructor
     *
     * @param messageID Enumeration of message type
     * @param messageData Serializable object of message
     */
    public Message(MessagesEnum messageID, Object messageData) {
        this.messageID = messageID;
        this.messageData = messageData;
        fromUuid = null;
        broadcast = false;
        toUuid = "";
    }

    /**
     * Server-side constructor
     *
     * @param messageID Enumeration of message type
     * @param messageData Serializable object of message
     * @param toUuid Destination Client by UUID
     */
    public Message(MessagesEnum messageID, Object messageData, String toUuid) {
        this.messageID = messageID;
        this.messageData = messageData;
        fromUuid = null;
        broadcast = false;
        this.toUuid = toUuid;
    }

    // TODO add a setSingle or something, to be able to send a message to a single client by
    // specifying their uuid

    /**
     * Set the broadcast bit for the Message (server-side)
     *
     * @param value true is broadcast message, false if unicast message
     */
    public void setBroadcast(boolean value) {
        broadcast = value;
    }

    /**
     * Check if the Message is a broadcast message
     *
     * @return true if broadcast message, false if unicast message
     */
    public boolean isBroadcast() {
        return broadcast;
    }

    /**
     * Get the client UUID message is from. (server-side)
     *
     * @return UUID of client source
     */
    public String getFromUuid() {
        return fromUuid;
    }

    /**
     * Set the client UUID for message (client-side)
     *
     * @param uuid UUID of client
     */
    public void setFromUuid(String uuid) {
        fromUuid = uuid;
    }

    /**
     * Get the client UUID of message
     *
     * @return UUID of client
     */
    public String getToUuid() {
        return toUuid;
    }

    /**
     * Set the destination client UUID (server-side)
     *
     * @param uuid client UUID
     */
    public void setToUuid(String uuid) {
        toUuid = uuid;
    }

    @Override
    public String toString() {
        return "Message ID: " + messageID + " Message Data: " + messageData;
    }

    /**
     * Get the message enumeration ID
     *
     * @return MessagesEnum
     */
    public MessagesEnum getMessageID() {
        return messageID;
    }

    /**
     * Get the object from message
     *
     * @return Object
     */
    public Object getMessageData() {
        return messageData;
    }

    /**
     * Get the object from message as a String
     *
     * @return String
     */
    public String asString() {
        return (String) messageData;
    }

    /**
     * Get the object from message as a SuspectCard
     *
     * @return SuspectCard
     */
    public Configuration asConfigData() {
        return (Configuration) messageData;
    }

    /**
     * Get the object from message as a DirectionsEnum
     *
     * @return DirectionsEnum
     */
    public DirectionsEnum asDirectionsEnum() {
        return (DirectionsEnum) messageData;
    }

    /**
     * Get the object from message as a Suggestion
     *
     * @return Suggestion
     */
    public Suggestion asSuggestion() {
        Suggestion suggestion = (Suggestion) messageData;
        if (suggestion != null) {
            suggestion.normalize();
        }
        return suggestion;
    }

    /**
     * Get the object from message as a Card
     *
     * @return Card
     */
    public Card asCard() {
        Card card = (Card) messageData;
        if (card != null) {
            // Normalize
            return Card.fetch(card.getId());
        }
        return card;
    }

    /**
     * Get the object from message as a Player
     *
     * @return Player
     */
    public Player asPlayer() {
        return (Player) messageData;
    }

    /**
     * Serialize a Message object to a ByteBuffer
     *
     * @param msg Message object to serialize
     * @return Serialized message in ByteBuffer
     * @throws Exception if serialization fails
     */
    public static ByteBuffer toBuffer(Message msg) throws Exception {
        ByteBufferBackedOutputStream bbos;
        ObjectOutputStream oos;
        ByteBuffer buf;

        buf = ByteBuffer.allocate(4096);
        bbos = new ByteBufferBackedOutputStream(buf);

        try {
            oos = new ObjectOutputStream(bbos);
        } catch (Exception e) {
            logger.error(e);
            bbos.close();
            throw e;
        }
        oos.writeObject(msg);
        buf.flip();
        oos.close();
        return buf;
    }

    /**
     * Parse a Message object from a ByteBuffer
     *
     * @param buf Serialized message to parse
     * @return Parsed message object
     * @throws Exception if parsing goes bad
     */
    public static Message fromBuffer(ByteBuffer buf) throws Exception {
        ByteBufferBackedInputStream bbis;
        ObjectInputStream ois;
        Message message;

        bbis = new ByteBufferBackedInputStream(buf);
        try {
            ois = new ObjectInputStream(bbis);
        } catch (Exception e) {
            logger.error(e);
            bbis.close();
            throw e;
        }
        message = (Message) ois.readObject();
        ois.close();
        return message;
    }

    /**
     * Generate clientConnect message
     *
     * @return clientConnect message
     */
    public static Message clientConnect() {
        logger.trace("Connecting to Game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED, "");
    }

    /**
     * Generate startGame message
     *
     * @return startGame message
     */
    public static Message startGame() {
        logger.trace("Client wants to start the game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_START_GAME, "");
    }

    /**
     * Generate moveClient message (client-side)
     *
     * @param direction Direction the client is moving
     * @return moveClient message
     */
    public static Message moveClient(DirectionsEnum direction) {
        logger.trace("Client is moving");
        return new Message(MessagesEnum.MESSAGE_CLIENT_MOVE, direction);
    }

    /**
     * Generate clientConfig message (client-side)
     *
     * @param suspect SuspectCard client would like to register
     * @return clientConfig message
     */
    public static Message clientConfig(SuspectCard suspect, String username) {
        logger.trace("Configuring client");
        return new Message(
                MessagesEnum.MESSAGE_CLIENT_CONFIG, new Configuration(suspect, username));
    }

    /**
     * Generate chatMessage message
     *
     * @param chatMessage String message to send to all clients
     * @return chatMessage message
     */
    public static Message chatMessage(String chatMessage) {
        logger.trace("Sending chat to others");
        return new Message(MessagesEnum.MESSAGE_CHAT_FROM_CLIENT, chatMessage);
    }

    /**
     * Generate winMessage message
     *
     * @param message Winning suspect
     * @return winMessage message
     */
    public static Message serverMessage(String message) {
        return new Message(MessagesEnum.MESSAGE_CHAT_FROM_SERVER, message);
    }

    /**
     * Generate endTurn message
     *
     * @return endTurn message
     */
    public static Message endTurn() {
        logger.trace("Ending turn");
        return new Message(MessagesEnum.MESSAGE_CLIENT_END_TURN, "");
    }

    /**
     * Generate clientPulse message (client-side)
     *
     * @return clientPulse message
     */
    public static Message clientPulse() {
        logger.trace("Sending watchdog pulse");
        return new Message(MessagesEnum.MESSAGE_PULSE, "");
    }

    /**
     * Generate accusation message (client-side)
     *
     * @param suggestion Suggestion object of accusation
     * @return accusation message
     */
    public static Message accusation(Suggestion suggestion) {
        return new Message(MessagesEnum.MESSAGE_CLIENT_ACCUSE, suggestion);
    }

    /**
     * Generate suggestion message (client-side)
     *
     * @param suggestion Suggestion object of suggestion sequence
     * @return suggestion message
     */
    public static Message suggestion(Suggestion suggestion) {
        return new Message(MessagesEnum.MESSAGE_CLIENT_SUGGEST, suggestion);
    }

    /**
     * Generate failedConfig message (server-side)
     *
     * <p>TODO: This should be more structured
     *
     * @param message Failed config description to client
     * @return failedConfig message
     */
    public static Message failedConfig(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_CONFIG, message);
    }

    /**
     * Generate failedMove message (server-side)
     *
     * @param message Failed move description to client
     * @return failedMove message
     */
    public static Message failedMove(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_MOVE, message);
    }

    /**
     * Generate relaySuggestion message (server-side)
     *
     * @param cards Suggestion being disproven
     * @param playerToDisprove Player object to disprove
     * @return Message object
     */
    public static Message relaySuggestion(Suggestion cards, Player playerToDisprove) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RELAY_SUGGEST, cards, playerToDisprove.uuid);
    }

    /**
     * Generate sendGameStatePulse message (server-side)
     *
     * <p>TODO: Duplicate of serverPulse?
     *
     * @param gsp GameStatePulse payload to send to client
     * @return sendGameStatePulse message
     */
    public static Message sendGameStatePulse(GameStatePulse gsp) {
        return new Message(MessagesEnum.MESSAGE_PULSE, gsp);
    }

    /**
     * Generate clientRespondSuggestion message (client-side)
     *
     * @param card Card used to disprove suggestion, or null for no disprove
     * @return clientRespondSuggestion message
     */
    public static Message clientRespondSuggestion(Card card) {
        return new Message(MessagesEnum.MESSAGE_CLIENT_RESPONSE_SUGGEST, card);
    }

    /**
     * Generate serverRespondSuggestion message (server-side)
     *
     * @param card Card used to disprove suggestion, or null for no disprove
     * @return serverRespondSuggestion message
     */
    public static Message serverRespondSuggestion(Card card) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RESPONSE_SUGGEST, card);
    }

    /**
     * Generate info message
     *
     * @param infostr String to give back as info
     * @return info message
     */
    public static Message info(String infostr) {
        return new Message(MessagesEnum.MESSAGE_INFO, infostr);
    }

    /**
     * Generate error message
     *
     * @param errorstr String to give back as error
     * @return error message
     */
    public static Message error(String errorstr) {
        return new Message(MessagesEnum.MESSAGE_ERROR, errorstr);
    }

    /**
     * This message lets the Game know that a client has disconnected
     *
     * @param player The player object of the client who left
     * @return internal message
     */
    public static Message internalRemovePlayer(Player player) {
        return new Message(MessagesEnum.MESSAGE_INTERNAL_SERVER_REMOVE_PLAYER, player);
    }

    /**
     * This message tells the Game to end
     *
     * @return internal message
     */
    public static Message internalGameEnd() {
        return new Message(MessagesEnum.MESSAGE_INTERNAL_SERVER_END_GAME, "");
    }
}
