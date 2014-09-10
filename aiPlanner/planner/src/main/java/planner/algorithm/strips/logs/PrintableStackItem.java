package planner.algorithm.strips.logs;

import planner.algorithm.strips.StackItem;

public class PrintableStackItem{
	private String label;
	private String type;
	
	public PrintableStackItem(StackItem i) {
		label = i.toString();
		type = i.isActionType() ? "action" : "state";
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}
	
	
}
