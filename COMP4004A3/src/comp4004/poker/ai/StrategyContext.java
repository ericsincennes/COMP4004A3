package comp4004.poker.ai;

import comp4004.poker.*;

public class StrategyContext {
	private Strategy strategy;
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public int executeStrategy(RulesEngine r, BoardState b, long id) {
		return strategy.runAI(r, b, id);
	}
}
