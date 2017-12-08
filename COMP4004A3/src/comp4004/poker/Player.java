package comp4004.poker;

import java.util.List;

import comp4004.poker.Card.*;

public class Player {
	private Hand hand;
	private PointsBoard display;
	private long id;
	private boolean isPlaying = false;
	private boolean turnOver = false;
	private boolean ai = false;
	public boolean hasPlayedToBoard = false;
	
	
	public Player(){
		display = new PointsBoard();
		hand = new Hand();
	}
	
	public Hand getHand(){
		return hand;
	}
	
	public boolean getPlaying(){
		return isPlaying;
	}
	
	public boolean getTurnOver() {
		return turnOver;
	}
	
	public boolean getAI() {
		return ai;
	}
	
	public void removeCard(String s) {
		hand.remove(s);
	}
	
	public void setPlaying(boolean s){
		isPlaying = s;
	}
	
	public void setTurnOver(boolean t) {
		turnOver = t;
	}
	
	public void setAI(boolean t) {
		ai = t;
	}
	
	/**
	 * Draws a card  and adds it to hand
	 * @param c Card
	 */
	public void addCard(Card c){
		hand.add(c);
	}
	
	public int getHandSize(){
		return hand.getNumCards();
	}
	
	public PointsBoard getDisplay(){
		return display;
	}
	
	/**
	 * Returns the thread ID that the player class is associated with
	 * @return long ID
	 */
	public long getID(){
		return id;
	}
	
	public void setid(long i){
		id = i;
	}
}
