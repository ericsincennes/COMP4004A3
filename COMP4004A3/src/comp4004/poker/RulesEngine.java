
package comp4004.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import comp4004.poker.Hand.Strength;


public class RulesEngine {
	private HashMap<Long, Player> players;
	private List<Player> playersList;
	private List<Card> exchangeList = new ArrayList<Card>();
	private int numPlayers = 0, expectedPlayers, expectedai;
	private Deck deck, discard;
	protected long first;

	public RulesEngine(int i, int j){
		expectedPlayers = i;
		expectedai = i-j;
		players = new HashMap<Long, Player>();
		playersList = new ArrayList<Player>();
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
		deck.initDeck();
	}
	
	/**
	 * Returns a list containing all players regardless
	 * of if they are playing in the current tournament or not
	 * @return List
	 */
	public synchronized List<Player> getPlayerList(){
		return playersList;
	}
	
	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return player number, as in order they joined game
	 */
	public synchronized int registerThread(long ID){
		//is game full?	
		if(players.containsKey(ID)){
			notifyAll();
			return -1;
		}
		else if(numPlayers >= expectedPlayers){
			notifyAll();
			return -1;
		}

		Player p = new Player();
		p.setid(ID);
		players.put(ID, p);
		playersList.add(p);
		numPlayers++;
		return numPlayers;
	}

