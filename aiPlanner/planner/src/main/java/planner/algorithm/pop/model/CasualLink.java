package planner.algorithm.pop.model;

import planner.model.AtomicState;

public class CasualLink extends GraphLink {
	private SubGoal subgoal;
	

	public CasualLink(GraphNode nodeFrom, GraphNode nodeTo, SubGoal subgoal) {
		super(nodeFrom, nodeTo);
		this.subgoal = subgoal;
	}

	
	public SubGoal getSubgoal() {
		return subgoal;
	}


	public AtomicState getAchieves() {
		return subgoal.getGoal();
	}

	@Override
	public String toString() {
		return nodeFrom.toString() + " -- " + getAchieves().toString() + " --> " + nodeTo.toString();
	}

	@Override
	public GraphLink clone() {
		return new CasualLink(this.nodeFrom, this.nodeTo, this.subgoal);
	}



	@Override
	public boolean isRedundantFor(GraphLink[] existing) {
		for (GraphLink l : existing) {
			if(l instanceof CasualLink){
				CasualLink casual = (CasualLink)l;
				if(casual.getAchieves().equals(this.getAchieves())) return true;
			}
		}
		return false;
	}
}
