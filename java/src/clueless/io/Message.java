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
	 * @param value true is broadcast message, false if unicast message
	 */
    public void setBroadcast(boolean value) {
        broadcast = value;
    }

	/**
	 * Check if the Message is a broadcast message
	 * @return true if broadcast message, false if unicast message
	 */
	public boolean isBroadcast() {
        return broadcast;
    }

	/**
	 * Get the client UUID message is from. (server-side)
	 * @return UUID of client source
	 */
	public String getFromUuid() {
        return fromUuid;
    }

	/**
	 * Set the client UUID for message (client-side) 
	 * @param uuid UUID of client
	 */
	public void setFromUuid(String uuid) {
        fromUuid = uuid;
    }

	/**
	 * Get the client UUID of message
	 * @return UUID of client
	 */
	public String getToUuid() {
        return toUuid;
    }

	/**
	 * Set the destination client UUID (server-side)
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
	 * @return MessagesEnum
	 */
	public MessagesEnum getMessageID() {
        return messageID;
    }

	/**
	 * Get the object from message
	 * @return Object
	 */
	public Object getMessageData() {
        return messageData;
    }

	/**
	 * Serialize a Message object to a ByteBuffer
	 * @param msg
	 * @return
	 * @throws Exception
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
	 * @param buf
	 * @return
	 * @throws Exception
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
	 * @return clientConnect message
	 * @throws Exception
	 */
	public static Message clientConnect() throws Exception {
        logger.trace("Connecting to Game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED, "");
    }

	/**
	 * Generate startGame message
	 * @return startGame message
	 * @throws Exception
	 */
	public static Message startGame() throws Exception {
        logger.trace("Client wants to start the game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_START_GAME, "");
    }

	/**
	 * Generate moveClient message (client-side)
	 * @param direction Direction the client is moving
	 * @return moveClient message
	 * @throws Exception
	 */
	public static Message moveClient(DirectionsEnum direction) throws Exception {
        logger.trace("Client is moving");
        return new Message(MessagesEnum.MESSAGE_CLIENT_MOVE, direction);
    }

	/**
	 * Generate clientConfig message (client-side)
	 * @param suspect SuspectCard client would like to register
	 * @return clientConfig message
	 * @throws Exception
	 */
	public static Message clientConfig(SuspectCard suspect) throws Exception {
        logger.trace("Configuring client");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONFIG, suspect);
    }

	/**
	 * Generate chatMessage message
	 * @param chatMessage String message to send to all clients
	 * @return chatMessage message
	 * @throws Exception
	 */
	public static Message chatMessage(String chatMessage) throws Exception {
        logger.trace("Sending chat to others");
        return new Message(MessagesEnum.MESSAGE_CHAT_FROM_CLIENT, chatMessage);
    }

	/**
	 * Generate winMessage message
	 * @param winningSuspect Winning suspect
	 * @return winMessage message
	 * @todo Perhaps a client ID or username would be better?
	 */
	public static Message winMessage(SuspectCard winningSuspect) {
        return new Message(
                MessagesEnum.MESSAGE_CHAT_FROM_SERVER,
                "The game has been won by " + winningSuspect.getName() + "!");
    }

	/**
	 * Generate endTurn message
	 * @return endTurn message
	 * @throws Exception
	 */
	public static Message endTurn() throws Exception {
        logger.trace("Ending turn");
        return new Message(MessagesEnum.MESSAGE_CLIENT_END_TURN, "");
    }

	/**
	 * Generate clientPulse message (client-side)
	 * @return clientPulse message
	 * @throws Exception
	 */
	public static Message clientPulse() throws Exception {
        logger.trace("Sending watchdog pulse");
        return new Message(MessagesEnum.MESSAGE_PULSE, "");
    }

	/**
	 * Generate serverPulse message (server-side)
	 * @param pulsePayload The GameStatePulse payload object
	 * @return serverPulse message
	 * @throws Exception
	 */
	public static Message serverPulse(GameStatePulse pulsePayload) throws Exception {
        return new Message(MessagesEnum.MESSAGE_PULSE, pulsePayload);
    }

	/**
	 * Generate accusation message (client-side)
	 * @param suggestion Suggestion object of accusation
	 * @return accusation message
	 * @throws Exception
	 */
	public static Message accusation(Suggestion suggestion) throws Exception {
        return new Message(MessagesEnum.MESSAGE_CLIENT_ACCUSE, suggestion);
    }

	/**
	 * Generate suggestion message (client-side)
	 * @param suggestion Suggestion object of suggestion sequence
	 * @return suggestion message
	 * @throws Exception
	 */
	public static Message suggestion(Suggestion suggestion) throws Exception {
        return new Message(MessagesEnum.MESSAGE_CLIENT_SUGGEST, suggestion);
    }

	/**
	 * Generate failedConfig message (server-side)
	 * @param message Failed config description to client
	 * @return failedConfig message
	 * @todo This should be more structured
	 */
	public static Message failedConfig(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_CONFIG, message);
    }

	/**
	 * Generate failedMove message (server-side)
	 * @param message Failed move description to client
	 * @return failedMove message
	 */
	public static Message failedMove(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_MOVE, message);
    }

	/**
	 * Generate relaySuggestion message (server-side)
	 * @param cards Suggestion being disproven
	 * @param playerToDisprove Player object to disprove
	 * @return
	 */
	public static Message relaySuggestion(Suggestion cards, Player playerToDisprove) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RELAY_SUGGEST, cards, playerToDisprove.uuid);
    }

	/**
	 * Generate sendGameStatePulse message (server-side)
	 * @param gsp GameStatePulse payload to send to client
	 * @return sendGameStatePulse message
	 * @todo Duplicate of serverPulse?
	 */
	public static Message sendGameStatePulse(GameStatePulse gsp) {
        return new Message(MessagesEnum.MESSAGE_PULSE, gsp);
    }

	/**
	 * Generate clientRespondSuggestion message (client-side)
	 * @param card Card used to disprove suggestion, or null for no disprove
	 * @return clientRespondSuggestion message
	 */
	public static Message clientRespondSuggestion(Card card) {
        return new Message(MessagesEnum.MESSAGE_CLIENT_RESPONSE_SUGGEST, card);
    }

	/**
	 * Generate serverRespondSuggestion message (server-side)
	 * @param card Card used to disprove suggestion, or null for no disprove
	 * @return serverRespondSuggestion message
	 */
	public static Message serverRespondSuggestion(Card card) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RESPONSE_SUGGEST, card);
    }
}
