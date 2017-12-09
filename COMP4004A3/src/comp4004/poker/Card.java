package comp4004.poker;

import java.io.Serializable;

/**
 * Representation of Cards in Ivanhoe.
 * Implements Serializable for transfer over a network to the client.
 */
public class Card implements Serializable, Comparable<Card>{
	
	public enum CardSuit {
		
		Spades(1), Hearts(2), Clubs(3), Diamonds(4);
		
		private final int rank;
		private CardSuit(int val) {
			this.rank = val;
		}
		
		public int getSuitRank() {
			return rank;
		}
	}
	
	protected String cardName;
	protected int cardValue;
	protected CardSuit cardSuit;
	
	public Card(CardSuit suit, int value) {
		cardSuit = suit;
		cardValue = value;
		if (value == 14) {
			cardName = "Ace of " + suit.name();
		} else if (value == 11) {
			cardName = "Jack of " + suit.name();
		} else if (value == 12) {
			cardName = "Queen of " + suit.name();
		} else if (value == 13) {
			cardName = "King of " + suit.name();
		} else {
			cardName = value + " of " + suit.name();
		}		
	}
	
	public int getCardValue() {
		return cardValue;
	}
	
	public CardSuit getCardSuit() {
		return cardSuit;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public int compareTo(Card compCard) {
		int compValue = ((Card) compCard).getCardValue();
		return this.cardValue - compValue;
	}
}
