package clueless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import clueless.Helper;

public class Game {

	private CardDeck cards;
	private LinkedList<Player> activePlayers;
	public boolean classicMode = false;
	private ArrayList<Location> locations;
	private ArrayList<Weapon> weapons;
	private ArrayList<Suspect> suspects;
	private boolean gameStarted = false;
	private Server server;
	private Player activePlayer;
	
	public Game(Server server) {
		locations = new ArrayList<Location>();
		weapons = new ArrayList<Weapon>();
		suspects = new ArrayList<Suspect>();
		activePlayers = new LinkedList<Player>();
		cards = new CardDeck();
		setupWeapons();
		setupLocations();
		setupSuspects();
		this.server = server;
		HeartbeatThread gt = new HeartbeatThread(activePlayers,this);
		gt.start();
	}
	
	private void setupWeapons() {
		for(CardsEnum weapon : CardsEnum.values()) {
			if(weapon.getCardType() == CardType.CARD_TYPE_WEAPON) {
				Weapon t = new Weapon(weapon);
				weapons.add(t);
				cards.add(new Card(weapon));
			}
		}
	}
	
	private void sendMessageToAllPlayers(Message message) {
		Iterator<Player> iter = activePlayers.iterator();
		while(iter.hasNext()) {
			iter.next().getThread().send(message);
		}
	}
	
	private void setupLocations() {
		for(CardsEnum location : CardsEnum.values()) {
			if(location.getCardType() == CardType.CARD_TYPE_LOCATION) {
				Location t = new Location(location);
				locations.add(t);
				cards.add(new Card(location));
			}
		}
		
		//ToDo
		//Now connect all of the locations with a hallway and the other locations
		for(Location loc : locations) {

		}
	}
	
	private void setupSuspects() {
		for(CardsEnum suspect : CardsEnum.values()) {
			if(suspect.getCardType() == CardType.CARD_TYPE_SUSPECT) {
				Suspect t = new Suspect(suspect, Helper.GetStartingLocationOfSuspect(suspect));
				suspects.add(t);
				cards.add(new Card(suspect));
			}
		}
	}
	
	public void processMessage(Object message, ClientThread thread) {
		System.out.println("Processing the message");
		System.out.println(message);
		Message newMessage = (Message)message;
		
		switch(newMessage.getMessageID()) {
			case MESSAGE_CLIENT_START_GAME:
				//Client chose to start the game
				//Should check with all other clients before starting
				startGame();
				break;
			case MESSAGE_CLIENT_CONNECTED:
				//Client just connected, let them pick a suspect
				sendAvailableSuspects(thread);
				break;
			case MESSAGE_CHAT_FROM_CLIENT:
				//Chat from client
				System.out.println("Chat from client: " + newMessage.getMessageData());
				break;
			case MESSAGE_CHAT_FROM_SERVER:
				//This shouldn't happen
				System.out.println("Chat from server: " + newMessage.getMessageData());
				break;
			case MESSAGE_CLIENT_CONFIG:
				//Client picked a suspect (and username, any other configurables, etc)
				CardsEnum pickedSuspect = (CardsEnum)newMessage.getMessageData();
				for(Suspect s : suspects) {
					if(s.getSuspect() == pickedSuspect) {
						s.setActive(true);
					}
				}
				addPlayer(pickedSuspect, thread);
				break;
			case MESSAGE_CLIENT_MOVE:
				//handle the move
				setNextPlayer();
				break;
			case MESSAGE_CLIENT_SUGGESTION:
				//handle the 
				break;
			case MESSAGE_CLIENT_ACCUSE:
				//handle the accuse
				setNextPlayer();
				break;
			default:
				break;
		}
	}
	
	private void startGame() {
		if(activePlayers.size() >= 3) {
			cards.setupCardDeckAndDealCards(activePlayers,classicMode);
			gameStarted = true;
			shufflePlayersAndSetActivePlayer();
			sendMessageToAllPlayers(new Message(MessagesEnum.MESSAGE_SERVER_START_GAME,""));			
		}
		else {
			System.out.println("Not enough players to start the game");
			return;
		}
	}
	
	private void shufflePlayersAndSetActivePlayer() {
		//randomly set activePlayer linked list and set activePlayer variable
		Collections.shuffle(activePlayers, Helper.GetRandom());
		setNextPlayer();
	}
	
	
	private void setNextPlayer() {
		activePlayer = activePlayers.element();
		activePlayers.add(activePlayers.pop());
	}
	
	private void addPlayer(CardsEnum suspect, ClientThread thread) {
		System.out.println("Adding new player");
		activePlayers.add(new Player(thread, suspect));
		startGame();	//For now, try to start the game after every user connects.  We need to let the users pick when to start
	}
	
	public Message getGameState() {
		CardsEnum activeSuspect;
		if(activePlayer != null) {
			activeSuspect =  activePlayer.getSuspect();
		}
		else {
			activeSuspect = activePlayers.element().getSuspect();
		}
		Heartbeat hb = new Heartbeat(activePlayers.size(), gameStarted, activeSuspect);
		System.out.println("game state is " + hb);
		return new Message(MessagesEnum.MESSAGE_SERVER_HEARTBEAT,hb);
	}
	
	private void sendAvailableSuspects(ClientThread thread) {
		ArrayList<CardsEnum> availableSuspects = new ArrayList<CardsEnum>();
		for (Suspect suspect : suspects) {
			if(!suspect.getActive()) {
				availableSuspects.add(suspect.getSuspect());
			}
		}
		thread.send(new Message(MessagesEnum.MESSAGE_SERVER_AVAILABLE_SUSPECTS,availableSuspects));
	}
}

class HeartbeatThread extends Thread {
	private LinkedList<Player> activePlayers;
	private Game game;
    static long lastUpdate = 0;
    
    
    public HeartbeatThread(LinkedList<Player> activePlayers, Game game) {
		this.activePlayers = activePlayers;
		this.game=game;
	}
	
	public void run() {
		while(true) {
	    	//We should update all clients at the same time.
	    	if((System.currentTimeMillis() - lastUpdate) > 1000) {
	    		LinkedList<Player> copy = (LinkedList<Player>) activePlayers.clone();
	    		Iterator<Player> iter = copy.iterator();
	    		while(iter.hasNext()) {
	    			Player p = iter.next();
	    			if(!p.getThread().send(game.getGameState())) {
	    				iter.remove();
	    			}
	    		}
		    	lastUpdate = System.currentTimeMillis();
	    	}
		}
	}
}
