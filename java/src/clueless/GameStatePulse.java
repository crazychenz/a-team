package clueless;

import java.io.Serializable;
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
	
	public GameStatePulse(boolean gameActive, CardsEnum activeSuspect, AvailableSuspects availableSuspects, HashMap<CardsEnum, CardsEnum> suspectLocations, HashMap<CardsEnum, CardsEnum> weaponLocations) {
		setGameActive(gameActive);
		setActiveSuspect(activeSuspect);
		setSuspectLocations(suspectLocations);
		setWeaponLocations(weaponLocations);
		setAvailableSuspects(availableSuspects);
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
}
