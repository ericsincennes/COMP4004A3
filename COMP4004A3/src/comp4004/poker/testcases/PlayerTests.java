
package comp4004.poker.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;

public class PlayerTests {
	Player p;
	
	@Before
	public void setUp() throws Exception {
		p = new Player();
	}
	
	@Test
	public void addCardToHand() {
		p.addCard(new Card(CardSuit.Spades, 6));
		assertTrue(p.getHand().contains("6 of Spades"));
	}
	
	@Test
	public void removeCardFromHand() {
		p.addCard(new Card(CardSuit.Spades, 8));
		assertTrue(p.getHand().contains("8 of Spades"));
		p.removeCard("8 of Spades");
		assertFalse(p.getHand().contains("8 of Spades"));
	}
	
}
