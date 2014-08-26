package planner.algorithm.strips;

import pddl4j.exp.action.ActionDef;
import planner.model.Action;

public class StripsAction implements Action {	
	private pddl4j.exp.action.Action action;
	
	public StripsAction(ActionDef action) {
		super();
		
		this.action = (pddl4j.exp.action.Action)action;
	}

	public String getName() {
		return "test_action";
	}

	public pddl4j.exp.action.Action getAction() {
		return action;
	}
}
