package clueless;

import java.util.ArrayList;

public class WeaponCard extends Card {

    public static final WeaponCard WEAPON_LEADPIPE,
            WEAPON_REVOLVER,
            WEAPON_ROPE,
            WEAPON_DAGGER,
            WEAPON_WRENCH,
            WEAPON_CANDLESTICK;

    public static final ArrayList<WeaponCard> allCards = new ArrayList<>();

    static {
        allCards.add(WEAPON_LEADPIPE = new WeaponCard(0x21, "Lead Pipe"));
        allCards.add(WEAPON_REVOLVER = new WeaponCard(0x22, "Revolver"));
        allCards.add(WEAPON_ROPE = new WeaponCard(0x23, "Rope"));
        allCards.add(WEAPON_DAGGER = new WeaponCard(0x24, "Dagger"));
        allCards.add(WEAPON_WRENCH = new WeaponCard(0x25, "Wrench"));
        allCards.add(WEAPON_CANDLESTICK = new WeaponCard(0x26, "Candlestick"));
    }

    private WeaponCard(int id, String name) {
        super(id, name);
    }
}