	/**
	 * Checks if a tournament is running.
	 */
	public boolean isRunning(){
		int count = 0;
		for(Player p : playersList){
			if(p.getPlaying()){
				count++;
			}
		}
		if(count > 1) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Does startup for the first tournament
	 * @return player number of the first tournament starter
	 */
	public synchronized long initFGame(){
		Collections.reverse(playersList);
		//notifyAll();
		for(Player p : playersList){
			for(int i = 0; i < 5; i++){
				drawHidCard(p.getID());
			}
		}
		first = playersList.get(0).getID();
		return first;
	}
	
	public void initGame(){
		if (isRunning()) {
			return;
		}
		for(Player p : playersList){
			p.setPlaying(true);
		}
	}


	/**
	 * Checks if the player can play their turn
	 * if true then draw a card
	 * else go to next player
	 * @param id player id
	 * @return boolean
	 */
	public boolean startTurn(long id){
		Player p = getPlayerById(id);
		if(p.getPlaying()){
			p.hasPlayedToBoard = false;
			return true;
		} else {
			Collections.rotate(playersList, -1);
			return false;
		}
	}
	
	/**
	 * Adds a hidden card to the players hand
	 * @param id ID of player
	 */
	public void drawHidCard(long id){
		getPlayerById(id).addCard(deck.draw());
	}
	
	public Card drawRevCard(long id){
		Card c = deck.draw();
		getPlayerById(id).addCard(c);
		return c;
	}
	
	public synchronized boolean discardCard(int posinhand, Long id){
		Player p = players.get(id);
		Card c;
		//Check if the card is in the players hand
		if(posinhand > -1 && posinhand < p.getHandSize()){
			c = p.getHand().getCardbyIndex(posinhand);
		} else {
			return false;
		}
		//Null checker
		if (c == null) {
			notifyAll();
			return false; 
		}
		exchangeList.add(c);
		p.getHand().removeByIndex(posinhand);
		return true;
	}
	
	public synchronized boolean discardCard(String cardname, Long id){
		Player p = players.get(id);
		Card c;
		//Check if the card is in the players hand
		if(p.getHand().contains(cardname)){
			c = p.getHand().getCardByName(cardname);
		} else {
			return false;
		}
		//Null checker
		if (c == null) {
			notifyAll();
			return false; 
		}
		exchangeList.add(c);
		p.getHand().remove(cardname);
		return true;
	}
	
	public void exchangeCard(long id){
		Player p = getPlayerById(id);
		for (Card c : exchangeList){
			Card reveal = drawRevCard(id);
			p.getDisplay().addCard(reveal);
		}
		exchangeList.clear();
	}
	
	public void determineHandStrength(Player p) {
		Hand hand = p.getHand();
		List<Card> h = p.getHand().getHand();
		Collections.sort(h);
		if (isFOK(h, hand)) {
			hand.setStrength(Strength.FourOK);
		} else if (isFH(h, hand)) {
			hand.setStrength(Strength.FullHouse);
		} else if (isTOK(h, hand)) {
			hand.setStrength(Strength.ThreeOK); 
		} else if (isTP(h, hand)) {
			hand.setStrength(Strength.TwoPair);
		} else if (isOP(h, hand)) {
			hand.setStrength(Strength.OnePair);
		} else if (isRoyal(h, hand)) {
			hand.setStrength(Strength.Royal);
		} else if (isStrFlu(h, hand)) {
			hand.setStrength(Strength.StrFlush);
		} else if (isStr(h, hand)) {
			hand.setStrength(Strength.Straight);
		} else if (isFlush(h, hand)) {
			hand.setStrength(Strength.Flush);
		} else {
			hand.setStrength(Strength.HighCard);
			hand.setTieBreaker(h.get(4));
			hand.setPTieBreaker(h.get(3));
		}
		System.out.println(hand.getStrength());
	}
	
	//four of a kind
	public boolean isFOK(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(3).getCardValue() || h.get(1).getCardValue() == h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(1));
			return true;
		}
		return false;
	}
	
	//full house
	public boolean isFH(List<Card> h, Hand hand) {
		//low three of a kind and high pair
		if (h.get(0).getCardValue() == h.get(2).getCardValue() && h.get(3).getCardValue() == h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(0));
			return true;
		//low pair and high three of a kind
		} else if (h.get(2).getCardValue() == h.get(4).getCardValue() && h.get(0).getCardValue() == h.get(1).getCardValue()) {
			hand.setTieBreaker(h.get(4));
			return true;
		}
		return false;
	}
	
	//three of a kind
	public boolean isTOK(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(2).getCardValue() && h.get(3).getCardValue() != h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(0));
			return true;
		} else if (h.get(2).getCardValue() == h.get(4).getCardValue() && h.get(0).getCardValue() != h.get(1).getCardValue()) {
			hand.setTieBreaker(h.get(4));
			return true;
		} else if (h.get(1).getCardValue() == h.get(3).getCardValue()) {
			hand.setTieBreaker(h.get(1));
			return true;
		}
		return false;
	}
	
	//two pair
	public boolean isTP(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(1).getCardValue() && h.get(2).getCardValue() == h.get(3).getCardValue()) {
			if (h.get(0).getCardValue() > h.get(2).getCardValue()) {
				hand.setTieBreaker(h.get(0));
				hand.setPTieBreaker(h.get(2));
				hand.setHigh(h.get(4));
			} else {
				hand.setTieBreaker(h.get(2));
				hand.setPTieBreaker(h.get(0));
				hand.setHigh(h.get(4));
			}
			return true;
		} else if (h.get(0).getCardValue() == h.get(1).getCardValue() && h.get(3).getCardValue() == h.get(4).getCardValue()) {
			if (h.get(0).getCardValue() > h.get(3).getCardValue()) {
				hand.setTieBreaker(h.get(0));
				hand.setPTieBreaker(h.get(3));
				hand.setHigh(h.get(2));
			} else {
				hand.setTieBreaker(h.get(3));
				hand.setPTieBreaker(h.get(0));
				hand.setHigh(h.get(2));
			}
			return true;
		} else if (h.get(1).getCardValue() == h.get(2).getCardValue() && h.get(3).getCardValue() == h.get(4).getCardValue()) {
			if (h.get(1).getCardValue() > h.get(3).getCardValue()) {
				hand.setTieBreaker(h.get(1));
				hand.setPTieBreaker(h.get(3));
				hand.setHigh(h.get(0));
			} else {
				hand.setTieBreaker(h.get(3));
				hand.setPTieBreaker(h.get(1));
				hand.setHigh(h.get(0));
			}
			return true;
		} 
		return false;
	}
	
	//one pair
	public boolean isOP(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(1).getCardValue() && (h.get(2).getCardValue() != h.get(3).getCardValue() && h.get(3).getCardValue() != h.get(4).getCardValue())) {
			hand.setTieBreaker(h.get(0));
			hand.setPTieBreaker(h.get(4));
			return true;
		} else if (h.get(1).getCardValue() == h.get(2).getCardValue() && (h.get(0).getCardValue() != h.get(3).getCardValue() && h.get(3).getCardValue() != h.get(4).getCardValue())) {
			hand.setTieBreaker(h.get(1));
			hand.setPTieBreaker(h.get(4));
			return true;
		} else if (h.get(2).getCardValue() == h.get(3).getCardValue() && (h.get(0).getCardValue() != h.get(1).getCardValue() && h.get(1).getCardValue() != h.get(4).getCardValue())) {
			hand.setTieBreaker(h.get(2));
			hand.setPTieBreaker(h.get(4));
			return true;
		} else if (h.get(3).getCardValue() == h.get(4).getCardValue() && (h.get(0).getCardValue() != h.get(1).getCardValue() && h.get(1).getCardValue() != h.get(2).getCardValue())) {
			hand.setTieBreaker(h.get(3));
			hand.setPTieBreaker(h.get(2));
			return true;
		}
		return false;
	}
	
	//straight
	public boolean isStr(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue()+1 == h.get(1).getCardValue() && h.get(0).getCardValue()+2 == h.get(2).getCardValue() 
						&& h.get(0).getCardValue()+3 == h.get(3).getCardValue() && h.get(0).getCardValue()+4 == h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(4));
			return true;
		//ace low card straight
		} else if (h.get(0).getCardValue()+1 == h.get(1).getCardValue() && h.get(0).getCardValue()+2 == h.get(2).getCardValue() 
				&& h.get(0).getCardValue()+3 == h.get(3).getCardValue() && h.get(0).getCardValue()+12 == h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(3));
			return true;
		}
		return false;
	}
	
	//flush
	public boolean isFlush(List<Card> h, Hand hand) {
		if (h.get(0).getCardSuit() == h.get(1).getCardSuit() && (h.get(0).getCardSuit() == h.get(2).getCardSuit() 
				&& h.get(0).getCardSuit() == h.get(3).getCardSuit() && h.get(0).getCardSuit() == h.get(4).getCardSuit())) {
			hand.setTieBreaker(h.get(4));
			return true;
		} 
		return false;
	}
	
	//straight flush
	public boolean isStrFlu(List<Card> h, Hand hand) {
		if (isFlush(h, hand) && isStr(h, hand)) {
			hand.setTieBreaker(h.get(4));
			return true;
		}
		return false;
	}
	
	//royal flush
	public boolean isRoyal(List<Card> h, Hand hand) {
		if (isStrFlu(h, hand) && h.get(0).getCardValue() == 1 && h.get(4).getCardValue() == 13) {
			hand.setTieBreaker(h.get(4));
			return true;
		}
		return false;
	}
	

	/**
	 * Checks if a player can end their turn
	 * 	if yes checks if the player is the last player left
	 * 		if yes ends the round
	 * 	else moves to the next player
	 * @param id player id
	 * @return boolean
	 */
	public boolean endTurn(long id){
		players.get(id).hasPlayedToBoard = false;
		//exchangeCard(id);
		players.get(id).setTurnOver(true);
		//players.get(id).setPlaying(false);
		Collections.rotate(playersList, -1);
		return true;
	}

	/**
	 * Clears the board for all players and resets the active player
	 */
	public void roundCleanup(){
		for(Player p : playersList){
			List<Card> toDiscard = p.getDisplay().clearBoard();
			for (Card c : toDiscard) {
				deck.addToDiscard(c);
			}
		}
	}
	
	/**
	 * checks if there is a winner to the game yet
	 * @returns the winning player or null if none exist
	 */
	public Player gameWinner() {
		Player winner = null;
		for (Player p : playersList) {
			if (numPlayers < 2) return p;
			if (!p.getTurnOver()) {
				return null;
			} else {
				determineHandStrength(p);
			}
		}
		for (Player p : playersList) {
			if (winner == null) {
				winner = p;
				System.out.println(winner);
			} else {
				if (winner.getHand().getStrength().ordinal() > p.getHand().getStrength().ordinal()) {
					winner = p;
					//check in strength order
				} else if (winner.getHand().getStrength() == p.getHand().getStrength()) {
					switch (winner.getHand().getStrength()) {
						case Royal:
							//compare suits
							if (winner.getHand().getTieBreaker().getCardSuit().ordinal() > p.getHand().getTieBreaker().getCardSuit().ordinal()) {
								winner = p;
							}
							return winner;
						case StrFlush:
							//compare high card
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							//compare suits
							} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
								if (winner.getHand().getTieBreaker().getCardSuit().ordinal() > p.getHand().getTieBreaker().getCardSuit().ordinal()) {
									winner = p;
								}
							}
							return winner;
						case FourOK:
							//compare values
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							}
							return winner;
						case FullHouse:
							//compare values of three of a kind
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							}
							return winner;
						case Flush:
							//compare high card
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							//compare suits
							} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
								if (winner.getHand().getTieBreaker().getCardSuit().ordinal() > p.getHand().getTieBreaker().getCardSuit().ordinal()) {
									winner = p;
								}
							}
							return winner;
						case Straight:
							//compare high card
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							//compare suits
							} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
								if (winner.getHand().getTieBreaker().getCardSuit().ordinal() > p.getHand().getTieBreaker().getCardSuit().ordinal()) {
									winner = p;
								}
							}
							return winner;
						case ThreeOK:
							//compare values
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							}
							return winner;
						case TwoPair:
							//compare values of higher pair
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							//compare values of lower pair
							} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
								if (winner.getHand().getPTieBreaker().getCardValue() < p.getHand().getPTieBreaker().getCardValue()) {
									winner = p;
									//compare value of remaining card
								} else if (winner.getHand().getPTieBreaker().getCardValue() == p.getHand().getPTieBreaker().getCardValue()) {
									if (winner.getHand().getHigh().getCardValue() < p.getHand().getHigh().getCardValue()) {
										winner = p;
									//compare suit of remaining card
									} else if (winner.getHand().getHigh().getCardValue() == p.getHand().getHigh().getCardValue()) {
										if (winner.getHand().getHigh().getCardSuit().ordinal() > p.getHand().getHigh().getCardSuit().ordinal()) {
											winner = p;
										}
									}
								}
							}
							return winner;
						case OnePair:
							//compare value of pair
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							//compare value of high card
							} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
								if (winner.getHand().getPTieBreaker().getCardValue() < p.getHand().getPTieBreaker().getCardValue()) {
									winner = p;
									//compare suit of high card
								} else if (winner.getHand().getPTieBreaker().getCardValue() == p.getHand().getPTieBreaker().getCardValue()) {
									if (winner.getHand().getPTieBreaker().getCardSuit().ordinal() > p.getHand().getPTieBreaker().getCardSuit().ordinal()) {
										winner = p;
									}
								}
							}
							return winner;
						case HighCard:
							//compare value
							if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
								winner = p;
							//compare suit
							} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
								if (winner.getHand().getTieBreaker().getCardSuit().ordinal() > p.getHand().getTieBreaker().getCardSuit().ordinal()) {
									winner = p;
								}
							}
							return winner;
						default:
							return null;
					}
				} 
			}
		}
		return null;
	}
	
	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	private void print(String s){
		System.out.println(s);
	}

	/**
	 * Returns the player if the id exists
	 * @param id Long 
	 * @return Player
	 */
	public Player getPlayerById(long id){
		if(players.containsKey(id)){
			return players.get(id);
		}
		return null;
	}
	
	
	public Deck getDeck(){
		return deck;
	}
	
	public void setDeck(Deck d){
		deck = d;
	}

	public long getFirst(){
		return first;
	}
	/**
	 * Gets the boardstate of a player, that is all publicly available info for that player
	 * @param p the player who's boardstate we're fetching
	 * @return the boardstate obj created
	 */
	public BoardState makeBoardState(Player p) {
		return new BoardState(p, playersList, p.getHand(), deck);
	}
	
	/**
	 * Removes a player from the game. Used if someone disconnects.
	 * @param threadID - the ID of the player to remove from game
	 */
	public void removePlayer(long threadID) {
		Player toRemove = getPlayerById(threadID);
		
		List<Card> toDiscard = toRemove.getDisplay().clearBoard();
		toDiscard.addAll(toRemove.getHand().discardHand());
		for (Card c : toDiscard) { deck.addToDiscard(c); }
		
		players.remove(threadID);
		playersList.remove(toRemove);
		numPlayers--; expectedPlayers--;
		
	}
	
	/**
	 * Initalizes a testing rules engine. All cards are squires
	 * @param i number of expected players
	 * @return RulesEngine
	 */
	public static RulesEngine testRuleEngine(int i, int j) {
		RulesEngine test = new RulesEngine(i,j);
		test.deck = Deck.createDeck(test.discard);
		test.deck.testDeck();
		return test;
	}

}