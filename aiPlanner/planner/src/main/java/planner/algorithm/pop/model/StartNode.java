package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.model.AtomicState;
import planner.model.State;

public class StartNode extends GraphNode {
	private AtomicState[] initStates;
	
	public StartNode(State init) {
		initStates = init.breakIntoAtomic();
	}
	
	@Override
	public Set<SubGoal> getPreconditions() {
		return new HashSet<SubGoal>();
	}
}
