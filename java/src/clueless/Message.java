/**
 *
 */
package clueless;

import java.nio.ByteBuffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author tombo
 *
 */
public class Message implements Serializable {

    private static final Logger logger
            = LogManager.getLogger(Message.class);

    /**
     *
     */
    private static final long serialVersionUID = -2609974894890218306L;
    private MessagesEnum messageID;
    private Object messageData;

    public Message(MessagesEnum messageID, Object messageData) {
        this.messageID = messageID;
        this.messageData = messageData;
    }

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
            throw e;
        }
        oos.writeObject(msg);
        buf.flip();

        return buf;
    }

    public static Message fromBuffer(ByteBuffer buf) throws Exception {
        ByteBufferBackedInputStream bbis;
        ObjectInputStream ois;

        bbis = new ByteBufferBackedInputStream(buf);
        try {
            ois = new ObjectInputStream(bbis);
        } catch (IOException e) {
            logger.error(e);
            throw e;
        }
        return (Message) ois.readObject();
    }

    public static Message clientConnect() throws Exception {
        logger.debug("Connecting to Game");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED, "");
    }

    public static Message clientConfig(CardsEnum suspect) throws Exception {
        logger.debug("Configuring client");
        return new Message(MessagesEnum.MESSAGE_CLIENT_CONFIG, suspect);
    }

    public static Message chatMessage(String chatMessage) throws Exception {
        logger.debug("Sending chat to others");
        return new Message(MessagesEnum.MESSAGE_CHAT_FROM_CLIENT, chatMessage);
    }
}
