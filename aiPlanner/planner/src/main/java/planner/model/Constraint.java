package planner.model;

import pddl4j.exp.action.Action;

public class Constraint {
	private StripsState state;
	/**
	 * Extracts constraint from fake action
	 * @param actionInst
	 */
	public Constraint(Action action) {
		state = new StripsState(action.getEffect());
	}
	
	public Constraint(StripsState s){
		state = s;
	}

	public StripsState getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
}
