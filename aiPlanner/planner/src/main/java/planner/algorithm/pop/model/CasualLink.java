package planner.algorithm.pop.model;

import planner.model.AtomicState;

public class CasualLink extends GraphLink {
	private AtomicState achieves;

	@Override
	public GraphLink clone() {
		CasualLink link = new CasualLink();
		
		link.setNodeFrom(this.nodeFrom);
		link.setNodeTo(this.nodeTo);
		link.achieves = this.achieves;
		
		return link;
	}
}
