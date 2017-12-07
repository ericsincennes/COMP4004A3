package comp4004.poker.testcases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;


public class PointsBoardTest {
	PointsBoard testBoard;
	
	@Before
	public void setUp() throws Exception {
		testBoard = new PointsBoard();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test 
	public void getCards() {
		testBoard.addCard(new Card(CardSuit.Spades, 7));
		assertTrue(testBoard.getCards() != null);
	}
	
	@Test
	public void getCardByIndex() {
		testBoard.addCard(new Card(CardSuit.Diamonds, 4));
		testBoard.addCard(new Card(CardSuit.Clubs, 8));
		testBoard.addCard(new Card(CardSuit.Hearts, 9));
		
		assertEquals(testBoard.getCard(1).getCardName(), "8 of Clubs");
		assertEquals(testBoard.getCard(1).getCardSuit(), CardSuit.Clubs);
	}
}
