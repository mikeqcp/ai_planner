package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.model.AtomicState;
import planner.model.State;

public class EndNode extends GraphNode {
	private AtomicState[] goals;
	
	
	public EndNode(State goal) {
		goals = goal.breakIntoAtomic();
	}

	@Override
	public Set<SubGoal> getPreconditions() {
		Set<SubGoal> preconds = new HashSet<SubGoal>();
		for (AtomicState g : goals) {
			preconds.add(new SubGoal(this, g));
		}
		return preconds;
	}
	
}
