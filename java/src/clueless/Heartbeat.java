package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.Serializable;

public class Heartbeat implements Serializable{

    private static final Logger logger =
        LogManager.getLogger(Heartbeat.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 6649550097257861656L;
	private boolean gameStarted = false;
	private int playersConnected = 0;
	private CardsEnum activePlayer;
	
	public Heartbeat(int playersConnected, boolean gameStarted, CardsEnum activePlayer) {
		this.playersConnected = playersConnected;
		this.gameStarted = gameStarted;
		this.activePlayer = activePlayer;
	}

	/**
	 * @return the gameStarted
	 */
	public boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * @param gameStarted the gameStarted to set
	 */
	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	/**
	 * @return the playersConnected
	 */
	public int getPlayersConnected() {
		return playersConnected;
	}

	public String toString() {
		return "Number of players " + playersConnected + ", game started " + gameStarted + ", active player " + activePlayer;
	}

	/**
	 * @return the activePlayer
	 */
	public CardsEnum getActivePlayer() {
		return activePlayer;
	}

	/**
	 * @param activePlayer the activePlayer to set
	 */
	public void setActivePlayer(CardsEnum activePlayer) {
		this.activePlayer = activePlayer;
	}

}
