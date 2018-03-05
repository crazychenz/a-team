import clueless.Server;

public class ServerRun {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting server");
		Server a = new Server();
		a.listenForConnections();
	}

}
