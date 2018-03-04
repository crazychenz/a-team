package clueless;

public class Player {
	private ClientThread thread;
	private CardsEnum suspect;
	private String username;
	
	public Player(ClientThread thread, CardsEnum suspect) {
		this.thread = thread;
		this.suspect = suspect;
	}
	
	public ClientThread getThread() {
		return thread;
	}
	
	public CardsEnum getSuspect() {
		return suspect;
	}
}
