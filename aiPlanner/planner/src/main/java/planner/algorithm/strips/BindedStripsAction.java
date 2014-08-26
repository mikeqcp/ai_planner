package planner.algorithm.strips;

import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;

public class BindedStripsAction extends StripsAction {
	//parameters list
	//binded values
	
	
	public BindedStripsAction(StripsAction parent, ActionBinding binding) {
		super(parent.action);
		//TODO:all
	}

	/**
	 * @param initial state
	 * @return state after applying action (new object is created, old one is unchanged)
	 */
	public StripsState applyTo(StripsState s){
		Exp pre = action.getPrecondition();
		Exp post = action.getEffect();
		
		
		return s;	//TODO
	}
}
