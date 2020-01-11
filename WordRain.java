import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Random;

public class WordRain {
	private static final long serialVersionUID = 1L;
	
	//declare objects	
	private Head[] Heads;
	private Player[] Players;
	private Word[] Words;
	private int[] WordNumber;
	private MovingWords movingWords;
	
	public WordRain() { 
		//initialize variables
		Words = new Word[23];
		WordNumber = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22};
		//randomize WordNumber array
		Random random = new Random();
		for (int i = 0; i < WordNumber.length; i++) {
			int randomIndexToSwap = random.nextInt(WordNumber.length);
			int temp = WordNumber[randomIndexToSwap];
			WordNumber[randomIndexToSwap] = WordNumber[i];
			WordNumber[i] = temp;
		}

		Heads = new Head[2];
		Players = new Player[2];
		
		movingWords =  new MovingWords();
		
		//head
		for (int i=0;i<Heads.length;i++) {
			Heads[i] = new Head();
			Heads[i].setSpriteX(320);
			Heads[i].setSpriteY(420);
		}
		
		//player
		for (int i=0;i<Players.length;i++) {
			Players[i] = new Player();
			Players[i].setSpriteX(320);
			Players[i].setSpriteY(480);
		}
		
		//initialize the array of words
		for (int i=0;i<Words.length;i++) {
			Words[i] = new Word(i);
			Words[i].setHead(Heads[WordRainProperties.PLAYER_NUM]);
			Words[i].setPlayer(Players[WordRainProperties.PLAYER_NUM]);
			Words[i].setMovingWords(movingWords);
			
			//set width for long.png
			if (i==22) {
				Words[i].setLongWord(true); 
				Words[i].setSpriteW(500);
			}

			Words[i].setSpriteY(-200);

			//set the points for each word (based on the number of letters in the word)
			switch (i) {
				case 0:case 1:case 2:case 3:case 4:case 5:
				case 6: Words[i].setWordPoints(20); break;
				case 7:case 8:case 9:case 10:case 11:
				case 12: Words[i].setWordPoints(50); break;
				case 13:case 14:case 16:
				case 17: Words[i].setWordPoints(60); break;
				case 18:case 19:case 20:
				case 21: Words[i].setWordPoints(70); break;
				case 22: Words[i].setWordPoints(100); break;
				default: Words[i].setWordPoints(0); break;
			}

			Words[i].setMoving(false);
		}
		
		//movingWords
		movingWords.setWords(Words);
		movingWords.setPlayer(Players);
		movingWords.setHead(Heads);
		movingWords.setWordNumber(WordNumber);

	}
	
	//need a function to start the listening server
	
	public static void main(String [] args) throws IOException {
		WordRain wordRain = new WordRain();
		//call the function to start the listening server
		wordRain.startServer();
	}

	//displays records in database
	public static void DisplayRecords(ResultSet rs) throws SQLException {
		while ( rs.next() ) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int score = rs.getInt("score");
			
			System.out.println("id: " + id);
			System.out.println("name: " + name);
			System.out.println("score: " + score);
		}
	}
	
	public void startServer() {
		Server theServer = new Server(Players, Heads, Words, movingWords);
		Thread t = new Thread(theServer);
		t.start();
	}
	
//	public void actionPerformed(ActionEvent e) {
//		//play button
//		if (e.getSource() == playButton) {
//			movingWords.startMovingWords();
//			playButton.setVisible(false);
//		} else if (e.getSource() == gotItButton) {
//			//instructions gotItButton
//			instructionsDialog.dispose();
//		} else if (e.getSource() == submitButton) {
//			//submit name & score button
//			Connection conn = null;
//			Statement stmt = null;
//			
//			try {
//				//load the DB driver
//				Class.forName("org.sqlite.JDBC");
//				String dbURL = "jdbc:sqlite:product.db";
//				conn = DriverManager.getConnection(dbURL);
//				if (conn != null) {
//					System.out.println("Connection established");
//					
//					conn.setAutoCommit(false);
//					DatabaseMetaData dm = (DatabaseMetaData)conn.getMetaData();
//					System.out.println("Driver Name: " + dm.getDriverName());
//					System.out.println("Driver version: " + dm.getDriverVersion());
//					System.out.println("Product Name: " + dm.getDatabaseProductName());
//					System.out.println("Product verison: " + dm.getDatabaseProductVersion());
//					
//					stmt = conn.createStatement();
//					String sql = "";
//					ResultSet rs = null;
//					
//					//create table
//					sql = "CREATE TABLE IF NOT EXISTS players ("+ 
//						  "id INTEGER PRIMARY KEY, " +
//						  "name TEXT NOT NULL, " + 
//						  "score INT NOT NULL " + ")";
//					stmt.executeUpdate(sql);
//					conn.commit();
//					
//					//insert name and score
//					String name = nameTextField.getText();
//					int finalScore = movingWords.getScore();
//					sql = "INSERT INTO players (name, score) VALUES ('" +
//					       name + "', " + finalScore +  ")";
//					stmt.executeUpdate(sql);
//					conn.commit();
//	
//					//retrieve id, name, & score
//					sql = "SELECT * FROM players";
//					rs = stmt.executeQuery(sql);
//					System.out.println("Past player scores:  ");
//					DisplayRecords(rs);
//					rs.close();
//									
//					conn.close();
//				} else {
//					System.out.println("Cannot establish connection");
//				}
//							
//			} catch (ClassNotFoundException err) {
//				err.printStackTrace();
//			} catch (SQLException err) {
//				err.printStackTrace();
//			} catch (Exception err) {
//				err.printStackTrace();
//			} finally {
//				//cleanup
//			}
//			System.exit(ABORT);
//		}
//
//	}

}
