package comp4004.poker.ai;

import comp4004.poker.*;
import comp4004.poker.Card.CardSuit;
import comp4004.poker.Hand.Strength;

public class StrategyOne implements Strategy{
	@Override
	public int runAI(RulesEngine r, BoardState b, long id){
		Player p = r.getPlayerById(id);
		Hand h = p.getHand();
		r.determineHandStrength(p);
		//System.out.println(p.getHand().getStrength());
		
		if (r.getDeck().getSize() == 0) {
			//should only occur in testing
			return -3;
		} else {
			int aFlush = almostFlush(h);
			int aStraight = almostStraight(h);
			//System.out.println(aFlush + " " + aStraight);
			//check if one off royal/straight flush
			if(aFlush != -1 && aStraight != -1 && aFlush == aStraight) {
				r.discardCard(aFlush, id);
			//upgrade flush to straight flush
			} else if (aStraight != -1 && h.getStrength() == Strength.Flush) {
				r.discardCard(aStraight, id);
			//upgrade straight to straight flush
			} else if (aFlush != -1 && h.getStrength() == Strength.Straight) {
				r.discardCard(aFlush, id);
			//upgrade straight flush to royal flush
			} else if (h.getStrength() == Strength.StrFlush && h.getCardbyIndex(4).getCardValue() == 13) {
				r.discardCard(0, id);
			//check if one off flush
			} else if (aFlush != -1) {
				r.discardCard(aFlush, id);
			//check if one off straight
			} else if (aStraight != -1) {
				r.discardCard(aStraight, id);
			//check if have three of kind only
			} else if (h.getStrength() == Strength.ThreeOK) {
				//aaabc
				if (h.getCardbyIndex(0).getCardValue() == h.getCardbyIndex(1).getCardValue()) {
					r.discardCard(4, id);
					r.discardCard(3, id);
				//bcaaa
				} else if (h.getCardbyIndex(3).getCardValue() == h.getCardbyIndex(4).getCardValue()) {
					r.discardCard(1, id);
					r.discardCard(0, id);
				//baaac
				} else {
					r.discardCard(4, id);
					r.discardCard(0, id);
				}
			//check if have two pair only
			} else if (h.getStrength() == Strength.TwoPair) {
				//abbcc
				if (h.getCardbyIndex(0).getCardValue() != h.getCardbyIndex(1).getCardValue()) {
					r.discardCard(0, id);
				//bbacc
				} else if (h.getCardbyIndex(2).getCardValue() != h.getCardbyIndex(3).getCardValue()) {
					r.discardCard(2, id);
				//bbcca
				} else {
					r.discardCard(4, id);
				}
			//check if have one pair only
			} else if (h.getStrength() == Strength.OnePair) {
				//aabcd
				if (h.getCardbyIndex(0).getCardValue() == h.getCardbyIndex(1).getCardValue()) {
					r.discardCard(4, id);
					r.discardCard(3, id);
					r.discardCard(2, id);
				//baacd
				} else if (h.getCardbyIndex(1).getCardValue() == h.getCardbyIndex(2).getCardValue()) {
					r.discardCard(4, id);
					r.discardCard(3, id);
					r.discardCard(0, id);
				//bcaad
				} else if (h.getCardbyIndex(2).getCardValue() == h.getCardbyIndex(3).getCardValue()) {
					r.discardCard(4, id);
					r.discardCard(1, id);
					r.discardCard(0, id);
				//bcdaa
				} else {
					r.discardCard(2, id);
					r.discardCard(1, id);
					r.discardCard(0, id);
				}
			} else if (h.getStrength().getStrengthRank() < Strength.ThreeOK.getStrengthRank()) {
				return -3;
			//high card case
			} else {
				r.discardCard(2, id);
				r.discardCard(1, id);
				r.discardCard(0, id);
			}
			r.exchangeCard(id);
		}
		//should return -3 for end turn call
		return -3;
	}
	
