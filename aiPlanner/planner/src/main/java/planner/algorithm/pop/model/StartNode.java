package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.model.AtomicState;
import planner.model.State;

public class StartNode extends GraphNode {
	private State initState;
	private AtomicState[] initStates;
	
	public StartNode(State init) {
		initState = init;
		initStates = init.breakIntoAtomic();
		
	}
	
	@Override
	public Set<SubGoal> getPreconditions() {
		return new HashSet<SubGoal>();
	}
	
	@Override
	public String toString() {
		return "START";
	}
	
	@Override
	public GraphNode clone() {
		StartNode clone = new StartNode(initState);
		clone.id = id;
		return clone;
	}
}
