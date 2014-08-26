package planner.algorithm.strips;

import planner.model.Action;
import planner.model.ExprState;

public class StackItem {
	private Action action = null;
	private ExprState state = null;
	
	public StackItem(Action action) {
		super();
		this.action = action;
	}

	public StackItem(ExprState state) {
		super();
		this.state = state;
	}
	
	public boolean isAction(){
		return this.action != null;
	}
	
	public boolean isState(){
		return this.state != null;
	}

	public Action getAction() {
		return action;
	}

	public ExprState getState() {
		return state;
	}
	
	
}
