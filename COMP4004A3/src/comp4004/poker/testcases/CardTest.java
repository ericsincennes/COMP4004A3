package comp4004.poker.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp4004.poker.Card;
import comp4004.poker.Card.*;


public class CardTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void CardConstructor() {
		Card acespade = new Card(CardSuit.Spades, 14);
		assertTrue(acespade.getCardSuit() == CardSuit.Spades);
		assertTrue(acespade.getCardValue() == 14);
		assertEquals(acespade.getCardName(),"Ace of Spades");
		
		Card twospade = new Card(CardSuit.Spades, 2);
		assertTrue(twospade.getCardSuit() == CardSuit.Spades);
		assertTrue(twospade.getCardValue() == 2);
		assertEquals(twospade.getCardName(),"2 of Spades");
		
		Card fiveheart = new Card(CardSuit.Hearts, 5);
		assertTrue(fiveheart.getCardSuit() == CardSuit.Hearts);
		assertTrue(fiveheart.getCardValue() == 5);
		assertEquals(fiveheart.getCardName(),"5 of Hearts");
		
		Card tendiamond = new Card(CardSuit.Diamonds, 10);
		assertTrue(tendiamond.getCardSuit() == CardSuit.Diamonds);
		assertTrue(tendiamond.getCardValue() == 10);
		assertEquals(tendiamond.getCardName(),"10 of Diamonds");
		
		Card jackclub = new Card(CardSuit.Clubs, 11);
		assertTrue(jackclub.getCardSuit() == CardSuit.Clubs);
		assertTrue(jackclub.getCardValue() == 11);
		assertEquals(jackclub.getCardName(),"Jack of Clubs");
	}

}
