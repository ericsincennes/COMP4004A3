package comp4004.poker;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import comp4004.poker.Card.*;

public class ClientGUI extends Client{

	private JFrame frmMain;

	private JPanel informationPanel;
	private JPanel displaysPanel;
	private JPanel handPanel;
	private JPanel actionArea;
	private JPanel playerDisplayPanel;
	private JPanel[] opponentPanel;
//	private JPanel[] opponentActionPanel;
//	private JPanel actionCardPanel;
//	private JPanel playerActionPanel;
	
	private JLabel informationLabel = new JLabel();
/*	private JLabel tournamentColourLabel = new JLabel();
	private JLabel highestScore = new JLabel();
	private JLabel playerScore = new JLabel();
	private JLabel tokens = new JLabel();*/
	
	private JScrollPane handPane;
	private JScrollPane displayPane;
//	private JScrollPane actionCardPane;
	
	private Card selectedCard = null;
	
	private static final String ImageDirectory = (System.getProperty("user.dir") + "/Images/");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ClientGUI window = null;
				try {
					window = new ClientGUI();
					window.initialize();
					window.connect();
					window.frmMain.setVisible(true);
					
					Thread t = new Thread(window.new Updater());
					t.start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		opponentPanel = new JPanel[3];
		//opponentActionPanel = new JPanel[4];
		//initialize();
	}

