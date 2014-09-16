package planner.algorithm.strips.logs;

import planner.model.State;

public class PrintableStripsState{
	private String label;
	
	public PrintableStripsState(State s) {
		label = s.toString();
	}

	public String getLabel() {
		return label;
	}
}
