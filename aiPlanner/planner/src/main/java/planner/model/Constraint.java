package planner.model;

import pddl4j.exp.action.Action;

public class Constraint {
	private State state;
	/**
	 * Extracts constraint from fake action
	 * @param actionInst
	 */
	public Constraint(Action action) {
		state = new State(action.getEffect());
	}
	
	public Constraint(State s){
		state = s;
	}

	public State getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
}
