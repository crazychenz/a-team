package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import clueless.Helper;

public class Game {

    private static final Logger logger
            = LogManager.getLogger(Game.class);

    private CardDeck cards;
    private LinkedList<Player> activePlayers;
    public boolean classicMode = false;
    private HashMap<CardsEnum, Location> locations;
    private HashMap<CardsEnum, Location> hallways;
    private HashMap<CardsEnum, Weapon> weapons;
    private HashMap<CardsEnum, Suspect> suspects;
    private boolean gameStarted = false;
    private Player activePlayer;

    public Game() {
        locations = new HashMap<CardsEnum, Location>();
        weapons = new HashMap<CardsEnum, Weapon>();
        suspects = new HashMap<CardsEnum, Suspect>();
        hallways = new HashMap<CardsEnum, Location>();
        activePlayers = new LinkedList<Player>();
        cards = new CardDeck();
        setupWeapons();
        setupLocations();
        setupSuspects();
        //HeartbeatThread gt = new HeartbeatThread(activePlayers,this);
        //gt.start();
    }

    /*private void sendMessageToAllPlayers(Message message) {
        Iterator<Player> iter = activePlayers.iterator();
        while(iter.hasNext()) {
            iter.next().getThread().send(message);
        }
    }*/
    
    private void setupWeapons() {
        for (CardsEnum weapon : CardsEnum.values()) {
            if (weapon.getCardType() == CardType.CARD_TYPE_WEAPON) {
                Weapon t = new Weapon(weapon);
                weapons.put(weapon,t);
                cards.add(new Card(weapon));
            }
        }
    }

    private void setupLocations() {
        for (CardsEnum location : CardsEnum.values()) {
            if (location.getCardType() == CardType.CARD_TYPE_LOCATION) {
                Location t = new Location(location);
                locations.put(location, t);
                cards.add(new Card(location));
            }
        }
        
        for (CardsEnum hall : CardsEnum.values()) {
            if (hall.getCardType() == CardType.CARD_TYPE_HALLWAY) {
                Location t = new Location(hall);
                hallways.put(hall, t);
            }
        }
        
        for (Location location: locations.values()) {
        	addHallways(location);
        }
        
        for (Location location: locations.values()) {
        	System.out.println(location);
        }
        
        for (Location hall: hallways.values()) {
        	logger.info(hall);
        }
    }
    
