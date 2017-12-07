package comp4004.poker.ai;

public class Context {
	private Strategy strategy;
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public void execute() {
		strategy.method();
	}
}
