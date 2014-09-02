package planner.algorithm.strips.logs;

import planner.algorithm.strips.StackItem;

public class PrintableStackItem{
	private String label;
	
	public PrintableStackItem(StackItem i) {
		label = i.toString();
	}

	public String getLabel() {
		return label;
	}
}
