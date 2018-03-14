package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ClientState {
	
	private AvailableSuspects availableSuspects;
	private boolean configured = false;
	private GameStatePulse gameState;
	private CardsEnum mySuspect;
	private boolean myTurn = false;
	private static boolean alerted = false;
	private boolean moved = false;
	private CardsEnum lastLocation;
	
    private static final Logger logger
            = LogManager.getLogger(CLIEventHandler.class);
    
    ClientState() {
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
				logger.info("You are the active player!  Perform an action.");
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
		
		//check if our suspect has moved.  if so, the move was valid. if not, then we need to reset setMoved(false) so they can attempt to move again
		//to do this, we need to fill out the suspect and weapon locations structures in the pulse to see if it changed from the previous pulse to this one
		
		setAvailableSuspects(gameState.getAvailableSuspects());
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
}