/**
 * 
 */
package clueless;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;

/**
 * @author tombo
 *
 */
public class Server {
	ServerSocket serverSocket;
	Game gameState;
	private ArrayList<ClientThread> connectedClients;
	
	public Server() {
		connectedClients = new ArrayList<ClientThread>();
		gameState = new Game(this);
	}
	
	public void listenForConnections(
	    CluelessServerSocket cluelessServerSocket) throws Exception {
        
        try {
		    serverSocket = cluelessServerSocket.getServerSocket();
		} catch (Exception e) {
		    System.out.println("Failed to get server socket.");
		    throw e;
		}
		
		while(true) {
			ClientThread w;
			try {
				w = new ClientThread(serverSocket.accept(), this);
				w.start();
				connectedClients.add(w);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Couldn't accept client");
			}
		}
	}
}

class ClientThread extends Thread {
	private Socket client;
	private Server server;
	private boolean running = false;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

	public ClientThread(Socket client,Server server) {
		this.client = client;
		this.server = server;
		System.out.println("Client connected");
		running = true;
	}
	
	public boolean send(Object data) {
		try {
			out.writeObject(data);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Send failed, ending thread");
			try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		running = false;
    		return false;
		}
		return true;
	}
	
	public void run() {
		try{
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
	    } 
		catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
	    }

	    while(running){
	    	try{
	    		Object message = in.readObject();
	    		server.gameState.processMessage(message, this);
	    	}
	    	catch (ClassNotFoundException e) {
	    		System.out.println("class not found");
	    	}
	    	catch (EOFException e) {
	    		System.out.println("Nothing to read");
	    	}
	    	catch (IOException e) {
	    		System.out.println("Read failed, ending thread");
	    		return;
	    	}
	    }	
	}
}
