package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.model.Action;
import planner.model.AtomicState;
import planner.model.BindedAction;
import planner.model.ParameterBinding;

/**
 * @author MichalP
 *	Represents graph node - action to be performed in final plan
 *	Contains its preconditions and effects
 */
public class GraphNode {
	private Action action;
	private ParameterBinding binding;

	protected GraphNode() {
	}

	public GraphNode(Action action) {
		this.action = action;
	}
	
	public GraphNode(GraphNode other) {
		this.action = other.action;
		this.binding = new ParameterBinding(other.binding);
	}

	public BindedAction getBindedAction(){
		return new BindedAction(action, binding);
	}
	
	
	protected AtomicState[] getAtomicPreconditions() {
		BindedAction binded = getBindedAction();
		return binded.getBindedPreconditions();
	}
	
	public Set<SubGoal> getPreconditions() {
		AtomicState[] atomic = getAtomicPreconditions();
		Set<SubGoal> preconds = new HashSet<SubGoal>();
		for (AtomicState a : atomic) {
			preconds.add(new SubGoal(this, a));
		}
		return preconds;
	}
}
