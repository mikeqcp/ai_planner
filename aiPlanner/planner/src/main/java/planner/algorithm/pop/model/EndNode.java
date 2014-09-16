package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.model.AtomicState;
import planner.model.State;

public class EndNode extends GraphNode {
	private State goalState;
	private AtomicState[] goals;
	
	
	public EndNode(State goal) {
		goalState = goal;
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
	
	@Override
	public String toString() {
		return "END";
	}
	
	@Override
	public GraphNode clone() {
		EndNode clone = new EndNode(goalState);
		clone.id = id;
		return clone;
	}
}
