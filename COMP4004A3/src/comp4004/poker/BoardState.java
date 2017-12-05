package comp4004.poker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the boardstate one player can see. Used to give this info to the game client.
 *
 */
public final class BoardState implements Serializable{
	
	public long owner; //player by threadID
	public List<Long> players; //list of players in game, starting with current player
	public List<Card> hand;
	//next 4 board states corresponds to players list
	public List<List<Card>> revealed;
	//public List<Card> discarded;
	
	public BoardState(Player pOwner, List<Player> thePlayers, Hand pHand, Deck theDeck) {
		owner = pOwner.getID();
		
		int pListRot = thePlayers.indexOf(pOwner);
		int pListSize = thePlayers.size();
		players = new ArrayList<Long>(pListSize);
		revealed = new ArrayList<List<Card>>(pListSize);
		for (int i = 0; i < pListSize; i++) {
			players.add(thePlayers.get((i+pListRot)%pListSize).getID());
			revealed.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().getCards());
		}
		hand = pHand.getHand();
		//discarded = theDeck.viewDiscard();
	}
	
	@Override
	public boolean equals(Object otherobj) {
		if (!(otherobj instanceof BoardState)) {
			return false;
		}
		BoardState other = (BoardState) otherobj;
		boolean a,b,c;
		a = this.owner == other.owner;
		b = this.players.equals(other.players);
		c = this.hand.equals(other.hand);
		return a && b && c;
	}
	
}
