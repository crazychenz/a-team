/**
 * 
 */
package clueless;

/**
 * @author tombo
 *
 */
public class Envelope {
	private Card suspect;
	private Card location;
	private Card weapon;
	
	public Envelope(Card suspect, Card location, Card weapon) {
		this.suspect = suspect;
		this.location = location;
		this.weapon = weapon;
	}
	
	public Envelope() {
		
	}
	
	public void setSuspect(Card suspect) {
		this.suspect = suspect;
	}
	
	public void setLocation(Card location) {
		this.location = location;
	}
	
	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}
}
