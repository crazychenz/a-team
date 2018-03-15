package clueless;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

public class ClientState {
	
	private AvailableSuspects availableSuspects;
	private boolean configured = false;
	private GameStatePulse gameState;
	private CardsEnum mySuspect;
	private boolean myTurn = false;
	private boolean alerted = false;
	private boolean moved = false;
	private CardsEnum lastLocation;
	private ArrayList<Card> cards;
	private ArrayList<Card> faceUpCards;
	
    private static final Logger logger
            = LogManager.getLogger(CLIEventHandler.class);
    
    ClientState() {
    	setCards(new ArrayList<Card>());
    	setFaceUpCards(new ArrayList<Card>());
    }

	/**
	 * @return the gameState
	 */
	public GameStatePulse getGameState() {
		return gameState;
	}

	/**
	 * @param gameState the gameState to set
	 */
	public void setGameState(GameStatePulse gameState) {
		this.gameState = gameState;
		
		if(gameState.isGameActive() && gameState.getActiveSuspect().equals(mySuspect)) {
			setMyTurn(true);
			if(!alerted) {
				System.out.println("You are the active player!  Make your move!");
				alerted = true;
			}
		}
		else
		{
			setMyTurn(false);
			if(alerted) {
				alerted = false;
			}
		}
		
		lastLocation = gameState.getSuspectLocations().get(mySuspect);
		
		setAvailableSuspects(gameState.getAvailableSuspects());
		setCards(gameState.getCards());
		setFaceUpCards(gameState.getFaceUpCards());
	}

	/**
	 * @return the availableSuspects
	 */
	public AvailableSuspects getAvailableSuspects() {
		return availableSuspects;
	}

	/**
	 * @param availableSuspects the availableSuspects to set
	 */
	public void setAvailableSuspects(AvailableSuspects availableSuspects) {
		this.availableSuspects = availableSuspects;
        //logger.info("Available Suspects Count: " + availableSuspects.list.size());
        //for (CardsEnum suspect : availableSuspects.list) {
        //    logger.info(suspect);
        //}
	}

	/**
	 * @return the configured
	 */
	public boolean isConfigured() {
		return configured;
	}

	/**
	 * @param configured the configured to set
	 */
	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	/**
	 * @return the mySuspect
	 */
	public CardsEnum getMySuspect() {
		return mySuspect;
	}

	/**
	 * @param mySuspect the mySuspect to set
	 */
	public void setMySuspect(CardsEnum mySuspect) {
		this.mySuspect = mySuspect;
	}

	/**
	 * @return the myTurn
	 */
	public boolean isMyTurn() {
		return myTurn;
	}

	/**
	 * @param myTurn the myTurn to set
	 */
	private void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	/**
	 * @return the moved
	 */
	public boolean isMoved() {
		return moved;
	}

	/**
	 * @param moved the moved to set
	 */
	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	/**
	 * @return the cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * @param cards the cards to set
	 */
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	/**
	 * @return the faceUpCards
	 */
	public ArrayList<Card> getFaceUpCards() {
		return faceUpCards;
	}

	/**
	 * @param faceUpCards the faceUpCards to set
	 */
	public void setFaceUpCards(ArrayList<Card> faceUpCards) {
		this.faceUpCards = faceUpCards;
	}
}