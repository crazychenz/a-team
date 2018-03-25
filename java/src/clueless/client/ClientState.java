package clueless.client;

import clueless.*;
import java.util.ArrayList;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientState {

    private AvailableSuspects availableSuspects;
    private boolean configured = false;
    private GameStatePulse gameState;
    private SuspectCard mySuspect;
    private boolean myTurn = false;
    private boolean alerted = false;
    private boolean moved = false;
    private boolean suggested = false;
    private Integer myLocation;
    private ArrayList<Card> cards;
    private ArrayList<Card> faceUpCards;
    private ArrayList<Card> disproveCards;
    private boolean disproving = false;

    private static final Logger logger = LogManager.getLogger(ClientState.class);

    public ClientState() {
        setCards(new ArrayList<Card>());
        setFaceUpCards(new ArrayList<Card>());
        setDisproveCards(new ArrayList<Card>());
    }

    /** @return the gameState */
    public GameStatePulse getGameState() {
        return gameState;
    }

    /** @param gameState the gameState to set */
    public void setGameState(GameStatePulse gameState) {
        this.gameState = gameState;

        if (!gameState.isGameActive()) {
            setAvailableSuspects(gameState.getAvailableSuspects());
            return;
        }

        if (gameState.getActiveSuspect().equals(mySuspect)) {
            setMyTurn(true);
            if (!alerted) {
                logger.debug(getMySuspect());
                System.out.println("You are the active player!  Perform an action.\n");
                setMoved(false); // reset move so the player can move again when their turn begins
                setSuggested(false);
                alerted = true;
            }
        } else {
            setMyTurn(false);
            if (alerted) {
                alerted = false;
            }
        }

        // Bleh
        for (Entry<SuspectCard, Integer> entry : gameState.getSuspectLocations().entrySet()) {
            if (entry.getKey().equals(mySuspect)) {
                setMyLocation(entry.getValue());
            }
        }

        setCards(gameState.getCards());
        setFaceUpCards(gameState.getFaceUpCards());
    }

    /** @return the availableSuspects */
    public AvailableSuspects getAvailableSuspects() {
        return availableSuspects;
    }

    /** @param availableSuspects the availableSuspects to set */
    public void setAvailableSuspects(AvailableSuspects availableSuspects) {
        this.availableSuspects = availableSuspects;
    }

    /** @return the configured */
    public boolean isConfigured() {
        return configured;
    }

    /** @param configured the configured to set */
    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    /** @return the mySuspect */
    public SuspectCard getMySuspect() {
        return mySuspect;
    }

    /** @param mySuspect the mySuspect to set */
    public void setMySuspect(SuspectCard mySuspect) {
        this.mySuspect = mySuspect;
    }

    /** @return the myTurn */
    public boolean isMyTurn() {
        return myTurn;
    }

    /** @param myTurn the myTurn to set */
    private void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    /** @return the moved */
    public boolean isMoved() {
        return moved;
    }

    /** @param moved the moved to set */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /** @return the cards */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /** @param cards the cards to set */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /** @return the faceUpCards */
    public ArrayList<Card> getFaceUpCards() {
        return faceUpCards;
    }

    /** @param faceUpCards the faceUpCards to set */
    public void setFaceUpCards(ArrayList<Card> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    /** @return the myLocation */
    public Integer getMyLocation() {
        return myLocation;
    }

    /** @param myLocation the myLocation to set */
    public void setMyLocation(Integer myLocation) {
        this.myLocation = myLocation;
    }

    // This isn't the best way to do this.  Should probably be moved/handled differently.
    public void disprove(Suggestion cards, boolean active) {
        logger.debug(getMySuspect());
        if (active) {
            setDisproving(true);
            String toPrint = "";
            boolean found = false;

            System.out.println("You must disprove a suggestion of the following cards!");

            System.out.println("\t" + cards);

            // Cherry pick the relevant cards to disapprove with.
            for (Card myCard : getCards()) {
                if (cards.contains(myCard)) {
                    found = true;
                    disproveCards.add(myCard);
                    toPrint += "\t" + myCard.getName() + "\n";
                }
            }

            if (found) {
                System.out.println("Which card would you like to show?");
                System.out.println(toPrint);
                System.out.println(
                        "Disprove the suggestion using the disprove command with the card name!");
            } else {
                System.out.println("You are unable to disprove the suggestion!");
                System.out.println(
                        "Let the player know you cannot disprove the suggestion using the disprove command with no card name!");
            }

        } else {
            System.out.println(
                    "Somebody is trying to disprove a suggestion of the following cards:\n");
            System.out.println("\t" + cards);
        }
    }

    // This isn't the best way to do this.  Should probably be moved/handled differently.
    public void suggestResponse(Card card, boolean active) {

        System.out.println("The suggestion has been completed!\n");

        if (card == null) {
            System.out.println("The suggestion was unable to be disproven!");
            return;
        }

        if (active) {
            System.out.println("The following card was disproven: " + card.getName());
        } else {
            System.out.println("The suggestion was disproved!");
        }
    }

    /** @return the disproveCards */
    public ArrayList<Card> getDisproveCards() {
        return disproveCards;
    }

    /** @param disproveCards the disproveCards to set */
    public void setDisproveCards(ArrayList<Card> disproveCards) {
        this.disproveCards = disproveCards;
    }

    /** @return the disproving */
    public boolean isDisproving() {
        return disproving;
    }

    /** @param disproving the disproving to set */
    public void setDisproving(boolean disproving) {
        this.disproving = disproving;
        setDisproveCards(new ArrayList<Card>());
    }

    /** @return the suggested */
    public boolean isSuggested() {
        return suggested;
    }

    /** @param suggested the suggested to set */
    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }
}
