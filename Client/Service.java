import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

//processing routine on server (B)
public class Service implements Runnable {

	private Socket s;
	private Scanner in;

	private Player[] Players;
	private Head[] Heads;
	private Word[] Words;
	private MovingWords movingWords;
	
	private JLabel[] WordLabels, PlayerLabels, HeadLabels, scoreLabels, livesLabels;
	private JButton playButton;
	
	public Service (Socket aSocket, Player[] p, Head[] h, Word[] w, MovingWords m, JLabel[] hL, JLabel[] pL, JLabel[] wL, JLabel[] sL, JLabel[] lL, JButton pB) {
		this.s = aSocket;
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
		if (command.equals("PLAYERDATA")) {
			/*
			int playerNo = in.nextInt();
			int playerX = in.nextInt();
			int playerY = Players[WordRainProperties.PLAYER_NUM].getSpriteY();
		
			for (int i=0;i<2;i++) {
				Players[i].setSpriteX(playerX);
				PlayerLabels[i].setLocation(playerX, playerY);
			}
			*/
			for (int i=0;i<2;i++) {
				int playerX = in.nextInt();
				int playerY = Players[i].getSpriteY();
				int playerScore = in.nextInt();
				int playerLives = in.nextInt();
				Players[i].setSpriteX(playerX);
				PlayerLabels[i].setLocation(playerX, playerY);
				Players[i].setScore(playerScore);
				Players[i].setLives(playerLives);
			}
			
			System.out.println("Player data recieved");
		}
		
		if (command.equals("HEADDATA")) {
		
			for (int i=0;i<2;i++) {
				int headX = in.nextInt();
				String headName = in.next();
				int headY = Heads[i].getSpriteY();
				
				Heads[i].setSpriteX(headX);
				HeadLabels[i].setLocation(headX, headY);
				Heads[i].setSpriteName(headName);
				HeadLabels[i].setIcon(new ImageIcon(getClass().getResource(Heads[i].getSpriteName())));
			}
			
			System.out.println("Head data recieved");
		}
		
		if (command.equals("MOVINGWORDSDATA")) {
			//set all the changing variables for the words
			for (int i=0;i<23;i++) {
				int wordX = in.nextInt();
				int wordY = in.nextInt();
				boolean wordAct = in.nextBoolean();
				boolean wordMov = in.nextBoolean();
				boolean wordHit = in.nextBoolean();
				boolean wordCom = in.nextBoolean();
				boolean wordNCom = in.nextBoolean();
				boolean wordVis = in.nextBoolean();
				
				Words[i].setSpriteX(wordX);
				Words[i].setSpriteY(wordY);
				WordLabels[i].setLocation(wordX, wordY);
				WordLabels[i].setVisible(wordVis);
				
				Words[i].setActive(wordAct);
				Words[i].setMoving(wordMov);
				Words[i].setWordHit(wordHit);
				Words[i].setWordComplete(wordCom);
				Words[i].setWordNotComplete(wordNCom);
				Words[i].setVisible(wordVis);
				WordLabels[i].setVisible(wordVis);
				
				String wordCur = in.next();
				movingWords.setCurrentWord(wordCur);
				
				boolean rightD = in.nextBoolean();
				boolean leftD = in.nextBoolean();
				boolean middleD = in.nextBoolean();
				movingWords.setRightD(rightD);
				movingWords.setLeftD(leftD);
				movingWords.setMiddleD(middleD);
				
				boolean rightWord = in.nextBoolean();
				boolean leftWord = in.nextBoolean();
				boolean middleWord = in.nextBoolean();
				movingWords.setRightWord(rightWord);
				movingWords.setLeftWord(leftWord);
				movingWords.setMiddleWord(middleWord);
				
				int leftDis = in.nextInt();
				int middleDis = in.nextInt();
				int rightDis = in.nextInt();
				movingWords.setLeftDistance(leftDis);
				movingWords.setMiddleDistance(middleDis);
				movingWords.setRightDistance(rightDis);
				
				//set WordTyped label
				String wordTyp = movingWords.getTypedWord();
				Words[i].getWordTypedLabel().setText("Typed: " +wordTyp);
				
				if ((wordCur.equalsIgnoreCase(wordTyp))) {
					movingWords.setTypedWord("");
				}
				
			}
			
			for (int j=0;j<2;j++) {
				//set score label
				int playerScor = in.nextInt();
				int playerLive = in.nextInt();
				//just for one player...
				if (j == WordRainProperties.PLAYER_NUM) {
					Words[j].getScoreLabel().setText("Score:" +playerScor);
					if (playerLive == 3) {
                        Words[j].getLivesLabel().setIcon(new ImageIcon(getClass().getResource("pictures/lives3.png")));
                    } else if (playerLive == 2) {
                        Words[j].getLivesLabel().setIcon(new ImageIcon(getClass().getResource("pictures/lives2.png")));
                    } else if (playerLive == 1) {
                        Words[j].getLivesLabel().setIcon(new ImageIcon(getClass().getResource("pictures/lives1.png")));
                    } else if (playerLive == 0) {
                        Words[j].getLivesLabel().setIcon(new ImageIcon(getClass().getResource("pictures/lives0.png")));					
                    } else if (playerLive < 0) {
                    	//if all lives are used up stop game
                        Words[j].stopWords();
                        movingWords.stopMovingWords();
                    }
				} else {
					System.out.println("update other score" +playerScor);
				}
				
				HeadLabels[j].setIcon(new ImageIcon(getClass().getResource(Heads[j].getSpriteName())));
			}
			
			System.out.println("moving words data recieved");
		}
		
		if (command.equals("WORDSDATA")) {
		}
		
	}
	
}
