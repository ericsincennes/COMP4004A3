
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
	private List<Card> exchangeList;
	private int numPlayers = 0, expectedPlayers, expectedai;
	private Deck deck, discard;
	private int highestScore = 0;

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
	 * Returns the highest score recorded by the rules engine for the current tournament
	 * @return int
	 */
	public int getHighestScore() {
		return highestScore;
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
	public synchronized long initFirstTournament(){
		Collections.shuffle(playersList);
		//notifyAll();
		for(Player p : playersList){
			for(int i = 0; i < 5; i++){
				drawHidCard(p.getID());
			}
		}
		return playersList.get(0).getID();
	}


	/**
	 * Initializing gamestate for a tournament if no tournament is started
	 */
	public void initTournament(){
		if (isRunning()) {
			return;
		}
		for(Player p : playersList){
			p.setPlaying(true);
		}
	}
	
	
	/**
	 * Deals a hand of 5 cards to each player
	 */
	public void dealHand(){ //up for being replaced by initFirstTournament
		for(Player p : playersList){
			for(int i =0; i < 5; i++){
				drawHidCard(p.getID());
			}
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
			p.addCard(deck.draw());
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
	
	public synchronized boolean exchangeCard(int posinhand, Long id){
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
		return true;
	}
	
	public void exchange(long id){
		Player p = getPlayerById(id);
		p.exchangeCards(exchangeList);
		for (Card c : exchangeList){
			Card reveal = drawRevCard(id);
			p.getDisplay().addCard(reveal);
		}
	}
	
	public void determineHandStrength(long id) {
		Hand hand = getPlayerById(id).getHand();
		List<Card> h = getPlayerById(id).getHand().getHand();
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
		} else if (isStr(h, hand)) {
			hand.setStrength(Strength.Straight);
		} else if (isRoyal(h, hand)) {
			hand.setStrength(Strength.Royal);
		} else if (isStrFlu(h, hand)) {
			hand.setStrength(Strength.StrFlush);
		} else if (isFlush(h, hand)) {
			hand.setStrength(Strength.Flush);
		} else {
			hand.setStrength(Strength.HighCard);
			hand.setTieBreaker(h.get(4));
		}
	}
	
	public boolean isFOK(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(3).getCardValue() || h.get(1).getCardValue() == h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(1));
			return true;
		}
		return false;
	}
	
	public boolean isFH(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(2).getCardValue() && h.get(3).getCardValue() == h.get(4).getCardValue()) {
			hand.setTieBreaker(h.get(0));
			return true;
		} else if (h.get(2).getCardValue() == h.get(4).getCardValue() && h.get(0).getCardValue() == h.get(1).getCardValue()) {
			hand.setTieBreaker(h.get(4));
			return true;
		}
		return false;
	}
	
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
	
	public boolean isTP(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(1).getCardValue() && h.get(2).getCardValue() == h.get(3).getCardValue()) {
			if (h.get(0).getCardValue() > h.get(2).getCardValue()) {
				hand.setTieBreaker(h.get(0));
			} else {
				hand.setTieBreaker(h.get(2));
			}
			return true;
		} else if (h.get(0).getCardValue() == h.get(1).getCardValue() && h.get(3).getCardValue() == h.get(4).getCardValue()) {
			if (h.get(0).getCardValue() > h.get(3).getCardValue()) {
				hand.setTieBreaker(h.get(0));
			} else {
				hand.setTieBreaker(h.get(3));
			}
			return true;
		} else if (h.get(1).getCardValue() == h.get(2).getCardValue() && h.get(3).getCardValue() == h.get(4).getCardValue()) {
			if (h.get(1).getCardValue() > h.get(3).getCardValue()) {
				hand.setTieBreaker(h.get(1));
			} else {
				hand.setTieBreaker(h.get(3));
			}
			return true;
		} 
		return false;
	}
	
	public boolean isOP(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(1).getCardValue() && (h.get(2).getCardValue() != h.get(3).getCardValue() && h.get(3).getCardValue() != h.get(4).getCardValue())) {
			hand.setTieBreaker(h.get(0));
			return true;
		} else if (h.get(1).getCardValue() == h.get(2).getCardValue() && (h.get(0).getCardValue() != h.get(3).getCardValue() && h.get(3).getCardValue() != h.get(4).getCardValue())) {
			hand.setTieBreaker(h.get(1));
			return true;
		} else if (h.get(2).getCardValue() == h.get(3).getCardValue() && (h.get(0).getCardValue() != h.get(1).getCardValue() && h.get(1).getCardValue() != h.get(4).getCardValue())) {
			hand.setTieBreaker(h.get(2));
			return true;
		} else if (h.get(3).getCardValue() == h.get(4).getCardValue() && (h.get(0).getCardValue() != h.get(1).getCardValue() && h.get(1).getCardValue() != h.get(2).getCardValue())) {
			hand.setTieBreaker(h.get(3));
			return true;
		}
		return false;
	}
	
	public boolean isStr(List<Card> h, Hand hand) {
		if (h.get(0).getCardValue() == h.get(1).getCardValue()+1 && (h.get(0).getCardValue() == h.get(2).getCardValue()+2 
						&& h.get(0).getCardValue() == h.get(3).getCardValue()+3 && h.get(0).getCardValue() == h.get(4).getCardValue()+4)) {
			hand.setTieBreaker(h.get(4));
			return true;
		} 
		return false;
	}
	
	public boolean isFlush(List<Card> h, Hand hand) {
		if (h.get(0).getCardSuit() == h.get(1).getCardSuit() && (h.get(0).getCardSuit() == h.get(2).getCardSuit() 
				&& h.get(0).getCardSuit() == h.get(3).getCardSuit() && h.get(0).getCardSuit() == h.get(4).getCardSuit())) {
			hand.setTieBreaker(h.get(4));
			return true;
		} 
		return false;
	}
	
	public boolean isStrFlu(List<Card> h, Hand hand) {
		if (isFlush(h, hand) && isStr(h, hand)) {
			hand.setTieBreaker(h.get(4));
			return true;
		}
		return false;
	}
	
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
		highestScore = 0;
	}
	
	/**
	 * checks if there is a winner to the game yet
	 * @returns the winning player or null if none exist
	 */
	public Player gameWinner() {
		Player winner = null;
		for (Player p : playersList) {
			if (numPlayers < 2) return p;
			determineHandStrength(p.getID());
		}
		
		for (Player p : playersList) {
			if (winner == null) {
				winner = p;
			} else {
				if (winner.getHand().getStrength().ordinal() > p.getHand().getStrength().ordinal()) {
					winner = p;
				} else if (winner.getHand().getStrength() == p.getHand().getStrength()) {
					switch (winner.getHand().getStrength()) {
						case Royal:
						if (winner.getHand().getTieBreaker().getCardSuit().ordinal() > p.getHand().getTieBreaker().getCardSuit().ordinal()) {
							winner = p;
						}
						return winner;
						case StrFlush:
						if (winner.getHand().getTieBreaker().getCardValue() < p.getHand().getTieBreaker().getCardValue()) {
							winner = p;
						} else if (winner.getHand().getTieBreaker().getCardValue() == p.getHand().getTieBreaker().getCardValue()) {
							
						}
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

}