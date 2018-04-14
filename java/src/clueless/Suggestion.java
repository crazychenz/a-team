/** */
package clueless;

import java.io.Serializable;

/** @author tombo */
public class Suggestion implements Serializable {

    /** */
    private SuspectCard suspect;

    private RoomCard room;
    private WeaponCard weapon;

    public Suggestion(SuspectCard suspect, RoomCard room, WeaponCard weapon) {
        this.suspect = suspect;
        this.room = room;
        this.weapon = weapon;
    }

    public void normalize() {
        suspect = (SuspectCard) SuspectCard.fetch(suspect.getId());
        room = (RoomCard) RoomCard.fetch(room.getId());
        weapon = (WeaponCard) WeaponCard.fetch(weapon.getId());
    }

    public Suggestion(Card c1, Card c2, Card c3) throws Exception {
        guess(c1);
        guess(c2);
        guess(c3);
    }

    private void guess(Card c) throws Exception {
        if (c instanceof SuspectCard) {
            if (suspect != null) {
                throw new Exception("Multple suspects in Suggestion");
            }
            suspect = (SuspectCard) c;
            return;
        }

        if (c instanceof RoomCard) {
            if (room != null) {
                throw new Exception("Multple rooms in Suggestion");
            }
            room = (RoomCard) c;
            return;
        }

        if (c instanceof WeaponCard) {
            if (weapon != null) {
                throw new Exception("Multple weapons in Suggestion");
            }
            weapon = (WeaponCard) c;
            return;
        }
    }

    /** @return the cards */
    public SuspectCard getSuspect() {
        return suspect;
    }

    public RoomCard getRoom() {
        return room;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }

    public boolean contains(Card c) {
        return room.equals(c) || weapon.equals(c) || suspect.equals(c);
    }

    @Override
    public String toString() {
        return getSuspect() + " " + getRoom() + " " + getWeapon();
    }
}
