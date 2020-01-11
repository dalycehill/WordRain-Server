import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;


//processing routine on server (B)
public class Service implements Runnable {
	final int CLIENT_PORT = 5656;

	private Socket s;
	private Scanner in;
	
	private Player[] Players;
	private Head[] Heads;
	private Word[] Words;
	private MovingWords movingWords;

	public Service (Socket aSocket, Player[] p, Head[] h, Word[] w, MovingWords m) {
		this.s = aSocket;
		this.Players = p;
		this.Heads = h;
		this.Words = w;
		this.movingWords = m;
	}
	
	public void run() {
		
		try {
			in = new Scanner(s.getInputStream());
			processRequest( );
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//processing the requests
	public void processRequest () throws IOException {
		//if next request is empty then return
		while(true) {
			if(!in.hasNext( )){
				return;
			}
			String command = in.next();
			if (command.equals("Quit")) {
				return;
			} else {
				executeCommand(command);
			}
		}
	}
	
	public void executeCommand(String command) throws IOException{
		/*
		if (command.equals("GETPLAYER")) {
			int playerNo = in.nextInt();
			System.out.println("Player "+playerNo);
			
			//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			String commandOut = "PLAYERDATA "+ playerNo + " " + Players[playerNo].getSpriteX() +"\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
			
			s2.close();
		}
		*/
		if (command.equals("GETPLAYERS")) {			
			//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			String commandOut = "PLAYERDATA ";
			for (int i=0;i<2;i++) {
				commandOut += Players[i].getSpriteX() +" "+ Players[i].getScore() +" "+ Players[i].getLives() +" ";
			}
			commandOut += "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
			
			s2.close();
		}
		
		if (command.equals("UPDATEPLAYER")) {
			int playerNo = in.nextInt();
			int playerX = in.nextInt();
			int playerScore = in.nextInt();
			int playerLives = in.nextInt();
			
			Players[playerNo].setSpriteX(playerX);
			Players[playerNo].setScore(playerScore);
			Players[playerNo].setLives(playerLives);
			
			System.out.println("Player " + playerNo +" updated: " + playerX);
		}
		
		if (command.equals("GETHEADS")) {
			//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			String commandOut = "HEADDATA ";
			for (int i=0; i<2; i++) {
				commandOut += Heads[i].getSpriteX()  +" "+ Heads[i].getSpriteName() +" ";
			}
			commandOut += "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s2.close();
		}
		
		if (command.equals("UPDATEHEAD")) {
			int playerNo = in.nextInt();
			int headX = in.nextInt();
			String headName = in.next();
			
			Heads[playerNo].setSpriteX(headX);
			Heads[playerNo].setSpriteName(headName);
			System.out.println("Head updated: " + headX + headName);
		}
		
		if (command.equals("GETMOVINGWORDS")) {
			//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			//send out moving words data
			String commandOut = "MOVINGWORDSDATA ";
			for (int i=0;i<23;i++) {
				commandOut += Words[i].getSpriteX() +" "+ Words[i].getSpriteY() +" "+ Words[i].getActive() +" "+
								Words[i].getMoving() +" "+ Words[i].getWordHit() +" "+ Words[i].getWordComplete() +" "+ Words[i].getWordNotComplete() 
								+" "+ Words[i].getVisible() +" "+ movingWords.getCurrentWord() +" "+ movingWords.getRightD() +" "+ movingWords.getLeftD() 
								+" "+ movingWords.getMiddleD() +" "+ movingWords.getLeftWord() +" "+ movingWords.getMiddleWord() +" "+ movingWords.getRightWord() 
								+" "+ movingWords.getLeftDistance() +" "+ movingWords.getMiddleDistance() +" "+ movingWords.getRightDistance() +" ";
			} 
			
			//send out head data to update labels
			for (int i=0; i<2; i++) {
				commandOut += Players[i].getScore() +" "+ Players[i].getLives() +" ";
			}
//			commandOut += movingWords.getTypedWord() +" ";
			commandOut += "\n";
			System.out.println("Sending: " + commandOut);
			//System.out.println("TYUIOXDCFGHJ: " + movingWords.getTypedWord());
			out.println(commandOut);
			out.flush();
			
			s2.close();
		}
		
		if (command.equals("UPDATETYPEDWORD")) {
			if (in.hasNext()) {
				//update typed word on server 
				String wordTyped = in.next();
				movingWords.setTypedWord(wordTyped);
				System.out.println("typed updated: " + wordTyped);
			}
		}
		
		if (command.equals("GETWORDS")) {
			//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);

			String commandOut = "WORDSDATA ";
			for (int i=0; i<23; i++) {
				commandOut += Words[i].getSpriteY() +" ";
			}
			commandOut += "\n";
			System.out.println("Sending: " + commandOut);
			out.println(commandOut);
			out.flush();
				
			s2.close();
		}
		
		//start the game on the server (NO FUNCTION BRO)
		if (command.equals("STARTGAME")) {
			movingWords.startMovingWords();
		}
		
	}
}