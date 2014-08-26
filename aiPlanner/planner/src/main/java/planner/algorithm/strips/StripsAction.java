package planner.algorithm.strips;

import java.util.Set;

import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import pddl4j.exp.term.Variable;
import planner.model.Action;

public class StripsAction implements Action {	
	protected pddl4j.exp.action.Action action;
	
	public StripsAction(ActionDef action) {
		super();
		this.action = (pddl4j.exp.action.Action)action;
	}

	public pddl4j.exp.action.Action getAction() {
		return action;
	}
	
	public boolean canProduce(StripsState s){
		//TODO: uwzgledniæ paremetry
		
//		Exp effects = action.getEffect();
//		return s.satisfies(new StripsState(effects));
		return true;
	}
}
