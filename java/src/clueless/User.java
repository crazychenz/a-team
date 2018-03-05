/**
 * 
 */
package clueless;

import java.io.Console;
import java.util.ArrayList;

/**
 * @author tombo
 *
 */

//This is on client side

public class User {
	private String userName;
	private ArrayList<Card> myCards;
	private Notebook notebook;
	private CardsEnum suspect;
	private boolean myTurn;
	private int ID;
	Client client;
	static boolean makingSelection = false;
	
	public User(Client client)
	{
		//super(suspect, Helper.GetStartingLocationOfSuspect(suspect));
		//mySuspect = suspect;
		this.client = client;
		myCards = new ArrayList<Card>();
	}
	
	private void askAQuestion(CardsEnum location, CardsEnum weapon, CardsEnum suspect) {

	}
	
	private void accuse(CardsEnum location, CardsEnum weapon, CardsEnum suspect) {

	}
	
	private Card showSingleCard() {
		return myCards.get(0);
	}
	
	private void pickASuspect(ArrayList<CardsEnum> suspects) {
		//Put console init stuff in Helper
		Console console = System.console();
        if (console == null) {
            System.out.println("Unable to fetch console");
            return;
        }
        boolean suspectSelected = false;
        
        while(!suspectSelected) {
            console.printf("Please pick a suspect to play as: \n");
            int index = 1;
            for (CardsEnum suspect : suspects) {
            	console.printf("\t%d %s\n", index, suspect.getLabel());
            	index++;
            }
            String line = console.readLine();
            
            int choice = 0;
            
            try {
            	choice = Integer.parseInt(line);
            	choice -=1;
            	console.printf("You have selected %s", suspects.get(choice).getLabel());
            	suspect = suspects.get(choice);
            	suspectSelected = true;
            }
            catch (NumberFormatException e) {
            	console.printf("\nThere is an issue with your selection.  Please try again.\n");
            }
        }

		sendUserConfiguration(suspect);
	}
	
	private void sendUserConfiguration(CardsEnum suspect) {
		client.SendData(new Message(MessagesEnum.MESSAGE_CLIENT_CONFIG,suspect));
	}
	
	private void sendUserChat(String chat) {
		client.SendData(new Message(MessagesEnum.MESSAGE_CHAT_FROM_CLIENT,chat));
	}
	
	private void startGame() {
		System.out.println("Game is starting!");
		//UserInputThread uit = new UserInputThread(this);
		//uit.start();
	}
	
	private void processHeartbeat(Heartbeat heartbeat) {
		//Do we want to ask users if they want to start the game here?  Or how should it be handled
//		Console console = System.console();
//        if (console == null) {
//            System.out.println("Unable to fetch console");
//            return;
//        }
//        if(!heartbeat.isGameStarted() && !makingSelection) {
//        	makingSelection = true;
//            console.printf("Players connected: %d\nWould you like to start the game? 1 = yes, 0 = no\n", heartbeat.getPlayersConnected());
//            
//            String line = console.readLine();
//            
//            int choice = 0;
//            
//            try {
//            	choice = Integer.parseInt(line);
//            	console.printf("You have selected %d\n", choice);
//            	client.SendData(new Message(MessagesEnum.MESSAGE_CLIENT_START_GAME,""));
//            }
//            catch (NumberFormatException e) {
//            	console.printf("\nThere is an issue with your selection.  Please try again.\n");
//            }
//            makingSelection = false;
//        }
		if(heartbeat.isGameStarted()) {
			if(heartbeat.getActivePlayer() == suspect) {
				Console console = System.console();
		        if (console == null) {
		            System.out.println("Unable to fetch console");
		            return;
		        }
		        console.printf("Your turn!\n\t1)Move\n\t2)Ask a question\n\t3)Accuse");
		        int choice = 0;
		        String line = console.readLine();
	            try {
	            	choice = Integer.parseInt(line);
	            	console.printf("You have selected %d\n", choice);
	            	processChoice(choice);
	            }
	            catch (NumberFormatException e) {
	            	console.printf("\nThere is an issue with your selection.  Please try again.\n");
	            }
			}
		}
	}
	
	private void processChoice(int input) {
		switch(input) {
		case 1:
			//Todo - where to move?
			client.SendData(new Message(MessagesEnum.MESSAGE_CLIENT_MOVE,""));
			break;
		case 2:
			//Todo - what to ask
			client.SendData(new Message(MessagesEnum.MESSAGE_CLIENT_QUESTION,""));
			break;
		case 3:
			//Todo - what to accuse?
			client.SendData(new Message(MessagesEnum.MESSAGE_CLIENT_ACCUSE,""));
			break;
		default:
			break;
		}
		
	}
	
	public void processMessage(Object message) {
		Message m = (Message) message;
		switch(m.getMessageID()) {
			case MESSAGE_CARD_FROM_SERVER:
				addCard((Card)m.getMessageData());
				break;
			case MESSAGE_CHAT_FROM_SERVER:
				System.out.println("Chat from server: " + m.getMessageData());
				break;
			case MESSAGE_SERVER_HEARTBEAT:
				//System.out.println("processing hb");
				processHeartbeat((Heartbeat)m.getMessageData());
				break;
			case MESSAGE_SERVER_AVAILABLE_SUSPECTS:
				pickASuspect((ArrayList<CardsEnum>)m.getMessageData());
				break;
			case MESSAGE_SERVER_START_GAME:
				startGame();
				break;
			case MESSAGE_CHAT_FROM_CLIENT:
				break;
			case MESSAGE_CLIENT_CONFIG:
				break;
			case MESSAGE_CLIENT_CONNECTED:
				break;
			case MESSAGE_CLIENT_START_GAME:
				break;
			default:
				break;
		}
	}
	
	private void addCard(Card card) {
		myCards.add(card);
	}
}

//Do we need a separate thread to process input?
/*class UserInputThread extends Thread {
	private User user;
	public UserInputThread(User user) {
		this.user = user;
	}
	
	public void run() {
		while(true) {
			Console console = System.console();
	        if (console == null) {
	            System.out.println("Unable to fetch console");
	            return;
	        }
	        String line = console.readLine();
	        user.processInput(line);
		}
	}
}*/
