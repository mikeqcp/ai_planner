package planner.algorithm.strips;

import planner.model.Action;

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
	
	public boolean isAction(){
		return this.action != null;
	}
	
	public boolean isState(){
		return this.state != null;
	}

	public BindedStripsAction getAction() {
		return action;
	}

	public StripsState getState() {
		return state;
	}
	
	
}
