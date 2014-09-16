package planner.algorithm.pop.model;

import planner.model.AtomicState;

public class SubGoal {
	private GraphNode node;
	private AtomicState precondition;
	
	public SubGoal(GraphNode node, AtomicState precondition) {
		super();
		this.node = node;
		this.precondition = precondition;
	}

	public GraphNode getNode() {
		return node;
	}

	public AtomicState getPrecondition() {
		return precondition;
	}
	
	
}
