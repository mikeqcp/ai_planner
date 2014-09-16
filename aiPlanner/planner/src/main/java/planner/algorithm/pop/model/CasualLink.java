package planner.algorithm.pop.model;

import planner.model.AtomicState;

public class CasualLink extends GraphLink {
	private AtomicState achieves;
	

	public CasualLink(GraphNode nodeFrom, GraphNode nodeTo, AtomicState achieves) {
		super(nodeFrom, nodeTo);
		this.achieves = achieves;
	}

	@Override
	public GraphLink clone() {
		return new CasualLink(this.nodeFrom, this.nodeTo, this.achieves);
	}
}
