package comp4004.poker.testcases;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;


public class BoardStateTest {
	RulesEngine rules;
	
	@Before
	public void setUp() throws Exception {
		rules = RulesEngine.testRuleEngine(3);
		rules.registerThread(1);
		rules.registerThread(2);
		rules.registerThread(3);
		
		rules.initFGame();
		assertFalse(rules.isRunning());
		rules.initGame();
		assertEquals(5, rules.getPlayerById(1).getHandSize());
		assertEquals(5, rules.getPlayerById(2).getHandSize());
		Card card1 = new Card(CardSuit.Spades, 3);
		Card card2 = new Card(CardSuit.Spades, 4);
		rules.getPlayerById(1).addCard(card1);
		rules.getPlayerById(1).removeCard("3 of Spades");
		rules.getPlayerById(1).getDisplay().addCard(card1);
		rules.getPlayerById(2).addCard(card2);
		rules.getPlayerById(2).removeCard("4 of Spades");
		rules.getPlayerById(2).getDisplay().addCard(card2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoardState() {
		BoardState b = rules.makeBoardState(rules.getPlayerById(2));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(b);
			oos.close();
			System.out.println("Size of boardstate is: " + baos.size());
		} catch (Exception e) {
			System.out.println("Error");
		}
		assertEquals(rules.getPlayerById(2).getID(), b.owner);
		assertEquals(b.players.get(0), Long.valueOf(b.owner));
		assertEquals(3, b.players.size());
		assertEquals(5, b.hand.size());
		assertEquals(1, b.revealed.get(0).size());
		assertEquals("4 of Spades", b.revealed.get(0).get(0).getCardName());
		for (int i=1; i<b.players.size(); i++) {
			if (b.players.get(i) == 1) {
				assertEquals(1, b.revealed.get(i).size());
				assertEquals("3 of Spades", b.revealed.get(i).get(0).getCardName());
			}
			else {
				assertEquals(0, b.revealed.get(i).size());
			}
		}
	}
	
	@Test
	public void testEquality() {
		BoardState a1 = rules.makeBoardState(rules.getPlayerById(1));
		BoardState a2 = rules.makeBoardState(rules.getPlayerById(1));
		BoardState a3 = rules.makeBoardState(rules.getPlayerById(1));
		BoardState b1 = rules.makeBoardState(rules.getPlayerById(3));
		
		assertFalse(a1.equals(null));
		assertFalse(a1.equals(new Card(CardSuit.Clubs, 3)));
		assertEquals(a1, a1);
		assertEquals(a1, a2);
		assertEquals(a2, a1);
		assertEquals(a1, a3);
		assertNotEquals(a1, b1);
		assertNotEquals(b1, a2);
	}

}
