package planner.algorithm.pop.logs;

import planner.algorithm.pop.model.GraphNode;

public class PrintableNode {
	private long id;
	private String label;
	
	public PrintableNode(GraphNode n) {
		this.id = n.getId();
		this.label = n.toString();
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
}
