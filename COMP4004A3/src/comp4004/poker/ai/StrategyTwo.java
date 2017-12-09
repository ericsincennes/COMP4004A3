package comp4004.poker.ai;

import java.util.ArrayList;
import java.util.List;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;
import comp4004.poker.Hand.Strength;

public class StrategyTwo implements Strategy{
	@Override
	public int runAI(RulesEngine r, BoardState b, long id){
		Player p = r.getPlayerById(id);
		Hand hand = p.getHand();
		List<Card> tempHand = new ArrayList<Card>();
		r.determineHandStrength(p);
		//hand is better than three of a kind = keep it
		if (hand.getStrength().getStrengthRank() < Strength.ThreeOK.getStrengthRank()) {
			return -3;
		} else if (suited(b)) {
			int s = 0, h = 0, c = 0, d = 0;
			for (Card card : hand.getHand()) {
				if (card.getCardSuit() == CardSuit.Spades) {
					s++;
				} else if (card.getCardSuit() == CardSuit.Hearts) {
					h++;
				} else if (card.getCardSuit() == CardSuit.Clubs) {
					c++;
				} else if (card.getCardSuit() == CardSuit.Diamonds) {
					d++;
				} 
			}
			if (s >= h || s >= c || s >= d) {
				for (Card card : hand.getHand()) {
					if (card.getCardSuit() != CardSuit.Spades) {
						tempHand.add(card);
					}
				}
			} else if (h >= c || h >= d || h > s) {
				for (Card card : hand.getHand()) {
					if (card.getCardSuit() != CardSuit.Hearts) {
						tempHand.add(card);
					}
				}
			} else if (c >= d || c > s || c > h) {
				for (Card card : hand.getHand()) {
					if (card.getCardSuit() != CardSuit.Clubs) {
						tempHand.add(card);
					}
				}
			} else if (d > s || d > h || d > c) {
				for (Card card : hand.getHand()) {
					if (card.getCardSuit() != CardSuit.Diamonds) {
						tempHand.add(card);
					}
				}
			}
		} else if (valued(b)) {
			for (Card c : hand.getHand()) {
				if (c.getCardValue() != hand.getTieBreaker().getCardValue()) {
					tempHand.add(c);
				}
			}
		} else {
			//should return -3 for end turn call
			// return 0 to switch to option 1
			return 0;
		}
		
		for (Card c : tempHand) {
			r.discardCard(c.getCardName(), id);
		}
		r.exchangeCard(id);
		
		//should return -3 for end turn call
		// return 0 to switch to option 1
		return -3;
	}
	
	//any opponent has revealed >2 cards of the same suit
	public boolean suited(BoardState b) {
		for (List<Card> boards : b.revealed) {
			int s = 0, h = 0, c = 0, d = 0;
			for (Card card : boards) {
				if (card.getCardSuit() == CardSuit.Spades) {
					s++;
				} else if (card.getCardSuit() == CardSuit.Hearts) {
					h++;
				} else if (card.getCardSuit() == CardSuit.Clubs) {
					c++;
				} else if (card.getCardSuit() == CardSuit.Diamonds) {
					d++;
				}
			}
			if (s > 2 || h > 2 || c > 2 || d > 2) {
				return true;
			}
		}
		return false;
	}
	
	//any opponent has revealed >2 cards of the same value
	public boolean valued(BoardState b) {
		for (List<Card> boards : b.revealed) {
			int count = 0;
			for (Card card : boards) {
				for (Card card2 : boards) {
					if (card.getCardValue() == card2.getCardValue()) {
						count++;
					}
					if (count > 2) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
