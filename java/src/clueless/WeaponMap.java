package clueless;

import java.util.ArrayList;
import java.util.HashMap;

public class WeaponMap {
    private HashMap<WeaponCard, Weapon> weapons = new HashMap<>();

    public WeaponMap() {
        for (WeaponCard card : WeaponCard.allCards) {
            Weapon weapon = new Weapon(card);
            weapons.put(card, weapon);
        }
    }

    public Weapon getByEnum(WeaponCard value) {
        return weapons.get(value);
    }

    public ArrayList<Weapon> getAllWeapons() {
        return new ArrayList<Weapon>(weapons.values());
    }
}
