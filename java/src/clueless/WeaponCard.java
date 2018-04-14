package clueless;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a WeaponCard.
 *
 * @author ateam
 */
public class WeaponCard extends Card {

    /** Enumeration of all possible WeaponCards */
    public static final WeaponCard WEAPON_LEADPIPE,
            WEAPON_REVOLVER,
            WEAPON_ROPE,
            WEAPON_DAGGER,
            WEAPON_WRENCH,
            WEAPON_CANDLESTICK;

    public static final ArrayList<WeaponCard> allCards = new ArrayList<>();

    static {
        allCards.add(WEAPON_LEADPIPE = new WeaponCard(0x21, "Pipe"));
        Card.register(WEAPON_LEADPIPE);
        allCards.add(WEAPON_REVOLVER = new WeaponCard(0x22, "Revolver"));
        Card.register(WEAPON_REVOLVER);
        allCards.add(WEAPON_ROPE = new WeaponCard(0x23, "Rope"));
        Card.register(WEAPON_ROPE);
        allCards.add(WEAPON_DAGGER = new WeaponCard(0x24, "Dagger"));
        Card.register(WEAPON_DAGGER);
        allCards.add(WEAPON_WRENCH = new WeaponCard(0x25, "Wrench"));
        Card.register(WEAPON_WRENCH);
        allCards.add(WEAPON_CANDLESTICK = new WeaponCard(0x26, "Candlestick"));
        Card.register(WEAPON_CANDLESTICK);
    }

    private WeaponCard(int id, String name) {
        super(id, name);
    }

    public static Iterable<WeaponCard> cards() {
        return Collections.unmodifiableList(allCards);
    }

    public static WeaponCard fetch(int id) {
        return (WeaponCard) Card.fetch(id);
    }
}
