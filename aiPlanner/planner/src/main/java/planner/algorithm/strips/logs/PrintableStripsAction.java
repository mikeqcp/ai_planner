package planner.algorithm.strips.logs;

import planner.algorithm.strips.StripsAction;

public class PrintableStripsAction{
	private String label;
	
	public PrintableStripsAction(StripsAction a) {
		label = a.toString();
	}

	public String getLabel() {
		return label;
	}
}
