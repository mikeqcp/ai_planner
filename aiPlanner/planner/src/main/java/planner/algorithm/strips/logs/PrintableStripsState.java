package planner.algorithm.strips.logs;

import planner.model.StripsState;
import planner.model.interfaces.PrintableState;

public class PrintableStripsState implements PrintableState{
	private String label;
	
	public PrintableStripsState(StripsState s) {
		label = s.toString();
	}

	public String getLabel() {
		return label;
	}
}
