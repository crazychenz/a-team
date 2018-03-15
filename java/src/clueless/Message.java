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
	private String fromUuid;
	private boolean broadcast;

	public Message(MessagesEnum messageID, Object messageData) {
		this.messageID = messageID;
		this.messageData = messageData;
		fromUuid = null;
		broadcast = false;
	}

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

	public String toString() {
		return "Message ID: " + messageID
				+ " Message Data: " + messageData;
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

	public static Message endTurn() throws Exception {
		logger.trace("Ending turn");
		return new Message(MessagesEnum.MESSAGE_CLIENT_END_TURN, "");
	}

	public static Message clientPulse() throws Exception {
		// BUG: log4j is printing \n when trace is disabled.
		//logger.trace("Sending watchdog pulse");
		return new Message(MessagesEnum.MESSAGE_PULSE, "");
	}

	public static Message serverPulse(GameStatePulse pulsePayload) throws Exception {
		return new Message(MessagesEnum.MESSAGE_PULSE, pulsePayload);
	}

}
