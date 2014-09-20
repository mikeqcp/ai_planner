package planner.algorithm.strips.logs;

import java.util.Arrays;

import planner.model.State;

public class PrintableStripsState{
	private String label;
	
	public PrintableStripsState(State s) {
		label = (Arrays.asList(s.breakIntoAtomic())).toString();
	}

	public String getLabel() {
		return label;
	}
}
