package clueless;

import java.util.ArrayList;

public class GameBoard {

    public final SuspectMap suspects;
    public final WeaponMap weapons;
    public final LocationMap locations;
    private final ArrayList<Card> faceUpCards;

    private Envelope envelope;

    public GameBoard() {
        // Locations must be created first so we have places to put things.
        locations = new LocationMap();
        suspects = new SuspectMap(this);
        weapons = new WeaponMap();

        faceUpCards = new ArrayList<Card>();
    }

    public void dealCards(PlayerMgr players) {
        Dealer dealer = new Dealer(System.currentTimeMillis());
        envelope = dealer.populateEnvelope();
        dealer.dealCards(players, faceUpCards);
    }

    public ArrayList<Card> getFaceUpCards() {
        return faceUpCards;
    }

    public Suspect getSuspectByEnum(SuspectCard value) {
        return suspects.getByEnum(value);
    }

    public ArrayList<Suspect> getAllSuspects() {
        return suspects.getAllSuspects();
    }

    public AvailableSuspects getAvailableSuspects() {
        return suspects.getAvailableSuspects();
    }

    public Weapon getWeaponByEnum(WeaponCard value) {
        return weapons.getByEnum(value);
    }

    public ArrayList<Weapon> getAllWeapons() {
        return weapons.getAllWeapons();
    }

    // public Location getLocationByEnum(Location value) {
    //    return locations.getByEnum(value);
    // }

    public boolean accuse(Suggestion suggestion) {
        return envelope.matchEnvelope(suggestion);
    }
}
