package clueless;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientState {

    private AvailableSuspects availableSuspects;
    private boolean configured = false;
    private GameStatePulse gameState;
    private CardsEnum mySuspect;
    private boolean myTurn = false;
    private boolean alerted = false;
    private boolean moved = false;
    private CardsEnum myLocation;
    private ArrayList<Card> cards;
    private ArrayList<Card> faceUpCards;
    private ArrayList<Card> disproveCards;
    private boolean disproving = false;

    private static final Logger logger = LogManager.getLogger(CLIEventHandler.class);

    ClientState() {
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
        if (gameState.isGameActive() && gameState.getActiveSuspect() == mySuspect) {
            setMyTurn(true);
            if (!alerted) {
                System.out.println("You are the active player!  Perform an action.\n");
                setMoved(false); // reset move so the player can move again when their turn begins
                alerted = true;
            }
        } else {
            setMyTurn(false);
            if (alerted) {
                alerted = false;
            }
        }

        for (Suspect suspect : gameState.getSuspectLocations()) {
            if (suspect.getSuspect() == mySuspect) {
                setMyLocation(suspect.getCurrent_location());
            }
        }

        setAvailableSuspects(gameState.getAvailableSuspects());
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
    public CardsEnum getMySuspect() {
        return mySuspect;
    }

    /** @param mySuspect the mySuspect to set */
    public void setMySuspect(CardsEnum mySuspect) {
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
    public CardsEnum getMyLocation() {
        return myLocation;
    }

    /** @param myLocation the myLocation to set */
    public void setMyLocation(CardsEnum myLocation) {
        this.myLocation = myLocation;
    }

    // This isn't the best way to do this.  Should probably be moved/handled differently.
    public void disprove(CardWrapper cards) {
        setDisproving(true);
        String toPrint = "";
        boolean found = false;
        System.out.println("You must disprove a suggestion of the following cards!\n");
        for (CardsEnum card : cards.getCards()) {
            System.out.println("\t" + card.getLabel() + "\n");
        }

        // Yuck this is ugly
        for (CardsEnum card : cards.getCards()) {
            for (Card myCard : getCards()) {
                if (myCard.getCardEnum() == card) {
                    if (!found) {
                        found = true;
                    }
                    disproveCards.add(myCard);
                    toPrint += "\t" + myCard.getCardEnum().getLabel() + "\n";
                }
            }
        }
        if (found) {
            System.out.println("Which card would you like to show?\n");
            System.out.println(toPrint);
            System.out.println(
                    "Disprove the suggestion using the disprove command with the card name!\n");
        } else {
            System.out.println("You are unable to disprove the suggestion!\n");
            System.out.println(
                    "Let the player know you cannot disprove the suggestion using the disprove command with no card name!\n");
        }
    }

    // This isn't the best way to do this.  Should probably be moved/handled differently.
    public void suggestResponse(CardWrapper cards) {
        String toPrint = "";
        System.out.println("Your suggestion has been completed!\n");
        if (cards.getCards().size() == 1) {
            System.out.println(
                    "The following card was disproven: "
                            + cards.getCards().get(0).getLabel()
                            + "\n");
        } else {
            System.out.println("Your suggestion was unable to be disproven!\n");
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
}
