package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeaponMap {
    private HashMap<WeaponCard, Weapon> weapons = new HashMap<>();

    public WeaponMap() {
        for (WeaponCard card : WeaponCard.allCards) {
            Weapon weapon = new Weapon(card);
            weapons.put(card, weapon);
        }
    }

    public Weapon getByCard(WeaponCard value) {
        // Bleh
        for (Map.Entry<WeaponCard, Weapon> entry : weapons.entrySet()) {
            if (entry.getKey().equals(value)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public ArrayList<Weapon> getAllWeapons() {
        return new ArrayList<Weapon>(weapons.values());
    }
}
