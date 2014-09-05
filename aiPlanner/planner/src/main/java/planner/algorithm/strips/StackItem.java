package planner.algorithm.strips;

import planner.model.StripsState;


public class StackItem {
	private BindedStripsAction action = null;
	private StripsState state = null;
	
	public StackItem(BindedStripsAction action) {
		super();
		this.action = action;
	}

	public StackItem(StripsState state) {
		super();
		this.state = state;
	}
	
	public boolean isActionType(){
		return this.action != null;
	}
	
	public boolean isStateType(){
		return this.state != null;
	}

	public BindedStripsAction getAction() {
		return action;
	}

	public StripsState getState() {
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
