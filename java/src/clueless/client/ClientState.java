package clueless.client;

import clueless.*;
import java.util.ArrayList;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a client's local state of the Game
 *
 * @author ateam
 */
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

    /** Default constructor */
    public ClientState() {
        setCards(new ArrayList<Card>());
        setFaceUpCards(new ArrayList<Card>());
        setDisproveCards(new ArrayList<Card>());
    }

    /**
     * Fetch the most recent game state
     *
     * @return the gameState
     */
    public GameStatePulse getGameState() {
        return gameState;
    }

    /**
     * Set the game state
     *
     * @param gameState the gameState to set
     */
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

    /**
     * Fetch available suspects
     *
     * @return the availableSuspects
     * @todo AvailableSuspects wrapper should be striped at this point
     */
    public AvailableSuspects getAvailableSuspects() {
        return availableSuspects;
    }

    /**
     * Set the AvailableSuspects
     *
     * @param availableSuspects the availableSuspects to set
     */
    public void setAvailableSuspects(AvailableSuspects availableSuspects) {
        this.availableSuspects = availableSuspects;
    }

    /**
     * Check if this client is registered/configured to play on server.
     *
     * @return the configured
     */
    public boolean isConfigured() {
        return configured;
    }

    /**
     * Set that this client is registered/configured to play on server
     *
     * @param configured the configured to set
     */
    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    /**
     * Fetch the SuspectCard this client is registered with.
     *
     * @return the mySuspect
     */
    public SuspectCard getMySuspect() {
        return mySuspect;
    }

    /**
     * Set suspect that this client represents
     *
     * @param mySuspect the mySuspect to set
     */
    public void setMySuspect(SuspectCard mySuspect) {
        this.mySuspect = mySuspect;
    }

    /**
     * Check if this client is the current player to move, suggest, or accuse
     *
     * @return the myTurn
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Set the client as the current player to move, suggest, or accuse
     *
     * @param myTurn the myTurn to set
     */
    private void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    /**
     * Check if the client has moved its player this turn.
     *
     * @return the moved
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * Set whether the player has moved this turn
     *
     * @param moved the moved to set
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * Fetch an ArrayList of all the player cards
     *
     * @return the cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Set the ArrayList of all the player cards in the client.
     *
     * @param cards the cards to set
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Get all the face up cards on the GameBoard
     *
     * @return the faceUpCards
     */
    public ArrayList<Card> getFaceUpCards() {
        return faceUpCards;
    }

    /**
     * Set the face up cards on the gameboard for this client
     *
     * @param faceUpCards the faceUpCards to set
     */
    public void setFaceUpCards(ArrayList<Card> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    /**
     * Get the location of this client's player's suspect.
     *
     * @return the myLocation
     */
    public Integer getMyLocation() {
        return myLocation;
    }

    /**
     * Set the location of the client's player's suspect.
     *
     * @param myLocation the myLocation to set
     */
    public void setMyLocation(Integer myLocation) {
        this.myLocation = myLocation;
    }

    // This isn't the best way to do this.  Should probably be moved/handled differently.

    /**
     * Disprove a suggestion
     *
     * @param cards Current suggestion
     * @param active Whether the client is expected to perform a disprove now
     */
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

    /**
     * Display conclusion of the suggestion sequence
     *
     * @param card The card that disproves the suggestion
     * @param active Whether this client is the suggester or not.
     */
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

    /**
     * Fetch the list of possible cards to use to disprove a suggestion.
     *
     * @return the disproveCards
     */
    public ArrayList<Card> getDisproveCards() {
        return disproveCards;
    }

    /**
     * Set the list of possible cards to use to disprove a suggestion.
     *
     * @param disproveCards the disproveCards to set
     */
    public void setDisproveCards(ArrayList<Card> disproveCards) {
        this.disproveCards = disproveCards;
    }

    /**
     * Check if client is in a disprove state.
     *
     * @return the disproving
     */
    public boolean isDisproving() {
        return disproving;
    }

    /**
     * Set the disproving state.
     *
     * @param disproving the disproving to set
     */
    public void setDisproving(boolean disproving) {
        this.disproving = disproving;
        setDisproveCards(new ArrayList<Card>());
    }

    /**
     * Check if a player has already suggested this turn.
     *
     * @return the suggested
     */
    public boolean isSuggested() {
        return suggested;
    }

    /**
     * Set that a player has suggested this turn.
     *
     * @param suggested the suggested to set
     */
    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }
}
