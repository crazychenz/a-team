package clueless;

import java.util.ArrayList;
import java.util.HashMap;

public class WeaponMap {
    private HashMap<CardsEnum, Weapon> weapons = new HashMap<>();

    public WeaponMap() {
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_WEAPON) {
                Weapon weapon = new Weapon(value);
                weapons.put(value, weapon);
            }
        }
    }

    public Weapon getByEnum(CardsEnum value) {
        return weapons.get(value);
    }

    public ArrayList<Weapon> getAllWeapons() {
        return new ArrayList<Weapon>(weapons.values());
    }
}