	//returns index of card that isn't part of the flush
	public int almostFlush(Hand hand) {
		int s = 0, h = 0, c = 0, d = 0;
		int count = 0;
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
		if (s == 4) {
			for (Card card : hand.getHand()) {
				if (card.getCardSuit() != CardSuit.Spades) {
					return count;
				} else {
					count++;
				}
			}
		} else if (h == 4) {
			for (Card card : hand.getHand()) {
				if (card.getCardSuit() != CardSuit.Hearts) {
					return count;
				} else {
					count++;
				}
			}
		} else if (c == 4) {
			for (Card card : hand.getHand()) {
				if (card.getCardSuit() != CardSuit.Clubs) {
					return count;
				} else {
					count++;
				}
			}
		} else if (d == 4) {
			for (Card card : hand.getHand()) {
				if (card.getCardSuit() != CardSuit.Diamonds) {
					return count;
				} else {
					count++;
				}
			}
		}
		return -1;
	}
	
	public int almostStraight(Hand hand) {
		if (hand.getCardbyIndex(0).getCardValue()+1 == hand.getCardbyIndex(1).getCardValue() 
				&& hand.getCardbyIndex(0).getCardValue()+2 == hand.getCardbyIndex(2).getCardValue()
				&& hand.getCardbyIndex(0).getCardValue()+3 == hand.getCardbyIndex(3).getCardValue() 
				&& !(hand.getCardbyIndex(0).getCardValue()+4 == hand.getCardbyIndex(4).getCardValue() || hand.getCardbyIndex(0).getCardValue()+12 == hand.getCardbyIndex(4).getCardValue())) {
			return 4;
		} else if (hand.getCardbyIndex(0).getCardValue()+1 == hand.getCardbyIndex(1).getCardValue() 
				&& hand.getCardbyIndex(0).getCardValue()+2 == hand.getCardbyIndex(2).getCardValue()
				&& !(hand.getCardbyIndex(0).getCardValue()+3 == hand.getCardbyIndex(3).getCardValue())
				&& (hand.getCardbyIndex(0).getCardValue()+4 == hand.getCardbyIndex(4).getCardValue() || hand.getCardbyIndex(0).getCardValue()+12 == hand.getCardbyIndex(4).getCardValue())) {
			return 3;
		} else if (hand.getCardbyIndex(0).getCardValue()+1 == hand.getCardbyIndex(1).getCardValue() 
				&& !(hand.getCardbyIndex(0).getCardValue()+2 == hand.getCardbyIndex(2).getCardValue())
				&& hand.getCardbyIndex(0).getCardValue()+3 == hand.getCardbyIndex(3).getCardValue()
				&& (hand.getCardbyIndex(0).getCardValue()+4 == hand.getCardbyIndex(4).getCardValue() || hand.getCardbyIndex(0).getCardValue()+12 == hand.getCardbyIndex(4).getCardValue())) {
			return 2;
		} else if (!(hand.getCardbyIndex(0).getCardValue()+1 == hand.getCardbyIndex(1).getCardValue())
				&& hand.getCardbyIndex(0).getCardValue()+2 == hand.getCardbyIndex(2).getCardValue()
				&& hand.getCardbyIndex(0).getCardValue()+3 == hand.getCardbyIndex(3).getCardValue()
				&& (hand.getCardbyIndex(0).getCardValue()+4 == hand.getCardbyIndex(4).getCardValue() || hand.getCardbyIndex(0).getCardValue()+12 == hand.getCardbyIndex(4).getCardValue())) {
			return 1;
		} else if (!(hand.getCardbyIndex(0).getCardValue()+1 == hand.getCardbyIndex(1).getCardValue())
				&& !(hand.getCardbyIndex(0).getCardValue()+2 == hand.getCardbyIndex(2).getCardValue())
				&& !(hand.getCardbyIndex(0).getCardValue()+3 == hand.getCardbyIndex(3).getCardValue())
				&& !(hand.getCardbyIndex(0).getCardValue()+4 == hand.getCardbyIndex(4).getCardValue() || hand.getCardbyIndex(0).getCardValue()+12 == hand.getCardbyIndex(4).getCardValue())) {
			return 0;
		}
		return -1;
	}
}
