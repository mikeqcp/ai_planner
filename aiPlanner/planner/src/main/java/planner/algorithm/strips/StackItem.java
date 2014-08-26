package planner.algorithm.strips;

import planner.model.Action;
import planner.model.State;

public class StackItem {
	private Action action = null;
	private State state = null;
	
	public StackItem(Action action) {
		super();
		this.action = action;
	}

	public StackItem(State state) {
		super();
		this.state = state;
	}
	
	public boolean isAction(){
		return this.action != null;
	}
	
	public boolean isState(){
		return this.state != null;
	}
	
}
