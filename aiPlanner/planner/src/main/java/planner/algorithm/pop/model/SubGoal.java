package planner.algorithm.pop.model;

import planner.model.AtomicState;

public class SubGoal {
	private GraphNode node;
	private AtomicState goal;
	
	public SubGoal(GraphNode node, AtomicState precondition) {
		super();
		this.node = node;
		this.goal = precondition;
	}

	public GraphNode getNode() {
		return node;
	}

	public AtomicState getGoal() {
		return goal;
	}
	
	@Override
	public String toString() {
		return goal.toString();
	}
}
