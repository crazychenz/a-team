package clueless;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//This file is meant to represent the cards from the board game and nothing else

public enum CardsEnum {
	LOCATION_KITCHEN(0x01, "Kitchen", CardType.CARD_TYPE_LOCATION),
	LOCATION_BALLROOM(0x02, "Ballroom", CardType.CARD_TYPE_LOCATION),
	LOCATION_CONSERVATORY(0x03, "Conservatory", CardType.CARD_TYPE_LOCATION),
	LOCATION_LIBRARY(0x04, "Library", CardType.CARD_TYPE_LOCATION),
	LOCATION_HALL(0x05, "Hall", CardType.CARD_TYPE_LOCATION),
	LOCATION_STUDY(0x06, "Study", CardType.CARD_TYPE_LOCATION),
	LOCATION_DININGROOM(0x07, "Dining Room", CardType.CARD_TYPE_LOCATION),
	LOCATION_LOUNGE(0x08, "Lounge", CardType.CARD_TYPE_LOCATION),
	LOCATION_BILLIARDROOM(0x09, "Billiard Room", CardType.CARD_TYPE_LOCATION),
	
	
	HALLWAY_STUDY_HALL(0x91, "Study - Hall", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_STUDY_LIBRARY(0x92, "Study - Library", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_HALL_LOUNGE(0x93, "Hall - Lounge", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_HALL_BILLIARD(0x94, "Hall - Billiard Room", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_LOUNGE_DINING(0x95, "Lounge - Dining Room", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_DINING_BILLIARD(0x96, "Dining Room - Billiard Room", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_DINING_KITCHEN(0x97, "Dining Room - Kitchen", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_KITCHEN_BALL(0x98, "Kitchen - Ballroom", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_BALL_BILLIARD(0x99, "Ballroom - Billiard Room", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_BALL_CONSERVATORY(0x100, "Ballroom - Conservatory", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_CONSERVATORY_LIBRARY(0x101, "Conservatory - Library", CardType.CARD_TYPE_HALLWAY),
	HALLWAY_LIBRARY_BILLIARD(0x102, "Library - Billiard Room", CardType.CARD_TYPE_HALLWAY),
	

	WEAPON_LEADPIPE(0x21, "Lead Pipe", CardType.CARD_TYPE_WEAPON),
	WEAPON_REVOLVER(0x22, "Revolver", CardType.CARD_TYPE_WEAPON),
	WEAPON_ROPE(0x23, "Rope", CardType.CARD_TYPE_WEAPON),
	WEAPON_DAGGER(0x24, "Dagger", CardType.CARD_TYPE_WEAPON),
	WEAPON_WRENCH(0x25, "Wrench", CardType.CARD_TYPE_WEAPON),
	WEAPON_CANDLESTICK(0x26, "Candlestick", CardType.CARD_TYPE_WEAPON),
	
	SUSPECT_PLUM(0x31, "Plum", CardType.CARD_TYPE_SUSPECT),
	SUSPECT_PEACOCK(0x32, "Peacock", CardType.CARD_TYPE_SUSPECT),
	SUSPECT_WHITE(0x33, "White", CardType.CARD_TYPE_SUSPECT),
	SUSPECT_SCARLET(0x34, "Scarlet", CardType.CARD_TYPE_SUSPECT),
	SUSPECT_GREEN(0x35, "Green", CardType.CARD_TYPE_SUSPECT),
	SUSPECT_MUSTARD(0x36, "Mustard", CardType.CARD_TYPE_SUSPECT);

	
	private final int uid;
	private final String label;
	private final CardType cardType;
	
	CardsEnum(int uid, String label, CardType cardType) {
		this.uid = uid;
		this.label = label;
		this.cardType = cardType;
	}

	public int getUid() {
		return uid;
	}
	
	public String getLabel() {
		return label;
	}
	
	public CardType getCardType() {
		return cardType;
	}
}
