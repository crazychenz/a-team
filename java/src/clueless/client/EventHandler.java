package clueless.client;

import clueless.io.*;

/**
 * This is a base class for different UIs to overload.
 * @author ateam
 */
public class EventHandler {

	/**
	 * Triggered when a message is received by the Server.
	 * @param client
	 * @param msg
	 */
	public void onMessageEvent(Client client, Message msg) {}
}
