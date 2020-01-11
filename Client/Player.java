import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JLabel;

public class Player extends Sprite {
	
	final static int SERVER_PORT = 5556;
	
	//variables
	private JLabel PlayerLabel;
	private int score, lives;

	//setters
	public void setPlayerLabel(JLabel p) {
		PlayerLabel = p;
	}
    public void setLives(int temp) {
		this.lives = temp;
	}
    public void setScore(int temp) {
		this.score = temp;
    }
	
	//getters
	public int getLives() {
		return lives;
	}
	public int getScore() {
        return score;
    }
	
	//constructor
	public Player() {
		super("pictures/player.png",0,0,80,100);
		this.score = 0;
    	this.lives = 3;
	}
	
	//move player function
	public void movePlayer(KeyEvent e) {
		int plX = this.spriteX;
		
		//move player left/right using arrow keys
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			//don't let the player go off screen
			if (plX > 0) {
				plX -= WordRainProperties.CHARACTER_STEP;
			} 
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//don't let the player go off screen
			if (plX < WordRainProperties.SCREEN_WIDTH - this.spriteW) {
				plX += WordRainProperties.CHARACTER_STEP;
			} 
		}

		//set location of player
//		this.setSpriteX(plX);
//		PlayerLabel.setLocation(this.spriteX, this.spriteY);
		
		Socket s;
		try {
			s = new Socket("localhost", SERVER_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s.getOutputStream();
			PrintWriter out = new PrintWriter(outstream); 
			
			String command = "UPDATEPLAYER " + WordRainProperties.PLAYER_NUM +" "+ plX +" "+ score +" "+ lives + "\n";
			System.out.println("Sending: " + command);
			out.println(command);
			out.flush();
			
			s.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
}