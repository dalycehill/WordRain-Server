import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	private Player[] Players;
	private Head[] Heads;
	private Word[] Words;
	private MovingWords movingWords;
	
	public Server(Player[] p, Head[] h, Word[] w, MovingWords m) {
		this.Players = p;
		this.Heads = h;
		this.Words = w;
		this.movingWords = m;
	}
	
	public void run() {
		final int SERVER_PORT = 5556;
		
		try {
			ServerSocket server = new ServerSocket(SERVER_PORT);
			System.out.println("Waiting for clients to connect...");
			while(true) {
				Socket s = server.accept();
				System.out.println("client connected");
				
				Service myService = new Service(s, Players, Heads, Words, movingWords);
				Thread t = new Thread(myService);
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
