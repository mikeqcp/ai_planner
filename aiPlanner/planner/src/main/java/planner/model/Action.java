package planner.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import pddl4j.exp.term.Term;
import planner.algorithm.logic.TermOperations;

public class Action {
	protected pddl4j.exp.action.Action action;

	public Action(ActionDef action) {
		super();
		this.action = (pddl4j.exp.action.Action) action;
	}

	public pddl4j.exp.action.Action getAction() {
		return action;
	}

	public ArrayList<ArrayList<Term>> getEffectParams(String effectPredicate){
		ArrayList<ArrayList<Term>> effectsList = new ArrayList<ArrayList<Term>>();
		Exp[] effs = TermOperations.splitExprElements(action.getEffect());
		for (Exp e : effs) {
			AtomicState s = new State(e).toAtomic();
			if(s.getPredicate().equals(effectPredicate)) effectsList.add(s.getArguments());
		}
		return effectsList;
	}
	
	public BindedAction bindParameters(ParameterBinding binding) {
		return new BindedAction(this, binding);
	}

	public AtomicState[] getPreconditions() {
		Set<AtomicState> pre = new HashSet<AtomicState>();
		for (Exp e : TermOperations.splitExprElements(action.getPrecondition())) {
			AtomicState state = new State(e).toAtomic();
			pre.add(state);
		}
		return pre.toArray(new AtomicState[0]);
	}

	/**
	 * @param s
	 *            - target state
	 * @return Parameters that allow achieve target state, null if there are
	 *         none
	 */
	public ParameterBinding bindToProduce(AtomicState s) {
		Exp effects = action.getEffect();

		Exp[] allEffects;
		if (effects instanceof AndExp)
			allEffects = TermOperations.splitExprElements((AndExp) effects);
		else
			allEffects = new Exp[] { effects }; // single effect

		for (Exp e : allEffects) {
			State state = new State(e);
			if (!(state.isAtomic()))
				continue;
			AtomicState atomic = state.toAtomic();

			if (atomic.isNegated())
				return null; // skip negations

			ParameterBinding binding = atomic.bindToSatisfy(s);
			if (binding != null)
				return binding;
		}
		return null;
	}
	
//	public ParameterBinding[] bindAllToProduce(AtomicState s){
//		List<ParameterBinding> bindings = new ArrayList<ParameterBinding>();
//		Exp effects = action.getEffect();
//
//		Exp[] allEffects;
//		if (effects instanceof AndExp)
//			allEffects = TermOperations.splitExprElements((AndExp) effects);
//		else
//			allEffects = new Exp[] { effects }; // single effect
//
//		for (Exp e : allEffects) {
//			State state = new State(e);
//			if (!(state.isAtomic()))
//				continue;
//			AtomicState atomic = state.toAtomic();
//
//			if (atomic.isNegated())
//				return null; // skip negations
//
//			ParameterBinding applicable = atomic.bindToSatisfy(s);
//			for (ParameterBinding b : applicable) {
//				bindings.add(b);
//			}
//		}
//		if(bindings.size() == 0)
//			return null;
//		return bindings.toArray(new ParameterBinding[0]);
//	}

	@Override
	public String toString() {
		return action.getName() + action.getParameters().toString();
	}
}
