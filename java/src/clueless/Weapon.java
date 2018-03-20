/** */
package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author tombo */
public class Weapon implements Serializable {

    private static final Logger logger = LogManager.getLogger(Weapon.class);

    private CardsEnum weapon;
    private CardsEnum current_location;

    private static HashMap<CardsEnum, Weapon> enumMap = new HashMap<>();

    static {
        for (CardsEnum value : CardsEnum.values()) {
            if (value.getCardType() == CardType.CARD_TYPE_WEAPON) {
                Weapon weapon = new Weapon(value);
                enumMap.put(value, weapon);
            }
        }
    }

    public static Weapon getByEnum(CardsEnum value) {
        return enumMap.get(value);
    }

    public static ArrayList<Weapon> getCollection() {
        return new ArrayList<Weapon>(enumMap.values());
    }

    public Weapon(CardsEnum weapon) {
        this.setWeapon(weapon);
        logger.debug("Creating weapon " + weapon.toString());
    }

    public void SetLocation(CardsEnum location) {
        setCurrent_location(location);
    }

    /** @return the weapon */
    public CardsEnum getWeapon() {
        return weapon;
    }

    /** @param weapon the weapon to set */
    public final void setWeapon(CardsEnum weapon) {
        this.weapon = weapon;
    }

    /** @return the current_location */
    public CardsEnum getCurrent_location() {
        return current_location;
    }

    /** @param current_location the current_location to set */
    public void setCurrent_location(CardsEnum current_location) {
        this.current_location = current_location;
    }

    /*
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeInt(weapon.getUid());
        out.writeInt(current_location.getUid());
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        weapon = CardsEnum.getByUid(in.readInt());
        current_location = CardsEnum.getByUid(in.readInt());
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required.");
    }*/
}
