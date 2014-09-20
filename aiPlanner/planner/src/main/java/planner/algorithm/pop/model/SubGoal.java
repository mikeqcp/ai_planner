package planner.algorithm.pop.model;

import java.util.ArrayList;

import pddl4j.exp.term.Term;
import planner.model.AtomicState;
import planner.model.ParameterBinding;

public class SubGoal {
	private GraphNode node;
	private AtomicState originalGoal;
	private AtomicState goal;
	
	public SubGoal(GraphNode node, AtomicState precondition) {
		super();
		this.node = new GraphNode(node);
		this.goal = precondition;
		this.originalGoal = precondition;
	}
	
	public GraphNode getNode() {
		return node;
	}
	
	public AtomicState getGoal() {
		return goal;
	}
	
	public void updateBinding(ParameterBinding b){
		this.goal = originalGoal.bind(b);
	}
	
	@Override
	public String toString() {
		return goal.toString();
	}
}
