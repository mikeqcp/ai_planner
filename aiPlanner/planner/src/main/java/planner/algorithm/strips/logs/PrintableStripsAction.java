package planner.algorithm.strips.logs;

import planner.model.Action;

public class PrintableStripsAction{
	private String label;
	
	public PrintableStripsAction(Action a) {
		label = a.toString();
	}

	public String getLabel() {
		return label;
	}
}
