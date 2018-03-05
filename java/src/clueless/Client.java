/**
 * 
 */
package clueless;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * @author tombo
 *
 */
public class Client {
	private Socket socket;
	public User user;
	private ConnectionThread conn;
	private boolean connected = false;
	
	public Client() {

	}
	
	public void SendData(Object data) {
		if(connected) {
			conn.send(data);
		}
	}
	
	public void connectToServer(){
		//Create socket connection
		while(!connected) {
			try{
				socket = new Socket(InetAddress.getLocalHost(), 2323);
				conn = new ConnectionThread(socket,this);
				conn.start();
				connected = true;
				Thread.sleep(500);
				user = new User(this);
				SendData(new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED,""));
			} 
			catch (UnknownHostException e) {
				System.out.println("Unknown host");
				System.exit(1);
			} 
			catch  (IOException e) {
				System.out.println("No Server. Retrying.");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class ConnectionThread extends Thread {
	private Socket server;
	private Client client;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
	static boolean sent = false;
    
	public ConnectionThread(Socket server, Client client) {
		this.server = server;
		this.client = client;
	}
	
	public void send(Object data) {
		try {
			out.writeObject(data);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
	    try{
	    	out = new ObjectOutputStream(server.getOutputStream());
	    	in = new ObjectInputStream(server.getInputStream());
	    } 
	    catch (IOException e) {
	    	System.out.println("in or out failed");
	    	System.exit(-1);
	    }

	    while(true){
	    	try{
		    	Object message = in.readObject();
		    	client.user.processMessage(message);
	    	}
	    	catch (ClassNotFoundException e) {
	    		System.out.println("class not found");
	    	}
	    	catch (EOFException e) {
	    		System.out.println("Nothing to read");
	    	}
	    	catch (IOException e) {
	    		System.out.println("Read failed");
	    		System.exit(-1);
	    	}
	    }	
	}
}
