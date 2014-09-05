package planner.model;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.strips.AtomicState;
import planner.algorithm.strips.BindedStripsAction;
import planner.algorithm.strips.ParameterBinding;
import planner.model.Action;

public class Action {	
	protected pddl4j.exp.action.Action action;
	
	public Action(ActionDef action) {
		super();
		this.action = (pddl4j.exp.action.Action)action;
	}

	public pddl4j.exp.action.Action getAction() {
		return action;
	}
	
	public BindedStripsAction bindParameters(ParameterBinding binding){
		return new BindedStripsAction(this, binding);
	}

	/**
	 * @param s - target state
	 * @return Parameters that allow achieve target state, null if there are none
	 */
	public ParameterBinding bindToProduce(AtomicState s){		
		Exp effects = action.getEffect();
		
		Exp[] allEffects;
		if(effects instanceof AndExp)
			allEffects = TermOperations.splitExprElements((AndExp)effects);
		else
			allEffects = new Exp[] { effects };	//single effect
		
		for (Exp e : allEffects) {
			StripsState state = new StripsState(e);
			if (!(state.isAtomic())) continue; 	//dont check negation -> it mean that action removes that term
			AtomicState atomic = state.toAtomic();
			
			if(atomic.isNegated()) return null;	//skip negations
			
			ParameterBinding binding = atomic.bindToSatisfy(s);
			if(binding != null) return binding;
		}
		return null;
	}

	@Override
	public String toString() {
		return action.toString();
	}
}
