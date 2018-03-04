package clueless;

public enum CardsEnum {
	LOCATION_KITCHEN(0x01, "Kitchen", CardType.CARD_TYPE_LOCATION),
	LOCATION_BATHROOM(0x02, "Bathroom", CardType.CARD_TYPE_LOCATION),
	LOCATION_BEDROOM(0x03, "Bedroom", CardType.CARD_TYPE_LOCATION),
	LOCATION_COURTYARD(0x04, "Courtyard", CardType.CARD_TYPE_LOCATION),
	LOCATION_GAMEROOM(0x05, "Game Room", CardType.CARD_TYPE_LOCATION),
	LOCATION_STUDY(0x06, "Study", CardType.CARD_TYPE_LOCATION),
	LOCATION_DININGROOM(0x07, "Dining Room", CardType.CARD_TYPE_LOCATION),
	LOCATION_GARAGE(0x08, "Garage", CardType.CARD_TYPE_LOCATION),
	LOCATION_LIVINGROOM(0x09, "Living Room", CardType.CARD_TYPE_LOCATION),
	
	WEAPON_LEADPIPE(0x21, "Lead Pipe", CardType.CARD_TYPE_WEAPON),
	WEAPON_REVOLVER(0x22, "Revolver", CardType.CARD_TYPE_WEAPON),
	WEAPON_ROPE(0x23, "Rope", CardType.CARD_TYPE_WEAPON),
	WEAPON_DAGGER(0x24, "Dagger", CardType.CARD_TYPE_WEAPON),
	WEAPON_WRENCH(0x25, "Wrench", CardType.CARD_TYPE_WEAPON),
	WEAPON_CANDLESTICK(0x26, "Candlestick", CardType.CARD_TYPE_WEAPON),
	
	SUSPECT_PLUM(0x31, "Professor Plum", CardType.CARD_TYPE_SUSPECT),
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
