package planner.algorithm.strips.model;

import planner.model.BindedAction;
import planner.model.State;


public class StackItem {
	private BindedAction action = null;
	private State state = null;
	
	public StackItem(BindedAction action) {
		super();
		this.action = action;
	}

	public StackItem(State state) {
		super();
		this.state = state;
	}
	
	public boolean isActionType(){
		return this.action != null;
	}
	
	public boolean isStateType(){
		return this.state != null;
	}

	public BindedAction getAction() {
		return action;
	}

	public State getState() {
		return state;
	}
	
	@Override
	public String toString() {
		if(isActionType())
			return action.toString();
		if(isStateType())
			return state.toString();
		return "{}";
	}
}