    private void addHallways(Location location) {
    	Location tempLocation;
    	switch(location.getLocation()) {
    	case LOCATION_STUDY:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_HALL);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_LIBRARY);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);
    		
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_KITCHEN));
    		
    		break;
    	case LOCATION_HALL:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_HALL);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_LOUNGE);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);
    		
    		break;
    		
    	case LOCATION_LOUNGE:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_LOUNGE);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_LOUNGE_DINING);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);
    		
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_CONSERVATORY));
    		
    		break;
    		
    	case LOCATION_DININGROOM:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_LOUNGE_DINING);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_KITCHEN);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);
    		
    		break;
    		
    	case LOCATION_KITCHEN:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_KITCHEN);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_KITCHEN_BALL);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
    		
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_STUDY));
    		
    		break;
    		
    	case LOCATION_BALLROOM:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_KITCHEN_BALL);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_CONSERVATORY);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
    		
    		break;
    		
    	case LOCATION_CONSERVATORY:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_CONSERVATORY);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);
    		
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SECRET, locations.get(CardsEnum.LOCATION_LOUNGE));
    		
    		break;
    		
    		
    	case LOCATION_LIBRARY:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_STUDY_LIBRARY);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_LIBRARY_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_CONSERVATORY_LIBRARY);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);
    		
    		break;
    		
    	case LOCATION_BILLIARDROOM:
    		tempLocation = hallways.get(CardsEnum.HALLWAY_HALL_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_DINING_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_BALL_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_NORTH, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_SOUTH, tempLocation);
    		
    		tempLocation = hallways.get(CardsEnum.HALLWAY_LIBRARY_BILLIARD);
    		tempLocation.setAdjacentRoom(DirectionsEnum.DIRECTION_EAST, location);
    		location.setAdjacentRoom(DirectionsEnum.DIRECTION_WEST, tempLocation);
    		break;
		default:
			break;
    		
    	}
    }

    private void setupSuspects() {
        for (CardsEnum suspect : CardsEnum.values()) {
            if (suspect.getCardType() == CardType.CARD_TYPE_SUSPECT) {
                Suspect t = new Suspect(suspect, Helper.GetStartingLocationOfSuspect(suspect));
                suspects.put(suspect, t);
                cards.add(new Card(suspect));
            }
        }
    }

    public Message processMessage(Message msg) {
        logger.debug("Processing the message");

        switch (msg.getMessageID()) {
            case MESSAGE_CLIENT_CONNECTED:
                //Client just connected, let them pick a suspect
                return sendAvailableSuspects();
            case MESSAGE_CLIENT_START_GAME:
                //Client chose to start the game
                //Should check with all other clients before starting
                startGame();
                break;
            case MESSAGE_CHAT_FROM_CLIENT:
                //Chat from client
                System.out.println("Chat from client: " + msg.getMessageData());
                //publishChatMessage(msg);
                break;
            case MESSAGE_CHAT_FROM_SERVER:
                //This shouldn't happen
                System.out.println("Chat from server: " + msg.getMessageData());
                break;
            case MESSAGE_CLIENT_CONFIG:
                //Client picked a suspect (and username, any other configurables, etc)
                CardsEnum pickedSuspect = (CardsEnum) msg.getMessageData();
                for (Suspect s : suspects.values()) {
                    if (s.getSuspect() == pickedSuspect) {
                        s.setActive(true);
                    }
                }
                addPlayer(pickedSuspect);
                // TODO: Develop acknowledgement
                break;
            case MESSAGE_CLIENT_MOVE:
                //handle the move
                setNextPlayer();
                break;
            case MESSAGE_CLIENT_SUGGESTION:
                //handle the suggestion
                break;
            case MESSAGE_CLIENT_ACCUSE:
                //handle the accuse
                setNextPlayer();
                break;
            case MESSAGE_PULSE:
                // return echo request
                return msg;
            default:
                break;
        }

        return null;
    }

    private void startGame() {
        if (activePlayers.size() >= 3) {
            cards.setupCardDeckAndDealCards(activePlayers, classicMode);
            gameStarted = true;
            shufflePlayersAndSetActivePlayer();
            //sendMessageToAllPlayers(new Message(MessagesEnum.MESSAGE_SERVER_START_GAME,""));			
        } else {
            logger.info("Not enough players to start the game");
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

    private void addPlayer(CardsEnum suspect) {
        logger.info("Adding new player");
        activePlayers.add(new Player(suspect));
        startGame();	//For now, try to start the game after every user connects.  We need to let the users pick when to start
    }

    public Message getGameState() {
        CardsEnum activeSuspect;
        if (activePlayer != null) {
            activeSuspect = activePlayer.getSuspect();
        } else {
            activeSuspect = activePlayers.element().getSuspect();
        }
        return new Message(MessagesEnum.MESSAGE_SERVER_HEARTBEAT, "");
    }

    private Message sendAvailableSuspects() {
        AvailableSuspects availableSuspects = new AvailableSuspects();
        for (Suspect suspect : suspects.values()) {
            if (!suspect.getActive()) {
                availableSuspects.list.add(suspect.getSuspect());
            }
        }

        return new Message(
                MessagesEnum.MESSAGE_SERVER_AVAILABLE_SUSPECTS,
                availableSuspects);
    }
}

//class HeartbeatThread extends Thread {
//	private LinkedList<Player> activePlayers;
//	private Game game;
//    static long lastUpdate = 0;
//    
//    public HeartbeatThread(LinkedList<Player> activePlayers, Game game) {
//		this.activePlayers = activePlayers;
//		this.game=game;
//	}
//	
//	public void run() {
//		while(true) {
//	    	//We should update all clients at the same time.
//	    	if((System.currentTimeMillis() - lastUpdate) > 1000) {
//	    		LinkedList<?> copy = (LinkedList<?>) activePlayers.clone();
//	    		Iterator<?> iter = copy.iterator();
//	    		while(iter.hasNext()) {
//	    			Player p = (Player) iter.next();
//	    			if(!p.getThread().send(game.getGameState())) {
//	    				iter.remove();
//	    			}
//	    		}
//		    	lastUpdate = System.currentTimeMillis();
//	    	}
//		}
//	}
//}

