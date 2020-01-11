import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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

public class WordRain extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	final static int CLIENT_PORT = 5656;
	final static int SERVER_PORT = 5556;
	
	//declare objects	
	private Head[] Heads;
	private Player[] Players;
	private Word[] Words;
	private int[] WordNumber;
	private MovingWords movingWords;

	//labels
	private JLabel WordTypedLabel, instructionsLabel, lostLabel, wonLabel;
	private JLabel[] WordLabels, PlayerLabels, HeadLabels, scoreLabels, livesLabels;

	//image icons	
	private ImageIcon[] WordImages, PlayerImages, HeadImages;
	
	//buttons, dialogs, & textarea
	private JButton playButton, gotItButton, submitButton;
	private JDialog instructionsDialog, lostDialog, wonDialog;
	private JTextField nameTextField;

	//container
	private Container content;
	
	public WordRain() { 
		//initialize variables
		Words = new Word[23];
		WordLabels = new JLabel[23];
		WordImages = new ImageIcon[23];
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
		HeadLabels = new JLabel[2];
		HeadImages = new ImageIcon[2];
		
		Players = new Player[2];
		PlayerLabels = new JLabel[2];
		PlayerImages = new ImageIcon[2];
		
		scoreLabels = new JLabel[2];
		livesLabels = new JLabel[2];
		WordTypedLabel = new JLabel();
		instructionsLabel = new JLabel();
		lostLabel = new JLabel();
		wonLabel = new JLabel();

		playButton = new JButton();
		gotItButton = new JButton();
		submitButton = new JButton();
		instructionsDialog = new JDialog();
		lostDialog = new JDialog();
		wonDialog = new JDialog();
		nameTextField = new JTextField();

		movingWords =  new MovingWords();
		content = getContentPane();
		
		//initialize the arrays of jLabels
		for (int i=0;i<scoreLabels.length;i++) {
			scoreLabels[i] = new JLabel();
			livesLabels[i] = new JLabel();
		}
		
		//initialize the array of heads
		for (int i=0;i<Heads.length;i++) {
			Heads[i] = new Head();
			HeadLabels[i] = new JLabel();
			HeadImages[i] = new ImageIcon(getClass().getResource(Heads[i].getSpriteName()));
		
			Heads[i].setHeadLabel(HeadLabels[i]);
			Heads[i].setSpriteX(320);
			Heads[i].setSpriteY(420);
			
			HeadLabels[i].setIcon(HeadImages[i]);
			HeadLabels[i].setSize(Heads[i].getSpriteW(), Heads[i].getSpriteH());
			HeadLabels[i].setLocation(Heads[i].getSpriteX(), Heads[i].getSpriteY());
			
			add(HeadLabels[i]);
		}
		
		//initialize the array of players
		for (int i=0;i<Players.length;i++) {
			Players[i] = new Player();
			PlayerLabels[i] = new JLabel();
			PlayerImages[i] = new ImageIcon(getClass().getResource(Players[i].getSpriteName()));
		
			Players[i].setPlayerLabel(PlayerLabels[i]);
			Players[i].setSpriteX(320);
			Players[i].setSpriteY(480);
			
			PlayerLabels[i].setIcon(PlayerImages[i]);
			PlayerLabels[i].setSize(Players[i].getSpriteW(), Players[i].getSpriteH());
			PlayerLabels[i].setLocation(Players[i].getSpriteX(), Players[i].getSpriteY());
			
			add(PlayerLabels[i]);
		}

		
		//initialize the array of words
		for (int i=0;i<Words.length;i++) {
			
			Words[i] = new Word(i);
			WordLabels[i] = new JLabel();
			WordImages[i] = new ImageIcon(getClass().getResource(Words[i].getSpriteName()));

			Words[i].setWordLabel(WordLabels[i]);
			Words[i].setHead(Heads[WordRainProperties.PLAYER_NUM]);
			Words[i].setHeadLabel(HeadLabels[WordRainProperties.PLAYER_NUM]);
			Words[i].setPlayer(Players[WordRainProperties.PLAYER_NUM]);
			Words[i].setPlayerLabel(PlayerLabels[WordRainProperties.PLAYER_NUM]);
			Words[i].setPlayButton(playButton);
			Words[i].setScoreLabel(scoreLabels[WordRainProperties.PLAYER_NUM]);
			Words[i].setLivesLabel(livesLabels[WordRainProperties.PLAYER_NUM]);
			Words[i].setWordTypedLabel(WordTypedLabel);
			Words[i].setMovingWords(movingWords);
			
			//set width for long.png
			if (i==22) {
				Words[i].setLongWord(true); 
				Words[i].setSpriteW(500);
			}

			Words[i].setSpriteY(-200);
			WordLabels[i].setIcon(WordImages[i]);
			WordLabels[i].setSize(Words[i].getSpriteW(), Words[i].getSpriteH());
			WordLabels[i].setLocation(Words[i].getSpriteX(), Words[i].getSpriteY());

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
			WordLabels[i].setVisible(false);
			add(WordLabels[i]);
		}

		//gui
		setSize(WordRainProperties.SCREEN_WIDTH, WordRainProperties.SCREEN_HEIGHT);
		content.setBackground(Color.gray);
		setLayout(null);
		
		//score 
		scoreLabels[WordRainProperties.PLAYER_NUM].setLocation(WordRainProperties.SCREEN_WIDTH-750, WordRainProperties.SCREEN_HEIGHT-620);
		scoreLabels[WordRainProperties.PLAYER_NUM].setSize(70,20);
		scoreLabels[WordRainProperties.PLAYER_NUM].setBackground(Color.white);
		scoreLabels[WordRainProperties.PLAYER_NUM].setOpaque(true);
		scoreLabels[WordRainProperties.PLAYER_NUM].setText("Score:");
		add(scoreLabels[WordRainProperties.PLAYER_NUM]);

		//lives
		livesLabels[WordRainProperties.PLAYER_NUM].setLocation(WordRainProperties.SCREEN_WIDTH-675, WordRainProperties.SCREEN_HEIGHT-620);
		livesLabels[WordRainProperties.PLAYER_NUM].setSize(80,20);
		livesLabels[WordRainProperties.PLAYER_NUM].setIcon(new ImageIcon(getClass().getResource("pictures/lives3.png")));
		add(livesLabels[WordRainProperties.PLAYER_NUM]);

		//WordTyped 
		WordTypedLabel.setLocation(WordRainProperties.SCREEN_WIDTH-750, WordRainProperties.SCREEN_HEIGHT-60);
		WordTypedLabel.setSize(WordRainProperties.SCREEN_WIDTH,WordRainProperties.SCREEN_HEIGHT-600);
		WordTypedLabel.setBackground(Color.white);
		WordTypedLabel.setOpaque(true);
		WordTypedLabel.setText("Typed: ");
		add(WordTypedLabel);

		//buttons
		playButton.setLocation(WordRainProperties.SCREEN_WIDTH-100, WordRainProperties.SCREEN_HEIGHT-620);
		playButton.setSize(85,20);
		playButton.setText("Start");
		playButton.addActionListener(this);
		playButton.setFocusable(false);
		add(playButton);
		
		gotItButton.setSize(70,20);
		gotItButton.setLocation(WordRainProperties.SCREEN_WIDTH-690,WordRainProperties.SCREEN_HEIGHT-540);
		gotItButton.setText("Got It");
		gotItButton.addActionListener(this);
		gotItButton.setFocusable(false);

		submitButton.setSize(80,20);
		submitButton.setLocation(WordRainProperties.SCREEN_WIDTH-700,WordRainProperties.SCREEN_HEIGHT-550);
		submitButton.setText("Submit");
		submitButton.addActionListener(this);
		submitButton.setFocusable(false);

		//instructions 
		instructionsLabel.setSize(10,10);
		instructionsLabel.setText("<html><body>Type in the word on the head </br>of the player before that word hits the bottom.<br/> Avoid falling words.</body></html>");
		instructionsLabel.setVerticalAlignment(instructionsLabel.NORTH);
		
		instructionsDialog.setTitle("Instructions");
		instructionsDialog.setModal(true);
		instructionsDialog.setBounds(200,200,200,150);
		instructionsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		instructionsDialog.add(gotItButton);
		instructionsDialog.add(instructionsLabel);
		instructionsDialog.setVisible(true);
		
		//lost 
		lostLabel.setSize(20,20);
		lostLabel.setText("<html><body>You Lost! Try Again! Score: </body></html>");

		lostDialog.setTitle("You Lost!");
		lostDialog.setModal(true);
		lostDialog.setBounds(200,200,100,100);
		lostDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		lostDialog.add(lostLabel);
		lostDialog.setVisible(false);

		//won
		nameTextField.setBounds(40,40,100,30);
		wonLabel.setSize(20,20);
		wonLabel.setText("<html><body>You Won! Score: <br/>Enter your name: </body></html>");
		wonLabel.setVerticalAlignment(wonLabel.NORTH);
		wonLabel.setHorizontalAlignment(wonLabel.CENTER);
		
		wonDialog.setTitle("You Won!");
		wonDialog.setModal(true);
		wonDialog.setBounds(200,200,200,150);
		wonDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		wonDialog.add(nameTextField);
		wonDialog.add(submitButton);
		wonDialog.add(wonLabel);
		wonDialog.setVisible(false);

		//movingWords
		movingWords.setWords(Words);
		movingWords.setWordLabels(WordLabels);
		movingWords.setPlayer(Players);
		movingWords.setHead(Heads);
		movingWords.setHeadLabel(HeadLabels);
		movingWords.setWordNumber(WordNumber);
		movingWords.setLostDialog(lostDialog);
		movingWords.setLostLabel(lostLabel);
		movingWords.setWonDialog(wonDialog);
		movingWords.setWonLabel(wonLabel);	

		content.addKeyListener(this);
		content.setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Socket s;
		try {
			s = new Socket("localhost", SERVER_PORT);
			System.out.println("get head thread");
			
			//Initialize data stream to send data out
			OutputStream outstream = s.getOutputStream();
			PrintWriter out = new PrintWriter(outstream); 
			
			String command = "GETWORDS\n";
			out.println(command);
			out.flush();
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//start retrieval thread(s)
		GetPlayerThread getPlayerThread = new GetPlayerThread();
		Thread t1 = new Thread(getPlayerThread);
		t1.start();
		
		GetHeadThread getHeadThread = new GetHeadThread();
		Thread t2 = new Thread(getHeadThread);
		t2.start();
		
//		GetWordsThread getWordsThread = new GetWordsThread();
//		Thread t3 = new Thread(getWordsThread);
//		t3.start();
		
		GetMovingWordsThread getMovingWordsThread = new GetMovingWordsThread();
		Thread t4 = new Thread(getMovingWordsThread);
		t4.start();
		
		//start listening server
		Server myServer = new Server(Players, Heads, Words, movingWords, HeadLabels, PlayerLabels, WordLabels, scoreLabels, livesLabels, playButton);
		Thread t5 = new Thread(myServer);
		t5.start();
	}
	
	public static void main(String [] args) {
		WordRain wordRain = new WordRain();
		wordRain.setVisible(true);
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
	
	public void actionPerformed(ActionEvent e) {
		//play button
		if (e.getSource() == playButton) {
			
			try {
				//set up a communication socket
				Socket s = new Socket("localhost", SERVER_PORT);
				
				//Initialize data stream to send data out
				OutputStream outstream = s.getOutputStream();
				PrintWriter out = new PrintWriter(outstream);
				
				String command = "STARTGAME \n";
				System.out.println("Sending: " + command);
				out.println(command);
				out.flush();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			movingWords.startMovingWords();
			playButton.setVisible(false);
		} else if (e.getSource() == gotItButton) {
			//instructions gotItButton
			instructionsDialog.dispose();
		} else if (e.getSource() == submitButton) {
			//submit name & score button
			Connection conn = null;
			Statement stmt = null;
			
			try {
				//load the DB driver
				Class.forName("org.sqlite.JDBC");
				String dbURL = "jdbc:sqlite:product.db";
				conn = DriverManager.getConnection(dbURL);
				if (conn != null) {
					System.out.println("Connection established");
					
					conn.setAutoCommit(false);
					DatabaseMetaData dm = (DatabaseMetaData)conn.getMetaData();
					System.out.println("Driver Name: " + dm.getDriverName());
					System.out.println("Driver version: " + dm.getDriverVersion());
					System.out.println("Product Name: " + dm.getDatabaseProductName());
					System.out.println("Product verison: " + dm.getDatabaseProductVersion());
					
					stmt = conn.createStatement();
					String sql = "";
					ResultSet rs = null;
					
					//create table
					sql = "CREATE TABLE IF NOT EXISTS players ("+ 
						  "id INTEGER PRIMARY KEY, " +
						  "name TEXT NOT NULL, " + 
						  "score INT NOT NULL " + ")";
					stmt.executeUpdate(sql);
					conn.commit();
					
					//insert name and score
					String name = nameTextField.getText();
					int finalScore = Players[WordRainProperties.PLAYER_NUM].getScore();
					sql = "INSERT INTO players (name, score) VALUES ('" +
					       name + "', " + finalScore +  ")";
					stmt.executeUpdate(sql);
					conn.commit();
	
					//retrieve id, name, & score
					sql = "SELECT * FROM players";
					rs = stmt.executeQuery(sql);
					System.out.println("Past player scores:  ");
					DisplayRecords(rs);
					rs.close();
									
					conn.close();
				} else {
					System.out.println("Cannot establish connection");
				}
							
			} catch (ClassNotFoundException err) {
				err.printStackTrace();
			} catch (SQLException err) {
				err.printStackTrace();
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				//cleanup
			}
			System.exit(ABORT);
		}

	}
	
	//move the player & head left or right if the game has started
	public void keyPressed(KeyEvent e) {
		if (!playButton.isVisible()) {
			Heads[WordRainProperties.PLAYER_NUM].moveHead(e);
			Players[WordRainProperties.PLAYER_NUM].movePlayer(e);
		}
		movingWords.typingWords(e);
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
