package comp4004.poker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import comp4004.poker.Card.*;
import comp4004.poker.Optcodes;

/**
 * Representation of a player in the game Ivanhoe.
 */
public class Client {
	protected Socket socket;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	protected int playerNum = -1;
	protected BoardState theBoard;
	protected boolean isActiveTurn;
	protected boolean hasExchanged = false;
	protected Scanner scan = new Scanner(System.in);
	protected String colour;

	public static void main(String[] args){
		Client p = new Client();
		p.connect("::",2244);
		p.mainLoop();
		
	}

	/**
	 * returns true if it is currently the players turn
	 * @return boolean
	 */
	public boolean isActiveTurn() {
		return isActiveTurn;
	}

	public Client(){}

	/**
	 * Connects the client to a specified ip address and port 
	 * @param IPaddr string from which an ip is parsed
	 * @param port integer port
	 * @return true if connect was successful
	 */
	public boolean connect(String IPaddr, int port){
		InetAddress host;
		

		try {
			host = InetAddress.getByName("localhost");
			host = InetAddress.getByName(IPaddr);
			socket = new Socket(host, port);
			print("Connection to " + host + " on port "+ port);

			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * The main loop of the client function that calls all other functions as needed.
	 * Waits for an optcode from the server and calls a specified function to handle the optcode
	 */
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
				print("Choose a card to play, type 99 to withdraw, or type 66 to end turn.");
				sendCardsToBePlayed();
				break;
			case Optcodes.InvalidCard:
				print("Card is unable to be played");
				break;
			case Optcodes.SuccessfulCardPlay:
				print("Card was played successfully");
				break;
			case Optcodes.ClientExchange:
				hasExchanged = true;
				break;
			case Optcodes.ClientActiveTurn:
				handleActiveTurn();
				isActiveTurn = true;
				break;
			case Optcodes.ClientNotActiveTurn:
				handleNonActiveTurn();
				break;
			default: new Exception("Unexpected Value");
				break;
			}
		}
	}
	

	/**
	 * The handler function for optcode NonActiveTurn
	 */
	protected void handleNonActiveTurn() {
		isActiveTurn = false;
	}
	
	/**
	 * The handler function for optcode ActiveTurn
	 * Currently overridden by clientGUI
	 */
	protected void handleActiveTurn() {

		isActiveTurn = true;
		print("It is now your turn.");

	}

	/**
	 * Handler for optcode ClientGetCardsToBePlayed.
	 * Gets input of what cards are goint to be sent to the server to be played.
	 * If card is an action card then gets input of the card's targets.
	 */
	protected void sendCardsToBePlayed(){
		int choice = -1;
		
		do {
			choice = scan.nextInt();
			if (choice != 66 && choice != 99 && (choice < 1 || choice > theBoard.hand.size())) {
				print("Choose a number corresponding to a card in your hand");
			}
			else break;
		} while (true);
		
		if (choice == 99) {
			send(Optcodes.ClientExchange);
		} else if (choice == 66) {
			send(Optcodes.ClientEndTurn);
		} else {
			send(choice-1);
		}

	}
	
	/**
	 * Handler for optcode ClientUpdateBoardState.
	 * Gets the state of the board from the server and updates.
	 */
	protected void handleUpdateBoardState(){
		Object o = get();
		print("handleUpdateBoardState() getting " + o.getClass().getName() + " " + o.toString());
		BoardState btmp = (BoardState) o;
		
		if (theBoard != null && theBoard.equals(btmp)) {
			
			return;
		}
		else {
			theBoard = btmp;
						
			print("The board state: \n");
			for (int i=theBoard.players.size()-1; i>=0; i--) {
				print("Board of player ID " +theBoard.players.get(i));
				List<Card> l = theBoard.revealed.get(i);
	
				for(Card c: l){
					System.out.print(c.getCardName() + " - ");
					System.out.println("");
				}
	
				
			}
			System.out.println("");
			
			print("Your hand:");
			for(int i =0; i<theBoard.hand.size(); i++){
				System.out.print(i+1 + ". " + theBoard.hand.get(i).getCardName() + "  ");
				
			}
			System.out.println("");
		}
	}		


	/**
	 * Gets an object from the client
	 * Does not verify the typeOf an object
	 * @return
	 */
	protected Object get(){
		Object o = new Object();
		try {
			o = in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * Sends an object to the client
	 * @param o Object to be sent
	 * @return boolean if successful
	 */
	protected boolean send(Object o){
		try {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Convenience function to avoid typing system.out.println
	 * @param s String to be printed
	 */
	protected void print(String s){
		System.out.println(s);
	}
	
	/**
	 * Convenience function to avoid typing system.out.print
	 * @param s String to be printed
	 */
	protected void printlist(String s){
		System.out.print(s);
	}
	
	/**
	 * Gets the player number of the client
	 * @return integer
	 */
	protected int getPlayerNum(){
		return playerNum;
	}
}