	protected boolean connect(){
		InetAddress adr = null;
		String add = null;
		int po = 0;
		
		while(true){
			add = (String) JOptionPane.showInputDialog(frmMain.getContentPane(), "Enter a IP address to connect to","Connect", JOptionPane.QUESTION_MESSAGE );
			
			if( add == null || add.length() == 0){
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "invalid IP", "IP Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			
			try {
				adr = InetAddress.getByName(add);
				break;
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "invalid IP", "IP Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		while(true){
			String p = (String) JOptionPane.showInputDialog(frmMain.getContentPane(), "Enter a port to connect to","Connect", JOptionPane.QUESTION_MESSAGE );
			
			try{
				po = Integer.parseInt(p);
				break;
			} catch(NumberFormatException err){
				JOptionPane.showMessageDialog(frmMain.getContentPane(), "Invalid port", "Port Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return super.connect(add, po);
	}
	
	/**
	 * Everything relating to the action area JPanel goes in here
	 */
	private void initializeActionArea(){
		actionArea = new JPanel();
		actionArea.setBackground(Color.gray);
		actionArea.setLayout(new GridLayout(6,1));

		JButton endTurnButton = new JButton("End Turn");
		//JButton exchangeButton = new JButton("Exchange");
		JButton playCardButton = new JButton("Exchange Card");
/*
		highestScore.setText("High Score: ");
		highestScore.setVerticalAlignment(JLabel.CENTER);

		playerScore.setText("Your Score: ");
		playerScore.setVerticalAlignment(JLabel.CENTER);*/
		
		playCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {			
				if(isActiveTurn){
					if(selectedCard != null){
						sendCardsToBePlayed();
					} else {
						//no card selected
						JOptionPane.showMessageDialog(actionArea, "Select a card to exchange", "Cannot exchange nothing", JOptionPane.ERROR_MESSAGE);
						}
				} else {
					//not players turn
					JOptionPane.showMessageDialog(actionArea, "Cannot exchange card when it is not your turn", "Exchange card error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		//End turn button pressed
		endTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isActiveTurn){
					//if player's turn
					send(Optcodes.ClientEndTurn);
				} else {
					//if not players turn
					JOptionPane.showMessageDialog(actionArea, "Cannot end turn when it is not your turn", "End Turn Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
/*
		//exchange Button Pressed
		exchangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isActiveTurn){
					//is players turn
					send(Optcodes.ClientExchange);
				} else {
					//if not player's turn
					JOptionPane.showMessageDialog(actionArea, "Cannot exchange when it is not your turn", "Exchange Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});*/

		//actionArea.add(highestScore);
		//actionArea.add(playerScore);
		actionArea.add(endTurnButton);
		//actionArea.add(exchangeButton);
		actionArea.add(playCardButton);
	}

	/**
	 * Everything relating to the hand area JPanel goes in here
	 */
	private void initializeHandPanel(){
		handPanel = new JPanel();
		handPanel.setBackground(Color.gray);
		handPanel.setLayout(new FlowLayout());
		
		handPane = new JScrollPane(handPanel);
		handPane.setVerticalScrollBarPolicy(ScrollPaneLayout.VERTICAL_SCROLLBAR_NEVER);
		handPane.setPreferredSize(new Dimension(0, 220));
	}

	private void initializeDisplayPanel(){
		int numplayers = theBoard.players.size();
		
		//get player id's
		Long[] playerid = new Long[numplayers];
		playerid = theBoard.players.toArray(playerid);
		
		displaysPanel = new JPanel();
		displaysPanel.setBackground(Color.gray);
		displaysPanel.setLayout(new GridLayout(0, 1));

		displayPane = new JScrollPane(displaysPanel);

		playerDisplayPanel = new JPanel();
		playerDisplayPanel.setLayout(new FlowLayout());
		playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "You " + playerid[0]));

		
		
		for(int i=0; i<numplayers-1; i++){
			opponentPanel[i] = new JPanel();
			opponentPanel[i].setLayout(new FlowLayout());
			opponentPanel[i].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + playerid[i+1]));
			opponentPanel[i].setName("Opponent " + playerid[i+1]);
			displaysPanel.add(opponentPanel[i]);
		}
		
		displaysPanel.add(playerDisplayPanel);
	}
/*	
	private void initializeActionCardPanel(){
		int numplayers = theBoard.players.size();
		
		//get player id's
		Long[] playerid = new Long[numplayers];
		playerid = theBoard.players.toArray(playerid);
				
		actionCardPanel = new JPanel();
		actionCardPanel.setBackground(Color.gray);
		actionCardPanel.setLayout(new GridLayout(0, 1));
		
		actionCardPane = new JScrollPane(actionCardPanel);
		actionCardPane.setPreferredSize(new Dimension(205, 145));
		
		playerActionPanel = new JPanel();
		playerActionPanel.setLayout(new FlowLayout());
		playerActionPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Action Cards"));
		
		for(int i=0; i<numplayers-1; i++){
			opponentActionPanel[i] = new JPanel();
			opponentActionPanel[i].setLayout(new FlowLayout());
			opponentActionPanel[i].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent " + playerid[i+1] +"'s Action Cards"));
			opponentActionPanel[i].setName("Opponent " + playerid[i+1]);
			actionCardPanel.add(opponentActionPanel[i]);
		}
		actionCardPanel.add(playerActionPanel);
	}
	*/
	private void initializeInformationPanel(){
		informationPanel = new JPanel();
		informationPanel.setBackground(Color.orange);
		informationPanel.setLayout(new GridLayout(3, 1));

		informationLabel.setText("no card selected");
		informationLabel.setHorizontalAlignment(JLabel.CENTER);

		informationPanel.add(informationLabel);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMain = new JFrame();
		frmMain.setTitle("Poker");
		frmMain.setBounds(100, 100, 1300, 810);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Initialize all GUI Pieces
		initializeActionArea();
		initializeHandPanel();
		initializeInformationPanel();
		//initializeDisplayPanel();

		frmMain.getContentPane().add(informationPanel, BorderLayout.NORTH);
		//frmMain.getContentPane().add(displayPane, BorderLayout.CENTER);
		frmMain.getContentPane().add(handPane, BorderLayout.SOUTH);
		frmMain.getContentPane().add(actionArea, BorderLayout.WEST);

	}

	//Update functions

	public void updateHand(){
		//clear panel
		handPanel.removeAll();
		
		//add all cards from hand
		for(Card x: theBoard.hand){
			BufferedImage ba = null;
			try{
				ba = ImageIO.read(new File(ImageDirectory + x.getCardName()+ ".png"));
			} catch (IOException e){
				e.printStackTrace();
			}

			JButton button = new JButton(new ImageIcon(ba));
			button.setName(x.getCardName());
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setToolTipText(x.getCardName());

			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for(Card x: theBoard.hand){
						if(x.getCardName() == button.getName()){
							selectedCard = x;
							updateInformationLabel("Selected card: " + x.getCardName());
							break;
						}
					}
				}
			});
			
			handPanel.add(button);

		}
		//repaint
		handPanel.revalidate();
		//handPanel.repaint();
		
		handPane.validate();
		handPane.repaint();
	}

	public void updateDisplayPanel(){
		for(List<Card> displays : theBoard.revealed){
			if(theBoard.revealed.indexOf(displays) == 0){
				playerDisplayPanel.removeAll();
				//playerDisplayPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Your Board"));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".png"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLabel = new JLabel(new ImageIcon(img));
					imgLabel.setToolTipText(x.getCardName());
					playerDisplayPanel.add(imgLabel);
				}
				playerDisplayPanel.revalidate();
				playerDisplayPanel.repaint();
				displaysPanel.revalidate();
				displaysPanel.repaint();;

			} else {
				//opponent displays
				opponentPanel[theBoard.revealed.indexOf(displays) -1].removeAll();
				//opponentPanel[theBoard.boards.indexOf(displays) -1].setBorder(new TitledBorder(new LineBorder(Color.black), "Opponent ID: " + theBoard.players.get(theBoard.boards.indexOf(displays)) ));
				for(Card x: displays){
					BufferedImage img = null;
					try{
						img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".png"));
					} catch (IOException e){
						e.printStackTrace();
					}
					JLabel imgLabel = new JLabel(new ImageIcon(img));
					imgLabel.setToolTipText(x.getCardName());
					opponentPanel[theBoard.revealed.indexOf(displays) -1].add(imgLabel);

				}
				opponentPanel[theBoard.revealed.indexOf(displays) -1].revalidate();
				displaysPanel.repaint();
				displaysPanel.revalidate();
			}
		}
		displayPane.repaint();
		displayPane.revalidate();
	}
	/*
	public void handleClientExchange(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "You have exchange from the game", "GG", JOptionPane.INFORMATION_MESSAGE);
	}*/
	
	public void handleInvalidCard(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "Card Cannot be played", "Card Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void handleSuccessfulCardPlay(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "Card Exchanged", "Card Exchanged", JOptionPane.INFORMATION_MESSAGE);
	}

	public void updateInformationLabel(String text){
		informationLabel.removeAll();
		informationLabel.setText(text);
		informationLabel.revalidate();
	}
	
	@Override
	protected void sendCardsToBePlayed(){
		send(theBoard.hand.indexOf(selectedCard));
	}
	
	
	public void handleLoseTournament() {
		String winner = (String) get();
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "The winner was Player: " + winner, "Tournament Winner", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void handleLoseGame() {
		String winner = (String) get();
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "The winner was Player: " + winner + "\nYou have Lost. \n", "Game Winner", JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public void handleActiveTurn(){
		if(!isActiveTurn){
			JOptionPane.showMessageDialog(frmMain.getContentPane(), "It is your turn", "Your Turn", JOptionPane.INFORMATION_MESSAGE);
			isActiveTurn = true;
		}
	}
	
	@Override
	protected void handleUpdateBoardState(){
		boolean firstdraw = false;
		if(theBoard == null){
			firstdraw = true;
		}
		super.handleUpdateBoardState();
		
		if(firstdraw){
			initializeDisplayPanel();
			//initializeActionCardPanel();
			
			frmMain.getContentPane().add(displayPane, BorderLayout.CENTER);
			frmMain.getContentPane().revalidate();
			
			//frmMain.getContentPane().add( actionCardPane, BorderLayout.EAST);
			frmMain.getContentPane().revalidate();
		}
		
		updateDisplayPanel();
		updateHand();
		
		frmMain.revalidate();
		frmMain.repaint();
	}
	private void handleClientFailStartTournament(){
		String playerID = (String) get();
		List<Card> hand = (List<Card>) get();
		
		String msg = "Player "+ playerID + " cannot start a tournament";
		JPanel hp = new JPanel();
		hp.setLayout(new FlowLayout());
		
		for(Card x : hand){
			BufferedImage img = null;
			try{
				img = ImageIO.read(new File(ImageDirectory + x.getCardName() + ".png"));
			} catch (IOException e){
				e.printStackTrace();
			}
			JLabel imgLabel = new JLabel(new ImageIcon(img));
			hp.add(imgLabel);
		}
		hp.revalidate();
		
		JOptionPane.showMessageDialog(frmMain.getContentPane(), hp, msg, JOptionPane.PLAIN_MESSAGE);
	}
	/*
	public void handleOppActionCardPlayed() {
		String message = (String) get();
		
		//display message of what action card has been played
		JOptionPane.showMessageDialog(frmMain.getContentPane(), message, "Action card played", JOptionPane.PLAIN_MESSAGE);
	}*/
	
	private void handleGameWinner(){
		JOptionPane.showMessageDialog(frmMain.getContentPane(), "You have won the game!" , "Game Winner", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * msg to notify client when others' turn are over
	 * @param mode 1 if they ended, 2 if they withdrew
	 */
	private void handleOppTurnOver(int mode) {
		Long pid = (Long) get();
		JOptionPane.showMessageDialog(frmMain.getContentPane(), 
				"Player " + pid + " has " + ((mode%2 == 0) ? "Exchanged." : "ended their turn."),
				"Turn Changed", JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	protected void mainLoop(){
		playerNum = (int) get();	//get player number from server

		while(true){
			Object o = get();
			print("getting optcode from server " + o.getClass().getName() + " " + o.toString());
			int optcode = (int) o;

			switch(optcode) {
			case Optcodes.ClientUpdateBoardState:
				handleUpdateBoardState();
				break;
			case Optcodes.ClientGetCardsToBePlayed:
				//sendCardsToBePlayed();
				break;
			case Optcodes.InvalidCard:
				handleInvalidCard();
				break;
			case Optcodes.SuccessfulCardPlay:
				handleSuccessfulCardPlay();
				break;
			case Optcodes.ClientExchange:
				//handleClientExchange();
				break;
			case Optcodes.ClientActiveTurn:
				handleActiveTurn();
				break;
			case Optcodes.ClientNotActiveTurn:
				isActiveTurn = false;
				break;
			case Optcodes.LoseTournament:
				handleLoseTournament();
				break;
			case Optcodes.GameOver:
				handleLoseGame();
				break;
			case Optcodes.ClientFailStartTournament:
				handleClientFailStartTournament();
				break;
			case Optcodes.ClientActionCardPlayed:
				//handleOppActionCardPlayed();
				break;
			case Optcodes.GameWinner:
				handleGameWinner();
				break;
			case Optcodes.OppEndTurn:
				handleOppTurnOver(1);
				break;
			case Optcodes.OppWithdraw:
				handleOppTurnOver(2);
				break;
			default: 
				new Exception("Unexpected Value");
				break;
			}
		}
	}
	
	private class Updater implements Runnable{

		@Override
		public void run() {
			ClientGUI.this.mainLoop();
		}
	}
}