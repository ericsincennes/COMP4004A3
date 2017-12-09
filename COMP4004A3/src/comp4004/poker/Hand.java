package comp4004.poker;

import java.util.List;

import comp4004.poker.Card.CardSuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Hand {
	
	private List<Card> hand;
	
	public enum Strength {
		Royal(0), StrFlush(1), FourOK(2), FullHouse(3), Flush(4), Straight(5), ThreeOK(6), TwoPair(7), OnePair(8), HighCard(9);
		
		private final int rank;
		private Strength(int val) {
			this.rank = val;
		}
		
		public int getStrengthRank() {
			return rank;
		}
	}
	
	protected Strength strength = Strength.HighCard;
	protected Card tieBreaker;
	protected Card pTieBreaker;
	protected Card high;
	
	public Strength getStrength() {
		return strength;
	}
	
	public Card getTieBreaker() {
		return tieBreaker;
	}
	
	public Card getPTieBreaker() {
		return pTieBreaker;
	}
	
	public Card getHigh() {
		return high;
	}
	
	public void setStrength(Strength str) {
		strength = str;
	}
	
	public void setTieBreaker(Card tb) {
		tieBreaker = tb;
	}
	
	public void setPTieBreaker(Card ptb) {
		pTieBreaker = ptb;
	}
	
	public void setHigh(Card h) {
		high = h;
	}
	public Hand(){
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Returns the whole hand
	 * @return List<Card>
	 */
	public List<Card> getHand(){
		return hand;
	}
	
	/**
	 * Removes all cards from hand
	 */
	public List<Card> discardHand(){
		List<Card> cards = new ArrayList<Card>(hand);
		hand.clear();
		return cards;
	}
	
	/**
	 * Add card c to player hand. C cannot be null
	 * @param c Card to be added
	 */
	public void add(Card c){
		hand.add(c);
	}
	
	/**
	 * Removes the first instance of a specified card from the hand
	 * @param c Card to be removed
	 */
	public void remove(String cardName){
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			if (cardName.equals(it.next().getCardName())) {
				it.remove();
				break;
			}
		}
	}
	
	/**
	 * Removes the card from the specified index
	 * @param pos
	 */
	public void removeByIndex(int pos){
		hand.remove(hand.get(pos));
	}
	
	/**
	 * returns the number of cards in the players hand
	 * @return int
	 */
	public int getNumCards(){
		return hand.size();
	}
	
	/**
	 * Returns the first instance of a card given its name
	 * @param cardname name of card (e.g. Blue 2)
	 * @return Card or null if not found
	 */
	public Card getCardByName(String cardname){
		Card c;
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			c = it.next();
			if (cardname.equals(c.getCardName())) {
				return c;
			}
		}
		return null;
	}
	
	
	/**
	 * Returns a card if it exists in the hand
	 * @return Card or null if no card found
	 */
	public Card getCard(Card card){
		Card temp;
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			temp = it.next();
			if (card.equals(it.next())) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Returns a card from a specific index
	 * @param index index of card
	 * @return Card or null if index is out of bounds
	 */
	public Card getCardbyIndex(int index){
		if(index < hand.size()){
			Card c = hand.get(index);
			return c;
		}
		return null;
	}
	
	/**
	 * Checks if an instance of the card is in the hand
	 * @param name name of card
	 * @return boolean
	 */
	public boolean contains(String name){
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			if (name.equals(it.next().getCardName())) {
				return true;
			}
		}
		return false;
	}
	
}