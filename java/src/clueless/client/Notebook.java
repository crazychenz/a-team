/** */
package clueless.client;

import clueless.Card;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Notebook {

    private static final Logger logger = LogManager.getLogger(Notebook.class);
    private HashMap<Card, String> notes;

    public Notebook() {
        notes = new HashMap<Card, String>();
    }

    public void makeNote(Card card, String note) {
        note = note.replace("note", "");
        note = note.replace(card.getName(), "");
        if (notes.get(card) != null) {
            notes.replace(card, notes.get(card).concat(note));
        } else {
            notes.put(card, note);
        }
    }

    public String getNotes() {
        String toReturn = "";
        for (Map.Entry<Card, String> entry : notes.entrySet()) {
            Card key = entry.getKey();
            String value = entry.getValue();
            toReturn += key.getName() + ":\t" + value + "\n";
        }
        return toReturn;
    }
}
