package clueless;

import java.util.Random;

public class Helper {

	static public CardsEnum GetStartingLocationOfSuspect(CardsEnum suspect) {
		switch(suspect) {
		case SUSPECT_PLUM: 
			return CardsEnum.LOCATION_BATHROOM;
		case SUSPECT_PEACOCK: 
			return CardsEnum.LOCATION_BEDROOM;
		case SUSPECT_GREEN: 
			return CardsEnum.LOCATION_COURTYARD;
		case SUSPECT_WHITE: 
			return CardsEnum.LOCATION_GAMEROOM;
		case SUSPECT_MUSTARD: 
			return CardsEnum.LOCATION_GARAGE;
		case SUSPECT_SCARLET: 
			return CardsEnum.LOCATION_STUDY;
		default:
			return CardsEnum.LOCATION_KITCHEN;
		}
	}
	
	static public int GetNumberOfFaceUpCardsForNumberOfUsers(int numUsers, boolean classicClue) {
		if(!classicClue) {
			if(numUsers == 3 || numUsers == 4) {
				return 6;
			}
			else if(numUsers == 5) {
				return 3;
			}
			else if (numUsers == 6) {
				return 6;
			}
			else {
				return 6;
			}
		}
		if(classicClue) {
			if(numUsers == 3) {
				return 0;
			}
			else if(numUsers == 4) {
				return 2;
			}
			else if (numUsers == 5) {
				return 3;
			}
			else if (numUsers == 6) {
				return 0;
			}
			else {
				return 0;
			}
		}
		return 0;
	}
	
	static public Random GetRandom() {
		return new Random();
	}
}
