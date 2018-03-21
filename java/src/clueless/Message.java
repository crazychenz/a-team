/** */
package clueless;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Message implements Serializable {

    private static final Logger logger = LogManager.getLogger(Message.class);

    /** */
    private static final long serialVersionUID = -2609974894890218306L;

    private final MessagesEnum messageID;
    private final Object messageData;
    private String fromUuid;
    private boolean broadcast;
    private String toUuid;

    public Message(MessagesEnum messageID, Object messageData) {
        this.messageID = messageID;
        this.messageData = messageData;
        fromUuid = null;
        broadcast = false;
        toUuid = "";
    }

    public Message(MessagesEnum messageID, Object messageData, String toUuid) {
        this.messageID = messageID;
        this.messageData = messageData;
        fromUuid = null;
        broadcast = false;
        this.toUuid = toUuid;
    }

    // TODO add a setSingle or something, to be able to send a message to a single client by
    // specifying their uuid
    public void setBroadcast(boolean value) {
        broadcast = value;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String uuid) {
        fromUuid = uuid;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String uuid) {
        toUuid = uuid;
    }

    @Override
    public String toString() {
        return "Message ID: " + messageID + " Message Data: " + messageData;
    }

    public MessagesEnum getMessageID() {
        return messageID;
    }

    public Object getMessageData() {
        return messageData;
    }

    public static ByteBuffer toBuffer(Message msg) throws Exception {
        ByteBufferBackedOutputStream bbos;
        ObjectOutputStream oos;
        ByteBuffer buf;

        buf = ByteBuffer.allocate(4096);
        bbos = new ByteBufferBackedOutputStream(buf);

        try {
            oos = new ObjectOutputStream(bbos);
        } catch (IOException e) {
            logger.error(e);
            bbos.close();
            throw e;
        }
        oos.writeObject(msg);
        buf.flip();
        oos.close();
        return buf;
    }

    public static Message fromBuffer(ByteBuffer buf) throws Exception {
        ByteBufferBackedInputStream bbis;
        ObjectInputStream ois;
        Message message;

        bbis = new ByteBufferBackedInputStream(buf);
        try {
            ois = new ObjectInputStream(bbis);
        } catch (IOException e) {
            logger.error(e);
            bbis.close();
            throw e;
        }
        message = (Message) ois.readObject();
        ois.close();
        return message;
    }

    public static Message clientConnect() throws Exception {
        logger.trace("Connecting to Game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED, "");
    }

    public static Message startGame() throws Exception {
        logger.trace("Client wants to start the game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_START_GAME, "");
    }

    public static Message moveClient(DirectionsEnum direction) throws Exception {
        logger.trace("Client is moving");
        return new Message(MessagesEnum.MESSAGE_CLIENT_MOVE, direction);
    }

    public static Message clientConfig(CardsEnum suspect) throws Exception {
        logger.trace("Configuring client");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONFIG, suspect);
    }

    public static Message chatMessage(String chatMessage) throws Exception {
        logger.trace("Sending chat to others");
        return new Message(MessagesEnum.MESSAGE_CHAT_FROM_CLIENT, chatMessage);
    }

    public static Message winMessage(CardsEnum winningSuspect) {
        return new Message(
                MessagesEnum.MESSAGE_CHAT_FROM_SERVER,
                "The game has been won by " + winningSuspect.getLabel() + "!");
    }

    public static Message endTurn() throws Exception {
        logger.trace("Ending turn");
        return new Message(MessagesEnum.MESSAGE_CLIENT_END_TURN, "");
    }

    public static Message clientPulse() throws Exception {
        logger.trace("Sending watchdog pulse");
        return new Message(MessagesEnum.MESSAGE_PULSE, "");
    }

    public static Message serverPulse(GameStatePulse pulsePayload) throws Exception {
        return new Message(MessagesEnum.MESSAGE_PULSE, pulsePayload);
    }

    public static Message accusation(CardWrapper cards) throws Exception {
        return new Message(MessagesEnum.MESSAGE_CLIENT_ACCUSE, cards);
    }

    public static Message suggestion(CardWrapper cards) throws Exception {
        return new Message(MessagesEnum.MESSAGE_CLIENT_SUGGEST, cards);
    }

    public static Message failedConfig(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_CONFIG, message);
    }

    public static Message failedMove(String message) {
        return new Message(MessagesEnum.MESSAGE_SERVER_FAIL_MOVE, message);
    }

    public static Message relaySuggestion(CardWrapper cards, Player playerToDisprove) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RELAY_SUGGEST, cards, playerToDisprove.uuid);
    }

    public static Message sendGameStatePulse(GameStatePulse gsp) {
        return new Message(MessagesEnum.MESSAGE_PULSE, gsp);
    }

    public static Message clientRespondSuggestion(CardWrapper cards) {
        return new Message(MessagesEnum.MESSAGE_CLIENT_RESPONSE_SUGGEST, cards);
    }

    public static Message serverRespondSuggestion(CardWrapper cards) {
        return new Message(MessagesEnum.MESSAGE_SERVER_RESPONSE_SUGGEST, cards);
    }
}
