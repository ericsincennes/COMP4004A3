package comp4004.poker;

import java.util.ArrayList;
import java.util.List;

import comp4004.poker.Card.*;



public class PointsBoard {
	private List<Card> exchangedCards;
	
	public PointsBoard() {
		exchangedCards = new ArrayList<Card>();

	}
	
	/**
	 * Add Card to display 
	 * @param c Card to be added
	 */
	public boolean addCard(Card c) {
		exchangedCards.add(c);
		return true;
	}
	
	
	/**
	 * checks if the display board contains the card played, actions and points card
	 * @param name - name of card to check
	 */
	public boolean contains(String name) {
		for (Card c : exchangedCards) {
			if (name.equals(c.getCardName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return specific non-action card played to display
	 * @param index index of card to be retrieved
	 * @return Card
	 */
	public Card getCard(int index) {
		return exchangedCards.get(index);
	}
	
	public Card getCard(String name) {
		for (Card c : exchangedCards) {
			if (name.equals(c.getCardName())) {
				return c;
			}
		}
		return null;
	}

	public List<Card> getCards() {
		List<Card> viewedCards = new ArrayList<Card>(exchangedCards);
		return viewedCards;
	}
	
	/**
	 * Clears board and returns the cards cleared
	 */
	public List<Card> clearBoard() {
		List<Card> cleared = new ArrayList<Card>();
		cleared.addAll(exchangedCards);
		exchangedCards.clear();
		return cleared;
	}
}
