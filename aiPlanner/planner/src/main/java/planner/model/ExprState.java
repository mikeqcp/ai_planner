package planner.model;

import pddl4j.exp.Exp;

public class ExprState implements State {	
	private Exp state;

	public String getName() {
		return "test_state";
	}

	public ExprState(Exp state) {
		this.state = state;
	}

	public Exp getState() {
		return state;
	}
}
