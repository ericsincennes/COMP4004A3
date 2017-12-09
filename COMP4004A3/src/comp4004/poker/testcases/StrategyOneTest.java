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
import comp4004.poker.ai.StrategyContext;
import comp4004.poker.ai.StrategyOne;


public class StrategyOneTest {
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
	public void gameSetUp2P() {
		rules = new RulesEngine(2);
		rules.registerThread(1);
		rules.registerThread(2);
		
		rules.initFGame();
		assertFalse(rules.isRunning());
		rules.initGame();
		rules.getPlayerById(1).getHand().discardHand();
		rules.getPlayerById(2).getHand().discardHand();
		b1 = updateBoard(1);
		b2 = updateBoard(2);
		assertEquals(0, rules.getPlayerById(1).getHandSize());
		assertEquals(0, rules.getPlayerById(2).getHandSize());
		//last player to join goes first
		assertEquals(rules.getFirst(), 2);
		rules.setDeck(td);
		assertEquals(td.getSize(), 0);
	}
	
	@Test
	public void gameSetUp3P() {
		rules = new RulesEngine(3);
		rules.registerThread(1);
		rules.registerThread(2);
		rules.registerThread(3);
		
		rules.initFGame();
		assertFalse(rules.isRunning());
		rules.initGame();
		rules.getPlayerById(1).getHand().discardHand();
		rules.getPlayerById(2).getHand().discardHand();
		rules.getPlayerById(3).getHand().discardHand();
		b1 = updateBoard(1);
		b1 = updateBoard(2);
		b2 = updateBoard(3);
		assertEquals(0, rules.getPlayerById(1).getHandSize());
		assertEquals(0, rules.getPlayerById(2).getHandSize());
		assertEquals(0, rules.getPlayerById(3).getHandSize());
		//last player to join goes first
		assertEquals(rules.getFirst(), 3);
		rules.setDeck(td);
		assertEquals(td.getSize(), 0);
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
	public void rfSpadeHeart() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Spades, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Hearts, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Spades);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Hearts);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfSpadeClub() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Spades, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Spades);
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void rfSpadeDiamond() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Spades, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Diamonds, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Spades);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Diamonds);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfHeartClub() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Hearts, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Hearts);
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void rfHeartDiamond() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Diamonds, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Hearts, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Diamonds);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Hearts);
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void rfClubDiamond() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Diamonds, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Diamonds);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsSF() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getSF(CardSuit.Hearts, 13, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.StrFlush);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeats4OK() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get4OK(9, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FourOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.FourOK);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsFH() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Spades, 9, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsFlush() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 9));
		p1.getHand().add(new Card(CardSuit.Hearts, 11));
		p1.getHand().add(new Card(CardSuit.Hearts, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 14));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Clubs, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Hearts);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		assertTrue(p2.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsStr() {
		gameSetUp2P();
		
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeats3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Spades, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Spades, 9, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertEquals(p2.getHand().getStrength(), Strength.TwoPair);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	
	@Test
	//full house post exchange
	public void rfBeatsFHPE() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 10));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 14));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Hearts, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void rfHKBeatsRF() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 11));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Hearts, 14));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getRF(CardSuit.Clubs, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		assertTrue(p1.getHand().getStrength() == Strength.Royal);
		assertTrue(p1.getHand().getTieBreaker().getCardSuit() == CardSuit.Clubs);

		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void sfBeats4OK() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Hearts, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get4OK(8, p2);
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FourOK);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void sfBeatsFH() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Hearts, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 9, p2);
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void sfBeatsFlush() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getSF(CardSuit.Hearts, 13, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.StrFlush);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void sfBeatsStr() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Hearts, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Diamonds, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void sfBeats3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Spades, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Hearts, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 9, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void sfBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Hearts, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void sfBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Diamonds, 14));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 14));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getSF(CardSuit.Hearts, 13, p2);

		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.StrFlush);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void sfBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Diamonds, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Hearts, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void FourOKBeatsFH() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = get4OK(8, p1);
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FourOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Spades, 9, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	@Test
	public void FourOKBeatsFlush() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get4OK(8, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FourOK);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	@Test
	public void FourOKBeatsStr() {
		gameSetUp2P();
		Player p1 = rules.getPlayerList().get(0);
		p1 = get4OK(8, p1);
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FourOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Diamonds, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	@Test
	public void FourOKBeats3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Spades, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get4OK(8, p1);
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FourOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 9, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 13));

		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	@Test
	public void FourOKBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get4OK(8, p1);
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FourOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	@Test
	public void FourOKBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Diamonds, 14));
		td.addCard(new Card(CardSuit.Spades, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get4OK(8, p1);
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FourOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void FourOKBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 9));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get4OK(8, p1);
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FourOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void fhBeatsFlush() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Diamonds, 9, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Diamonds, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 11));
		p2.getHand().add(new Card(CardSuit.Diamonds, 14));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Flush);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void fhBeatsStr() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 12));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Spades, 9, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void fhBeats3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Spades, 11));

		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Spades, 9, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 8, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void fhBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 10));

		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Spades, 9, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void fhBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 14));
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Spades, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Spades, 9, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 8));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Diamonds, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void fhBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Spades, 9, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Clubs, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void flushBeatsStr() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void flushBeats3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Spades, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Hearts, 8, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.ThreeOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Diamonds, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 11));
		p2.getHand().add(new Card(CardSuit.Diamonds, 14));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Flush);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void flushBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void flushBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 14));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Hearts, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void flushBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void strBeats3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Hearts, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 12));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 8, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 13));

		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void strBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Hearts, 14));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.TwoPair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void strBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 14));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Hearts, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 12));;
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void strBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Diamonds, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 12));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void threeOKBeatsTP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Spades, 12));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Hearts, 14));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.TwoPair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 8, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void threeOKBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Spades, 12));
		td.addCard(new Card(CardSuit.Hearts, 14));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Hearts, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Hearts, 8, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.ThreeOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void threeOKBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Spades, 12));
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 9));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Hearts, 8, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Spades, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.ThreeOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void tpBeatsOP() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Clubs, 14));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Hearts, 7));

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 10));
		p1.getHand().add(new Card(CardSuit.Hearts, 14));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.TwoPair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void tpBeatsHK() {
		gameSetUp2P();
		
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 14));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void opBeatsHK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 14));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Diamonds, 7));
		td.addCard(new Card(CardSuit.Hearts, 7));
		td.addCard(new Card(CardSuit.Hearts, 10));
		td.addCard(new Card(CardSuit.Spades, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 8));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		p1.getHand().add(new Card(CardSuit.Hearts, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Diamonds, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void strExchange3OK() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Spades, 8));
		td.addCard(new Card(CardSuit.Clubs, 14));
		td.addCard(new Card(CardSuit.Diamonds, 11));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Hearts, 14));
		td.addCard(new Card(CardSuit.Hearts, 10));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 8));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		p1.getHand().add(new Card(CardSuit.Hearts, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		p2.getHand().add(new Card(CardSuit.Diamonds, 12));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Diamonds, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.HighCard);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void aceWinsTieStr() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Clubs, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void kingWinsTieStr() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void queenWinsTieStr() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void jackWinsTieFH() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Spades, 11, p1);
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Spades, 8, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void tenWinsTieFH() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Spades, 8, p1);
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Clubs, 10, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void nineWinsTieFH() {
		gameSetUp2P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Diamonds, 8, p1);
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.FullHouse);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Clubs, 8, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void oneAwayLoseRF() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Clubs, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getSF(CardSuit.Hearts, 13, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.StrFlush);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void oneAwayWinRF() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 14));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Clubs, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getSF(CardSuit.Hearts, 13, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.StrFlush);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void oneAwayLoseSF() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 12));
		p1.getHand().add(new Card(CardSuit.Hearts, 11));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get4OK(8, p2);
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FourOK);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void oneAwayWinSF() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 9));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 12));
		p1.getHand().add(new Card(CardSuit.Hearts, 11));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get4OK(8, p2);
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FourOK);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void oneAwayLoseFlush() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void oneAwayWinFlush() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Diamonds, 9));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 7));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 9));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Spades, 11));
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void oneAwayLoseStr() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Hearts, 8));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Hearts, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 12));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 8, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void oneAwayWinStr() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Hearts, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Clubs, 13));
		p1.getHand().add(new Card(CardSuit.Hearts, 10));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 12));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.HighCard);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Hearts, 8, p2);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.ThreeOK);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void exchange3OKLose() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Spades, 12));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Hearts, 8, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.ThreeOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void exchange3OKWin() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 7));
		td.addCard(new Card(CardSuit.Diamonds, 7));
		Player p1 = rules.getPlayerList().get(0);
		p1 = get3OK(CardSuit.Hearts, 8, p1);
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.ThreeOK);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void exchangeTPLose() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 11));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 8));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.TwoPair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void exchangeTPWin() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 9));
		td.addCard(new Card(CardSuit.Clubs, 14));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 8));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 9));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.TwoPair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		p2.getHand().add(new Card(CardSuit.Diamonds, 7));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.TwoPair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void exchangeOPLose() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 7));
		td.addCard(new Card(CardSuit.Diamonds, 7));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 8));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void exchangeOPWin() {
		gameSetUp2P();
		td.addCard(new Card(CardSuit.Clubs, 13));
		td.addCard(new Card(CardSuit.Spades, 13));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 8));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Hearts, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Hearts, 12));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void threePlayersAllDiff() {
		gameSetUp3P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = get3OK(CardSuit.Clubs, 10, p2);
		p2.getHand().add(new Card(CardSuit.Spades, 7));
		p2.getHand().add(new Card(CardSuit.Hearts, 7));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.FullHouse);
		
		Player p3 = rules.getPlayerList().get(2);
		p3 = get4OK(14, p3);
		p3.getHand().add(new Card(CardSuit.Diamonds, 11));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.FourOK);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		assertEquals(p3, rules.gameWinner());
	}
	
	@Test
	public void threePlayers2Equal() {
		gameSetUp3P();

		Player p1 = rules.getPlayerList().get(0);
		p1 = getSF(CardSuit.Clubs, 13, p1);
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.StrFlush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getSF(CardSuit.Hearts, 12, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.StrFlush);
		
		Player p3 = rules.getPlayerList().get(2);
		p3 = get3OK(CardSuit.Spades, 7, p3);
		p3.getHand().add(new Card(CardSuit.Spades, 11));
		p3.getHand().add(new Card(CardSuit.Diamonds, 11));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.FullHouse);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void threePlayersAllEqual() {
		gameSetUp3P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Spades, 12));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Hearts, 10));
		p2.getHand().add(new Card(CardSuit.Hearts, 9));
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Hearts, 12));
		p3.getHand().add(new Card(CardSuit.Diamonds, 11));
		p3.getHand().add(new Card(CardSuit.Diamonds, 10));
		p3.getHand().add(new Card(CardSuit.Diamonds, 9));
		p3.getHand().add(new Card(CardSuit.Diamonds, 8));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.Straight);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void fourPlayersAllDiff() {
		gameSetUp4P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2  = getRF(CardSuit.Spades, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p3 = rules.getPlayerList().get(2);
		p3 = get3OK(CardSuit.Clubs, 9, p3);
		p3.getHand().add(new Card(CardSuit.Hearts, 10));
		p3.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.FullHouse);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Diamonds, 7));
		p4.getHand().add(new Card(CardSuit.Diamonds, 11));
		p4.getHand().add(new Card(CardSuit.Diamonds, 14));
		p4.getHand().add(new Card(CardSuit.Diamonds, 8));
		p4.getHand().add(new Card(CardSuit.Diamonds, 13));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.Flush);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void fourPlayers2Equal() {
		gameSetUp4P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Hearts, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 12));
		p2.getHand().add(new Card(CardSuit.Diamonds, 11));
		p2.getHand().add(new Card(CardSuit.Clubs, 10));
		p2.getHand().add(new Card(CardSuit.Diamonds, 9));
		p2.getHand().add(new Card(CardSuit.Diamonds, 8));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Straight);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Spades, 14));
		p3.getHand().add(new Card(CardSuit.Spades, 7));
		p3.getHand().add(new Card(CardSuit.Spades, 12));
		p3.getHand().add(new Card(CardSuit.Spades, 11));
		p3.getHand().add(new Card(CardSuit.Spades, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.Flush);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Diamonds, 7));
		p4.getHand().add(new Card(CardSuit.Diamonds, 6));
		p4.getHand().add(new Card(CardSuit.Diamonds, 4));
		p4.getHand().add(new Card(CardSuit.Diamonds, 14));
		p4.getHand().add(new Card(CardSuit.Diamonds, 2));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.Flush);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p3, rules.gameWinner());
	}
	
	@Test
	public void fourPlayers3Equal() {
		gameSetUp4P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Diamonds, 9));
		p1.getHand().add(new Card(CardSuit.Diamonds, 8));
		p1.getHand().add(new Card(CardSuit.Diamonds, 14));
		p1.getHand().add(new Card(CardSuit.Diamonds, 11));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Clubs, 11));
		p2.getHand().add(new Card(CardSuit.Clubs, 14));
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Flush);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Hearts, 8));
		p3.getHand().add(new Card(CardSuit.Hearts, 11));
		p3.getHand().add(new Card(CardSuit.Hearts, 14));
		p3.getHand().add(new Card(CardSuit.Hearts, 7));
		p3.getHand().add(new Card(CardSuit.Hearts, 13));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.Flush);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 10));
		p4.getHand().add(new Card(CardSuit.Hearts, 9));
		p4.getHand().add(new Card(CardSuit.Clubs, 8));
		p4.getHand().add(new Card(CardSuit.Hearts, 7));
		p4.getHand().add(new Card(CardSuit.Spades, 6));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.Straight);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p3, rules.gameWinner());
	}
	
	@Test
	public void fourPlayersAllEqual() {
		gameSetUp4P();

		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Spades, 8));
		p1.getHand().add(new Card(CardSuit.Spades, 11));
		p1.getHand().add(new Card(CardSuit.Spades, 14));
		p1.getHand().add(new Card(CardSuit.Spades, 7));
		p1.getHand().add(new Card(CardSuit.Spades, 13));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Flush);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Clubs, 8));
		p2.getHand().add(new Card(CardSuit.Clubs, 11));
		p2.getHand().add(new Card(CardSuit.Clubs, 7));
		p2.getHand().add(new Card(CardSuit.Clubs, 14));
		p2.getHand().add(new Card(CardSuit.Clubs, 13));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Flush);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Hearts, 8));
		p3.getHand().add(new Card(CardSuit.Hearts, 11));
		p3.getHand().add(new Card(CardSuit.Hearts, 7));
		p3.getHand().add(new Card(CardSuit.Hearts, 14));
		p3.getHand().add(new Card(CardSuit.Hearts, 13));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.Flush);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Diamonds, 8));
		p4.getHand().add(new Card(CardSuit.Diamonds, 11));
		p4.getHand().add(new Card(CardSuit.Diamonds, 7));
		p4.getHand().add(new Card(CardSuit.Diamonds, 14));
		p4.getHand().add(new Card(CardSuit.Diamonds, 13));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.Flush);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p3.getID());
		p3 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p4.getID());
		b = b4;
		//runTurn(rules, b, p3);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p4.getID());
		p4 = p;
		
		assertEquals(p1, rules.gameWinner());
	}
	
	@Test
	public void fourPlayers1Exchange() {
		gameSetUp4P();
		td.addCard(new Card(CardSuit.Diamonds, 8));
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 11));
		p1.getHand().add(new Card(CardSuit.Diamonds, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 8));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.Straight);
		
		Player p2 = rules.getPlayerList().get(1);
		p2 = getRF(CardSuit.Spades, p2);
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.Royal);
		
		Player p3 = rules.getPlayerList().get(2);
		p3 = get3OK(CardSuit.Clubs, 9, p3);
		p3.getHand().add(new Card(CardSuit.Hearts, 10));
		p3.getHand().add(new Card(CardSuit.Clubs, 10));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.FullHouse);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 8));
		p4.getHand().add(new Card(CardSuit.Diamonds, 11));
		p4.getHand().add(new Card(CardSuit.Diamonds, 14));
		p4.getHand().add(new Card(CardSuit.Diamonds, 7));
		p4.getHand().add(new Card(CardSuit.Diamonds, 13));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.HighCard);
		
		Player p;
		BoardState b;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID());
		b = b1;
		//runTurn(rules, b, p1);
		//rules.getPlayerList().set(0, p1);
		rules.endTurn(p1.getID());
		p1 = p;
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID());
		b = b2;
		//runTurn(rules, b, p2);
		//rules.getPlayerList().set(0, p2);
		rules.endTurn(p2.getID());
		p2 = p;

		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID());
		b = b3;
		//runTurn(rules, b, p3);
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
		
		assertEquals(p2, rules.gameWinner());
	}
	
	@Test
	public void fourPlayersAllExchange() {
		gameSetUp4P();
		td.addCard(new Card(CardSuit.Clubs, 2));
		td.addCard(new Card(CardSuit.Hearts, 12));
		td.addCard(new Card(CardSuit.Diamonds, 9));
		td.addCard(new Card(CardSuit.Clubs, 8));
		td.addCard(new Card(CardSuit.Diamonds, 13));
		td.addCard(new Card(CardSuit.Spades, 11));
		td.addCard(new Card(CardSuit.Clubs, 11));
		td.addCard(new Card(CardSuit.Hearts, 13));
		td.addCard(new Card(CardSuit.Clubs, 13));
		td.addCard(new Card(CardSuit.Diamonds, 14));
		td.addCard(new Card(CardSuit.Spades, 14));
		td.addCard(new Card(CardSuit.Clubs, 14));
		
		Player p1 = rules.getPlayerList().get(0);
		p1.getHand().add(new Card(CardSuit.Diamonds, 12));
		p1.getHand().add(new Card(CardSuit.Spades, 12));
		p1.getHand().add(new Card(CardSuit.Clubs, 10));
		p1.getHand().add(new Card(CardSuit.Clubs, 9));
		p1.getHand().add(new Card(CardSuit.Clubs, 7));
		
		rules.determineHandStrength(p1);
		assertTrue(p1.getHand().getStrength() == Strength.OnePair);
		
		Player p2 = rules.getPlayerList().get(1);
		p2.getHand().add(new Card(CardSuit.Hearts, 8));
		p2.getHand().add(new Card(CardSuit.Spades, 8));
		p2.getHand().add(new Card(CardSuit.Clubs, 12));
		p2.getHand().add(new Card(CardSuit.Hearts, 11));
		p2.getHand().add(new Card(CardSuit.Diamonds, 10));
		rules.determineHandStrength(p2);
		assertTrue(p2.getHand().getStrength() == Strength.OnePair);
		
		Player p3 = rules.getPlayerList().get(2);
		p3.getHand().add(new Card(CardSuit.Hearts, 9));
		p3.getHand().add(new Card(CardSuit.Spades, 9));
		p3.getHand().add(new Card(CardSuit.Diamonds, 11));
		p3.getHand().add(new Card(CardSuit.Hearts, 10));
		p3.getHand().add(new Card(CardSuit.Hearts, 7));
		rules.determineHandStrength(p3);
		assertTrue(p3.getHand().getStrength() == Strength.OnePair);
		
		Player p4 = rules.getPlayerList().get(3);
		p4.getHand().add(new Card(CardSuit.Spades, 7));
		p4.getHand().add(new Card(CardSuit.Diamonds, 7));
		p4.getHand().add(new Card(CardSuit.Hearts, 14));
		p4.getHand().add(new Card(CardSuit.Spades, 13));
		p4.getHand().add(new Card(CardSuit.Diamonds, 8));
		rules.determineHandStrength(p4);
		assertTrue(p4.getHand().getStrength() == Strength.OnePair);
		
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
		
		assertEquals(p1, rules.gameWinner());
	}
	
	
	//Hand Sculpting functions
	//royal flush builder
	public Player getRF(CardSuit suit, Player p) {
		for (int i = 0; i<5; i++) {
			p.getHand().add(new Card(suit, i+10));
		}
		Collections.shuffle(p.getHand().getHand());
		return p;
	}
	//straight flush builder
	public Player getSF(CardSuit suit, int highval, Player p) {
		for (int i = 0; i<5; i++) {
			p.getHand().add(new Card(suit, highval-i));
		}
		Collections.shuffle(p.getHand().getHand());
		return p;
	}
	//four of a kind builder
	public Player get4OK(int four, Player p) {
		p.getHand().add(new Card(CardSuit.Spades, four));
		p.getHand().add(new Card(CardSuit.Hearts, four));
		p.getHand().add(new Card(CardSuit.Clubs, four));
		p.getHand().add(new Card(CardSuit.Diamonds, four));
		Collections.shuffle(p.getHand().getHand());
		return p;
	}
	//three of a kind builder
	public Player get3OK(CardSuit nsuit, int three, Player p) {
		switch (nsuit) {
		case Spades:
			p.getHand().add(new Card(CardSuit.Diamonds, three));
			p.getHand().add(new Card(CardSuit.Hearts, three));
			p.getHand().add(new Card(CardSuit.Clubs, three));
			break;
		case Hearts:
			p.getHand().add(new Card(CardSuit.Diamonds, three));
			p.getHand().add(new Card(CardSuit.Spades, three));
			p.getHand().add(new Card(CardSuit.Clubs, three));
			break;
		case Clubs:
			p.getHand().add(new Card(CardSuit.Diamonds, three));
			p.getHand().add(new Card(CardSuit.Hearts, three));
			p.getHand().add(new Card(CardSuit.Spades, three));
			break;
		case Diamonds:
			p.getHand().add(new Card(CardSuit.Spades, three));
			p.getHand().add(new Card(CardSuit.Hearts, three));
			p.getHand().add(new Card(CardSuit.Clubs, three));
			break;
		}
		Collections.shuffle(p.getHand().getHand());
		return p;
	}
	//update board
	public BoardState updateBoard(long id) {
		return rules.makeBoardState(rules.getPlayerById(id));
	}
	//run ai strategy one
	public void runTurn(RulesEngine r, BoardState b, Player p) {
		r.startTurn(p.getID());
		b = updateBoard(p.getID());
		strat.setStrategy(new StrategyOne());
		stratReturn = strat.executeStrategy(r, b, p.getID());
	}
}
