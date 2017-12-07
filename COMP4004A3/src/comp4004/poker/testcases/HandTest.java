package comp4004.poker.testcases;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;


public class HandTest {
	Hand h;
	
	@Before
	public void setUp() throws Exception {
		h = new Hand();
	}

	@After
	public void tearDown() throws Exception {}

	
	@Test
	public void createHand() {
		assertTrue(h != null);
	}

	@Test
	public void addCard(){
		for(int i = 1; i < 11; i++){
			Card c = new Card(CardSuit.Diamonds , i+1);
			h.add(c);
		}
		assertEquals(h.getNumCards(), 10);
	}
	
	@Test
	public void removeByCard(){
		h.add(new Card(CardSuit.Hearts, 2));
		h.add(new Card(CardSuit.Hearts, 3));
		h.remove("3 of Hearts");		
		assertFalse(h.contains("3 of Hearts"));
		assertEquals(h.getNumCards(),1);
	}
	
	@Test
	public void removeByIndex(){
		addCard();
		h.removeByIndex(0);
		assertFalse(h.contains("2 of Diamonds"));
		assertEquals(h.getNumCards(), 9);
	}
	
	@Test
	public void getCard(){
		Card q = h.getCard(new Card(CardSuit.Clubs, 4));
		assertEquals(q , null);
	}
	
	@Test
	public void getCardFromHand(){
		Card temp = new Card(CardSuit.Clubs, 7); 
		h.add(temp);
		
		Card c = h.getCardByName("7 of Clubs");
		
		assertEquals(c.getCardName(), "7 of Clubs");
		assertTrue(h.contains("7 of Clubs"));
	}
}
