package comp4004.poker.ai;

import comp4004.poker.*;

public interface Strategy {
	public int runAI(RulesEngine r, BoardState b, long id);
}
