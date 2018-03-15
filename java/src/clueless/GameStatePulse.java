package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameStatePulse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2547048036673558884L;
	private boolean gameActive;
	private CardsEnum activeSuspect;
	private HashMap<CardsEnum, CardsEnum> suspectLocations;
	private HashMap<CardsEnum, CardsEnum> weaponLocations;
	private AvailableSuspects availableSuspects;
	private ArrayList<Card> cards;
	private ArrayList<Card> faceUpCards;
	
	public GameStatePulse(boolean gameActive, CardsEnum activeSuspect, AvailableSuspects availableSuspects, HashMap<CardsEnum, CardsEnum> suspectLocations, HashMap<CardsEnum, CardsEnum> weaponLocations, ArrayList<Card> cards,
			ArrayList<Card> faceUpCards) {
		setGameActive(gameActive);
		setActiveSuspect(activeSuspect);
		setSuspectLocations(suspectLocations);
		setWeaponLocations(weaponLocations);
		setAvailableSuspects(availableSuspects);
		setCards(cards);
		setFaceUpCards(faceUpCards);
	}

	/**
	 * @return the gameActive
	 */
	public boolean isGameActive() {
		return gameActive;
	}

	/**
	 * @param gameActive the gameActive to set
	 */
	private void setGameActive(boolean gameActive) {
		this.gameActive = gameActive;
	}

	/**
	 * @return the activeSuspect
	 */
	public CardsEnum getActiveSuspect() {
		return activeSuspect;
	}

	/**
	 * @param activeSuspect the activeSuspect to set
	 */
	private void setActiveSuspect(CardsEnum activeSuspect) {
		this.activeSuspect = activeSuspect;
	}

	/**
	 * @return the suspectLocations
	 */
	public HashMap<CardsEnum, CardsEnum> getSuspectLocations() {
		return suspectLocations;
	}

	/**
	 * @param suspectLocations the suspectLocations to set
	 */
	private void setSuspectLocations(HashMap<CardsEnum, CardsEnum> suspectLocations) {
		this.suspectLocations = suspectLocations;
	}

	/**
	 * @return the weaponLocations
	 */
	public HashMap<CardsEnum, CardsEnum> getWeaponLocations() {
		return weaponLocations;
	}

	/**
	 * @param weaponLocations the weaponLocations to set
	 */
	private void setWeaponLocations(HashMap<CardsEnum, CardsEnum> weaponLocations) {
		this.weaponLocations = weaponLocations;
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
	private void setAvailableSuspects(AvailableSuspects availableSuspects) {
		this.availableSuspects = availableSuspects;
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
	 * @param faceUpCards the cards to set
	 */
	public void setFaceUpCards(ArrayList<Card> faceUpCards) {
		this.faceUpCards = faceUpCards;
	}
}
