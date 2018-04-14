package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStatePulse implements Serializable {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private boolean gameActive;
    private SuspectCard activeSuspect;
    private HashMap<SuspectCard, Integer> suspectLocations;
    private HashMap<WeaponCard, Integer> weaponLocations;
    private AvailableSuspects availableSuspects;
    private ArrayList<Card> cards;
    private ArrayList<Card> faceUpCards;

    public GameStatePulse(boolean started, GameBoard board, PlayerMgr players, Player player) {
        setGameActive(started);

        suspectLocations = new HashMap<>();
        for (Suspect suspect : board.getAllSuspects()) {
            suspectLocations.put(suspect.getSuspect(), suspect.getCurrent_location().getId());
        }

        weaponLocations = new HashMap<>();
        for (Weapon weapon : board.getAllWeapons()) {
            if (weapon.getCurrent_location() != null) {
                // TODO: Make sure weapons always have a location.
                weaponLocations.put(weapon.getWeapon(), weapon.getCurrent_location().getId());
            }
        }

        setAvailableSuspects(board.getAvailableSuspects());
        // setSuspectLocations(board.getAllSuspects());
        // setWeaponLocations(board.getAllWeapons());

        if (started) {
            Player current = players.current();
            activeSuspect = null;
            if (current != null) {
                setActiveSuspect(players.current().getSuspect());
            }

            cards = null;
            if (player != null) {
                setCards(player.getCards());
            }
            setFaceUpCards(board.getFaceUpCards());
        }
    }

    public void normalize() {
        if (activeSuspect != null) {
            activeSuspect = SuspectCard.fetch(activeSuspect.getId());
        }

        if (suspectLocations != null) {
            HashMap<SuspectCard, Integer> _suspectLocations;
            _suspectLocations = new HashMap<SuspectCard, Integer>();
            for (Map.Entry<SuspectCard, Integer> entry : suspectLocations.entrySet()) {
                SuspectCard card = (SuspectCard) entry.getKey();
                card = SuspectCard.fetch(card.getId());
                _suspectLocations.put(card, (Integer) entry.getValue());
            }
            suspectLocations = _suspectLocations;
        }

        if (weaponLocations != null) {
            HashMap<WeaponCard, Integer> _weaponLocations;
            _weaponLocations = new HashMap<WeaponCard, Integer>();
            for (Map.Entry<WeaponCard, Integer> entry : weaponLocations.entrySet()) {
                WeaponCard card = (WeaponCard) entry.getKey();
                card = WeaponCard.fetch(card.getId());
                _weaponLocations.put(card, (Integer) entry.getValue());
            }
            weaponLocations = _weaponLocations;
        }

        if (availableSuspects != null) {
            availableSuspects.normalize();
        }

        if (cards != null) {
            ArrayList<Card> _cards = new ArrayList<Card>();
            for (Card card : cards) {
                _cards.add(Card.fetch(card.getId()));
            }
            cards = _cards;
        }

        if (faceUpCards != null) {
            ArrayList<Card> _faceUpCards = new ArrayList<Card>();
            for (Card card : faceUpCards) {
                _faceUpCards.add(Card.fetch(card.getId()));
            }
            faceUpCards = _faceUpCards;
        }
    }

    /** @return the gameActive */
    public boolean isGameActive() {
        return gameActive;
    }

    /** @param gameActive the gameActive to set */
    private void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    /** @return the activeSuspect */
    public SuspectCard getActiveSuspect() {
        return activeSuspect;
    }

    /** @param activeSuspect the activeSuspect to set */
    private void setActiveSuspect(SuspectCard activeSuspect) {
        this.activeSuspect = activeSuspect;
    }

    /** @return the suspectLocations */
    public HashMap<SuspectCard, Integer> getSuspectLocations() {
        return suspectLocations;
    }

    /** @param suspectLocations the suspectLocations to set */
    private void setSuspectLocations(HashMap<SuspectCard, Integer> suspectLocations) {
        this.suspectLocations = suspectLocations;
    }

    /** @return the weaponLocations */
    public HashMap<WeaponCard, Integer> getWeaponLocations() {
        return weaponLocations;
    }

    /** @param weaponLocations the weaponLocations to set */
    private void setWeaponLocations(HashMap<WeaponCard, Integer> weaponLocations) {
        this.weaponLocations = weaponLocations;
    }

    /** @return the availableSuspects */
    public AvailableSuspects getAvailableSuspects() {
        return availableSuspects;
    }

    /** @param availableSuspects the availableSuspects to set */
    private void setAvailableSuspects(AvailableSuspects availableSuspects) {
        this.availableSuspects = availableSuspects;
    }

    /** @return the cards */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /** @param cards the cards to set */
    private void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /** @return the faceUpCards */
    public ArrayList<Card> getFaceUpCards() {
        return faceUpCards;
    }

    /** @param faceUpCards the cards to set */
    private void setFaceUpCards(ArrayList<Card> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }
}
