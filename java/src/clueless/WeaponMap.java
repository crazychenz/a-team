package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the management of all Weapon objects.
 *
 * @author ateam
 */
public class WeaponMap {
    private HashMap<WeaponCard, Weapon> weapons = new HashMap<>();

    /** Default Constructor, creates all weapon instances. */
    public WeaponMap() {
        for (WeaponCard card : WeaponCard.cards()) {
            Weapon weapon = new Weapon(card);
            weapons.put(card, weapon);
        }
    }

    /**
     * Lookup Weapon by WeaponCard
     *
     * @param value WeaponCard used to lookup Weapon
     * @return Weapon or null if none found.
     */
    public Weapon getByCard(WeaponCard value) {
        // Bleh
        for (Map.Entry<WeaponCard, Weapon> entry : weapons.entrySet()) {
            if (entry.getKey().equals(value)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Fetch ArrayList of all Weapon objects.
     *
     * @return ArrayList of all Weapon objects.
     */
    public ArrayList<Weapon> getAllWeapons() {
        return new ArrayList<Weapon>(weapons.values());
    }
}
