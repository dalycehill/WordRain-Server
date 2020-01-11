import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Server implements Runnable {

	final static int CLIENT_PORT = 5656;
	final static int SERVER_PORT = 5556;
	
	private Player[] Players;
	private Head[] Heads;
	private Word[] Words;
	private MovingWords movingWords;
	
	private JLabel[] WordLabels, PlayerLabels, HeadLabels, scoreLabels, livesLabels;
	private JButton playButton;
	
	public Server(Player[] p, Head[] h, Word[] w, MovingWords m, JLabel[] hL, JLabel[] pL, JLabel[] wL, JLabel sL[], JLabel lL[], JButton pB) {
		this.Players = p;
		this.Heads = h;
		this.Words = w;
		this.movingWords = m;
		this.HeadLabels = hL;
		this.PlayerLabels = pL;
		this.WordLabels = wL;
		this.scoreLabels = sL;
		this.livesLabels = lL;
		this.playButton = pB;
	}

	public void run() {
		
		try {
			ServerSocket server = new ServerSocket(CLIENT_PORT);
			System.out.println("Waiting for clients to connect...");
			while(true) {
				Socket s = server.accept();
				System.out.println("client connected");
				
				Service myService = new Service(s, Players, Heads, Words, movingWords, HeadLabels, PlayerLabels, WordLabels, scoreLabels, livesLabels, playButton);
				Thread t = new Thread(myService);
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}