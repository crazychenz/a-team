/**
 * 
 */
package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.Serializable;

/**
 * @author tombo
 *
 */
public class Message implements Serializable{

    private static final Logger logger =
        LogManager.getLogger(Message.class);

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
}
