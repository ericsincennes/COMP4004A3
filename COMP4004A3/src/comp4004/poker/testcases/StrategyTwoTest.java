package comp4004.poker.testcases;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;
import comp4004.poker.Hand.Strength;
import comp4004.poker.ai.*;

public class StrategyTwoTest {
	RulesEngine rules;
	BoardState b1, b2, b3, b4;
	StrategyContext strat = new StrategyContext();
	int stratReturn = 0;
	Deck td = Deck.createDeck(Deck.createDiscard());

	@Before
	public void setUp() throws Exception {	

	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void gameSetUp4P() {
		rules = new RulesEngine(4);
		rules.registerThread(1);
		rules.registerThread(2);
		rules.registerThread(3);
		rules.registerThread(4);
		
		rules.initFGame();
		assertFalse(rules.isRunning());
		rules.initGame();
		rules.getPlayerById(1).getHand().discardHand();
		rules.getPlayerById(2).getHand().discardHand();
		rules.getPlayerById(3).getHand().discardHand();
		rules.getPlayerById(4).getHand().discardHand();
		b1 = updateBoard(1);
		b1 = updateBoard(2);
		b1 = updateBoard(3);
		b2 = updateBoard(4);
		assertEquals(0, rules.getPlayerById(1).getHandSize());
		assertEquals(0, rules.getPlayerById(2).getHandSize());
		assertEquals(0, rules.getPlayerById(3).getHandSize());
		assertEquals(0, rules.getPlayerById(4).getHandSize());
		//last player to join goes first
		assertEquals(rules.getFirst(), 4);
		rules.setDeck(td);
		assertEquals(td.getSize(), 0);
	}
	
	@Test
	public void allPlayerStratOne() {
		gameSetUp4P();
		apsoDeck();
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 2));
		p1.getHand().add(new Card(CardSuit.Clubs, 3));
		p1.getHand().add(new Card(CardSuit.Clubs, 4));
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Diamonds, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 2));
		p2.getHand().add(new Card(CardSuit.Spades, 3));
		p2.getHand().add(new Card(CardSuit.Spades, 4));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Spades, 7));
		p3.getHand().add(new Card(CardSuit.Spades, 9));
		p3.getHand().add(new Card(CardSuit.Hearts, 7));
		p3.getHand().add(new Card(CardSuit.Hearts, 9));
		p3.getHand().add(new Card(CardSuit.Clubs, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.TwoPair);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 13));
		p4.getHand().add(new Card(CardSuit.Spades, 12));
		p4.getHand().add(new Card(CardSuit.Hearts, 2));
		p4.getHand().add(new Card(CardSuit.Hearts, 3));
		p4.getHand().add(new Card(CardSuit.Hearts, 4));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		runTurn(rules, b, p4);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p4, rules.gameWinner());
	}
	
	@Test
	public void player2StratTwo() {
		gameSetUp4P();
		p2stDeck();
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 2));
		p1.getHand().add(new Card(CardSuit.Clubs, 3));
		p1.getHand().add(new Card(CardSuit.Clubs, 4));
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 2));
		p2.getHand().add(new Card(CardSuit.Diamonds, 5));
		p2.getHand().add(new Card(CardSuit.Spades, 14));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		p2.getHand().add(new Card(CardSuit.Spades, 4));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Spades, 8));
		p3.getHand().add(new Card(CardSuit.Clubs, 8));
		p3.getHand().add(new Card(CardSuit.Diamonds, 8));
		p3.getHand().add(new Card(CardSuit.Hearts, 9));
		p3.getHand().add(new Card(CardSuit.Clubs, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.ThreeOK);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Diamonds, 13));
		p4.getHand().add(new Card(CardSuit.Spades, 13));
		p4.getHand().add(new Card(CardSuit.Clubs, 13));
		p4.getHand().add(new Card(CardSuit.Hearts, 13));
		p4.getHand().add(new Card(CardSuit.Hearts, 11));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.FourOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		runTurn(rules, b, p4);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p4, rules.gameWinner());
	}
	
	@Test
	public void player3StratTwo() {
		gameSetUp4P();
		p3stDeck();
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 2));
		p1.getHand().add(new Card(CardSuit.Clubs, 3));
		p1.getHand().add(new Card(CardSuit.Clubs, 4));
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 2));
		p2.getHand().add(new Card(CardSuit.Spades, 3));
		p2.getHand().add(new Card(CardSuit.Spades, 4));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Spades, 8));
		p3.getHand().add(new Card(CardSuit.Clubs, 8));
		p3.getHand().add(new Card(CardSuit.Diamonds, 8));
		p3.getHand().add(new Card(CardSuit.Hearts, 9));
		p3.getHand().add(new Card(CardSuit.Clubs, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.ThreeOK);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 13));
		p4.getHand().add(new Card(CardSuit.Spades, 12));
		p4.getHand().add(new Card(CardSuit.Spades, 14));
		p4.getHand().add(new Card(CardSuit.Spades, 10));
		p4.getHand().add(new Card(CardSuit.Spades, 11));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		runTurn(rules, b, p4);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p4, rules.gameWinner());
	}
	
	@Test
	public void player4StratTwo() {
		gameSetUp4P();
		p4stDeck();
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 2));
		p1.getHand().add(new Card(CardSuit.Clubs, 3));
		p1.getHand().add(new Card(CardSuit.Clubs, 4));
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Diamonds, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 2));
		p2.getHand().add(new Card(CardSuit.Spades, 3));
		p2.getHand().add(new Card(CardSuit.Spades, 4));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Clubs, 5));
		p3.getHand().add(new Card(CardSuit.Diamonds, 5));
		p3.getHand().add(new Card(CardSuit.Hearts, 7));
		p3.getHand().add(new Card(CardSuit.Hearts, 9));
		p3.getHand().add(new Card(CardSuit.Clubs, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.OnePair);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 13));
		p4.getHand().add(new Card(CardSuit.Spades, 12));
		p4.getHand().add(new Card(CardSuit.Hearts, 2));
		p4.getHand().add(new Card(CardSuit.Hearts, 3));
		p4.getHand().add(new Card(CardSuit.Hearts, 4));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		runTurn(rules, b, p4);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p4, rules.gameWinner());
	}
	
	@Test
	public void stratTwoRFWin() {
		gameSetUp4P();
		strfwDeck();
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 2));
		p1.getHand().add(new Card(CardSuit.Clubs, 3));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 6));
		p2.getHand().add(new Card(CardSuit.Diamonds, 6));
		p2.getHand().add(new Card(CardSuit.Spades, 8));
		p2.getHand().add(new Card(CardSuit.Diamonds, 3));
		p2.getHand().add(new Card(CardSuit.Hearts, 2));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Spades, 7));
		p3.getHand().add(new Card(CardSuit.Spades, 6));
		p3.getHand().add(new Card(CardSuit.Hearts, 8));
		p3.getHand().add(new Card(CardSuit.Diamonds, 5));
		p3.getHand().add(new Card(CardSuit.Clubs, 5));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.OnePair);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 13));
		p4.getHand().add(new Card(CardSuit.Spades, 4));
		p4.getHand().add(new Card(CardSuit.Spades, 9));
		p4.getHand().add(new Card(CardSuit.Hearts, 3));
		p4.getHand().add(new Card(CardSuit.Hearts, 5));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		runTurn(rules, b, p4);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p4, rules.gameWinner());
	}
	
	public void apsoDeck() {
		td.addCard(new Card(CardSuit.Clubs, 2));
		td.addCard(new Card(CardSuit.Diamonds, 3));
		td.addCard(new Card(CardSuit.Diamonds, 4));
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Clubs, 10));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Diamonds, 10));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Spades, 10));
		td.addCard(new Card(CardSuit.Spades, 14));
	}
	
	public void p2stDeck() {
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Diamonds, 9));
		td.addCard(new Card(CardSuit.Spades, 9));
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Clubs, 6));
		td.addCard(new Card(CardSuit.Diamonds, 2));
		td.addCard(new Card(CardSuit.Diamonds, 3));
		td.addCard(new Card(CardSuit.Hearts, 8));
		td.addCard(new Card(CardSuit.Spades, 10));

	}
	
	public void p3stDeck() {
		td.addCard(new Card(CardSuit.Clubs, 2));
		td.addCard(new Card(CardSuit.Diamonds, 3));
		td.addCard(new Card(CardSuit.Diamonds, 4));
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Diamonds, 9));
		td.addCard(new Card(CardSuit.Spades, 9));
		td.addCard(new Card(CardSuit.Hearts, 8));
		td.addCard(new Card(CardSuit.Spades, 6));

	}
	
	public void p4stDeck() {
		td.addCard(new Card(CardSuit.Clubs, 2));
		td.addCard(new Card(CardSuit.Diamonds, 3));
		td.addCard(new Card(CardSuit.Diamonds, 4));
		td.addCard(new Card(CardSuit.Hearts, 6));
		td.addCard(new Card(CardSuit.Clubs, 10));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Diamonds, 9));
		td.addCard(new Card(CardSuit.Spades, 9));
		td.addCard(new Card(CardSuit.Hearts, 8));
		td.addCard(new Card(CardSuit.Spades, 6));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Spades, 10));

	}
	
	public void strfwDeck() {
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Diamonds, 9));
		td.addCard(new Card(CardSuit.Hearts, 9));
		
		td.addCard(new Card(CardSuit.Clubs, 4));
		td.addCard(new Card(CardSuit.Diamonds, 4));
		td.addCard(new Card(CardSuit.Hearts, 4));
		
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Hearts, 11));
		
		td.addCard(new Card(CardSuit.Spades, 12));
		td.addCard(new Card(CardSuit.Spades, 14));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Spades, 10));

	}
	
	//update board
	public BoardState updateBoard(long id) {
		return rules.makeBoardState(rules.getPlayerById(id));
	}
	//run ai strategy one
	public void runTurn(RulesEngine r, BoardState b, Player p) {
		//r.startTurn(p.getID());
		b = updateBoard(p.getID());
		if (!(r.getFirst() == p.getID())) {
			strat.setStrategy(new StrategyTwo());
			stratReturn = strat.executeStrategy(r, b, p.getID());
			if (stratReturn == 0) {
				strat.setStrategy(new StrategyOne());
				stratReturn = strat.executeStrategy(r, b, p.getID());
			}
		} else {
			strat.setStrategy(new StrategyOne());
			stratReturn = strat.executeStrategy(r, b, p.getID());
		}
	}
}
