import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JLabel;

public class Head extends Sprite {

	final static int SERVER_PORT = 5556;
	
	//variables
	private JLabel HeadLabel;

	//setter
	public void setHeadLabel(JLabel h) {
		HeadLabel = h;
	}

	//constructor
	public Head() {
		super("pictures/head.png",0,0,80,100);
	}
	
	//move head function
	public void moveHead(KeyEvent e) {
		int hX = this.spriteX;
		
		//move head left & right using arrow keys
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			//don't let the head go off screen
			if (hX > 0) {
				hX -= WordRainProperties.CHARACTER_STEP;
			} 
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//don't let the head go off screen
			if (hX < WordRainProperties.SCREEN_WIDTH - this.spriteW) {
				hX += WordRainProperties.CHARACTER_STEP;
			} 
		}

		//set location of head
//		this.setSpriteX(hX);
//		HeadLabel.setLocation(this.spriteX, this.spriteY);
		
		Socket s;
		try {
			s = new Socket("localhost", SERVER_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s.getOutputStream();
			PrintWriter out = new PrintWriter(outstream); 
			
			String command = "UPDATEHEAD " + WordRainProperties.PLAYER_NUM + " " + hX +" "+ this.getSpriteName() + "\n";
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