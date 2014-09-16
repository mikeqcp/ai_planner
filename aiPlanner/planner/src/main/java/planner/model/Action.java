package planner.model;

import java.util.HashSet;
import java.util.Set;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import planner.algorithm.logic.TermOperations;

public class Action {	
	protected pddl4j.exp.action.Action action;
	
	public Action(ActionDef action) {
		super();
		this.action = (pddl4j.exp.action.Action)action;
	}

	public pddl4j.exp.action.Action getAction() {
		return action;
	}
	
	public BindedAction bindParameters(ParameterBinding binding){
		return new BindedAction(this, binding);
	}

	public AtomicState[] getPreconditions(){
		Set<AtomicState> pre = new HashSet<AtomicState>();
		for (Exp e : TermOperations.splitExprElements(action.getPrecondition())) {
			AtomicState state = new State(e).toAtomic();
			pre.add(state);
		}
		return pre.toArray(new AtomicState[0]);
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
			State state = new State(e);
			if (!(state.isAtomic())) continue;
			AtomicState atomic = state.toAtomic();
			
			if(atomic.isNegated()) return null;	//skip negations
			
			ParameterBinding binding = atomic.bindToSatisfy(s);
			if(binding != null) return binding;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return action.getName() + action.getParameters().toString();
	}
}
