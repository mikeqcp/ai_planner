package planner.algorithm.regression;

import pddl4j.exp.Exp;
import planner.model.StripsState;

public class RegState extends StripsState {
	public RegState(StripsState s) {
		super(s);
	}
	
	public RegState(Exp state) {
		super(state);
	}
	
	public RegState(Exp... states){
		super(states);
	}
	
	public boolean isConsistent(){
		return true;
	}
}
