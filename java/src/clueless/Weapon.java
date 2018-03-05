/**
 * 
 */
package clueless;


/**
 * @author tombo
 *
 */
public class Weapon {
	private CardsEnum weapon;
	private CardsEnum current_location;
	
	public Weapon(CardsEnum weapon) {
		this.weapon = weapon;
		System.out.println("Creating weapon " + weapon.toString());
	}
	
	public void SetLocation(CardsEnum location) {
		current_location = location;
	}
}
