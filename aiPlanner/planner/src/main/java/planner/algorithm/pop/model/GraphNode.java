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
	private static long IdRandomizer = 0;
	protected Action action;
	protected ParameterBinding binding;
	protected long id = IdRandomizer++;

	protected GraphNode() {
	}

	public GraphNode(Action action) {
		this.action = action;
	}
	
	public GraphNode(BindedAction action) {
		this.action = action.unbind();
		this.binding = action.getBinding();
		
	}
	
	public GraphNode(GraphNode other) {
		this.action = other.action;
		this.id = other.id;
		if(action != null)
			this.binding = new ParameterBinding(other.binding);
		else
			this.binding = new ParameterBinding();
	}

	public BindedAction getBindedAction(){
		if(action == null) return null;
		return new BindedAction(action, binding);
	}
	
	
	public long getId() {
		return id;
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
	
	@Override
	public String toString() {
		if(binding != null)
			return getBindedAction().toString();
		
		return action.toString();
	}
	
	public GraphNode clone(){
		return new GraphNode(this);
	}
}
