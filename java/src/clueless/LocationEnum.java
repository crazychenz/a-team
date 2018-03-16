package clueless;

public enum LocationEnum {
    LOCATION_STUDY(0x01, "Kitchen"),
    LOCATION_HALL(0x02, "Hall"),
    LOCATION_LOUNGE(0x03, "Lounge"),
    LOCATION_LIBRARY(0x04, "Library"),
    LOCATION_BILLIARDS(0x05, "Billiards"),
    LOCATION_DINING(0x06, "Dining"),
    LOCATION_CONSERVATORY(0x07, "Conservatory"),
    LOCATION_BALLROOM(0x08, "Ballroom"),
    LOCATION_KITCHEN(0x09, "Kitchen"),
    HALLWAY_STUDY_HALL(0x10, "Study -> Hall"),
    HALLWAY_HALL_LOUNGE(0x10, "Hall -> Lounge"),
    HALLWAY_STUDY_LIBRARY(0x10, "Study -> Library"),
    HALLWAY_HALL_BILLIARDS(0x10, "Hall -> Billiards"),
    HALLWAY_LOUNGE_DINING(0x10, "Lounge -> Dining"),
    HALLWAY_LIBRARY_BILLIARDS(0x10, "Library -> Billiards"),
    HALLWAY_BILLIARDS_DINING(0x10, "Billiards -> Dining"),
    HALLWAY_LIBRARY_CONSERVATORY(0x10, "Library -> Conservatory"),
    HALLWAY_BILLIARDS_BALLROOM(0x10, "Billiards -> Ballroom"),
    HALLWAY_DINING_KITCHEN(0x10, "Dining -> Kitchen"),
    HALLWAY_CONSERVATORY_BALLROOM(0x10, "Conservatory -> Ballroom"),
    HALLWAY_BALLROOM_KITCHEN(0x10, "Ballroom -> Kitchen");

    private final int uid;
    private final String label;

    LocationEnum(int uid, String label) {
        this.uid = uid;
        this.label = label;
    }

    public int getUid() {
        return uid;
    }

    public String getLabel() {
        return label;
    }
}
