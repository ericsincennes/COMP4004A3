package comp4004.poker.testcases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;


public class DeckTest {
	Deck testDeck;
	Deck testDiscard;
	
	@Before
	public void setUp() throws Exception {
		testDiscard = Deck.createDiscard();
		testDeck = Deck.createDeck(testDiscard);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void addCard() {
		testDeck.addCard(new Card(CardSuit.Hearts, 6));
		assertTrue(testDeck.contains("6 of Hearts"));
	}
	
	@Test
	public void addToDiscard() {
		testDeck.addToDiscard(new Card(CardSuit.Spades, 6));
		assertTrue(testDiscard.contains("6 of Spades"));
	}
	
	@Test
	public void viewDiscard() {
		Card club4 = new Card(CardSuit.Clubs, 4);
		Card spade8 = new Card(CardSuit.Spades, 8);
		testDeck.addToDiscard(club4);
		testDeck.addToDiscard(spade8);
		assertEquals(testDeck.viewDiscard().get(0), club4);
		assertEquals(testDeck.viewDiscard().get(1), spade8);
	}
	
	@Test
	public void peek() {
		testDeck.addCard(new Card(CardSuit.Spades, 13));
		testDeck.addCard(new Card(CardSuit.Spades, 10));
		testDeck.addCard(new Card(CardSuit.Spades, 11));
		assertEquals(testDeck.peekDeck().getCardName(), "King of Spades");
	}
	
	@Test
	public void draw() {
		testDeck.addCard(new Card(CardSuit.Diamonds, 14));
		testDeck.addToDiscard(new Card(CardSuit.Diamonds, 7));
		testDeck.addToDiscard(new Card(CardSuit.Diamonds, 7));
		assertEquals(testDeck.draw().getCardName(),"Ace of Diamonds");
		assertEquals(testDeck.draw().getCardName(),"7 of Diamonds");
		assertEquals(testDeck.draw().getCardName(),"7 of Diamonds");
		assertTrue(testDeck.draw() == null);
	}
}
