package planner.algorithm.strips.logs;

import planner.model.State;
import planner.model.interfaces.PrintableState;

public class PrintableStripsState{
	private String label;
	
	public PrintableStripsState(State s) {
		label = s.toString();
	}

	public String getLabel() {
		return label;
	}
}
