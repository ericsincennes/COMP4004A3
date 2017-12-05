package comp4004.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp4004.poker.Card.CardSuit;

public class Deck {
	private List<Card> cards; //front is index 0
	private boolean isDiscard;
	private Deck discardPile;
	
	private Deck() {
		cards = new ArrayList<Card>();
		isDiscard = false;
		discardPile = null;
	}
	
	public static Deck createDeck(Deck discard) {
		Deck theDeck = new Deck();
		theDeck.isDiscard = false;
		theDeck.discardPile = discard;
		return theDeck;
	}
	
	/**
	 * Initialize the standard deck with 52 cards.
	 */
	public void initDeck(){
		for(int i=0; i<13; i++){
			cards.add(new Card(CardSuit.Spades, (i+1)));
			cards.add(new Card(CardSuit.Hearts, (i+1)));
			cards.add(new Card(CardSuit.Diamonds, (i+1)));
			cards.add(new Card(CardSuit.Clubs, (i+1)));
		}
		
		shuffle();
	}
	
	public static Deck createDiscard() {
		Deck theDeck = new Deck();
		theDeck.isDiscard = true;
		return theDeck;
	}
	
	public void addCard(Card c) {
		cards.add(c);
	}
	
	public void addToDiscard(Card c) {
		if (c == null) return;
		if (isDiscard) {
			cards.add(c);
		}
		else {
			discardPile.cards.add(c);
		}
	}
	
	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		if (!isDiscard) {
			Collections.shuffle(cards);
		}
	}
	
	/**
	 * Peeks at top card of deck
	 * @return Card
	 */
	public Card peekDeck() {
		return cards.get(0);
	}
	
	public int getSize() {
		return cards.size();
	}
	
	/**
	 * Returns the list of cards in the discard pile
	 * @return List<Card>
	 */
	public List<Card> viewDiscard() {
		List <Card> viewedCards;
		if (isDiscard) {
			viewedCards = new ArrayList<Card>(cards);
		}
		else {
			viewedCards = new ArrayList<Card>(discardPile.cards);
		}
		return viewedCards;
	}
	
	public boolean contains(String name) {
		for (Card c : cards) {
			if (c.getCardName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Draw a card from the deck and remove it
	 * @return Card
	 */
	public Card draw() { 
		if (isDiscard) {
			return null;
		}
		if (cards.isEmpty()) {
			cards.addAll(discardPile.cards);
			discardPile.cards.clear();
			shuffle();
		}
		Card toDraw = (cards.isEmpty()) ? null : cards.remove(0);
		return toDraw;
	}
}