package planner.algorithm.pop.model;

import planner.model.AtomicState;

public class CasualLink extends GraphLink {
	private AtomicState achieves;
	

	public CasualLink(GraphNode nodeFrom, GraphNode nodeTo, AtomicState achieves) {
		super(nodeFrom, nodeTo);
		this.achieves = achieves;
	}

	
	
	public AtomicState getAchieves() {
		return achieves;
	}

	@Override
	public String toString() {
		return nodeFrom.toString() + " -- " + achieves.toString() + " --> " + nodeTo.toString();
	}

	@Override
	public GraphLink clone() {
		return new CasualLink(this.nodeFrom, this.nodeTo, this.achieves);
	}



	@Override
	public boolean isRedundantFor(GraphLink[] existing) {
		for (GraphLink l : existing) {
			if(l instanceof CasualLink){
				CasualLink casual = (CasualLink)l;
				if(casual.achieves.equals(this.achieves)) return true;
			}
		}
		return false;
	}
}
