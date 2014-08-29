package planner.algorithm.strips;

import java.util.Set;

import pddl4j.exp.AndExp;
import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import pddl4j.exp.NotAtomicFormula;
import pddl4j.exp.action.ActionDef;
import pddl4j.exp.term.Term;
import planner.algorithm.logic.TermOperations;
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
}
