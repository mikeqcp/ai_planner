package planner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.research.ws.wadl.Param;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Term;
import planner.algorithm.logic.TermOperations;

public class BindedAction extends Action {
	private Random rand = new Random();
	private ParameterBinding binding;

	public BindedAction(Action parent, ParameterBinding binding) {
		super(parent.getAction());
		this.binding = binding;
	}
	
	public BindedAction(BindedAction other) {
		super(other.getAction());
		this.binding = new ParameterBinding(other.binding);
	}

	public Action unbind() {
		return new Action(this.action);
	}

	/**
	 * @param initial
	 *            state
	 * @return state after applying action (new object is created, old one is
	 *         unchanged)
	 */
	public State applyTo(State srcState) {
		State resultState = new State(srcState);

		AtomicState[] effects = getBindedEffects();
		for (AtomicState effect : effects) {
			if (!effect.isNegated())
				resultState = resultState.addTerm(effect);
			else
				resultState = resultState.removeTerm(effect);
		}

		return resultState; // TODO
	}

	public AtomicState[] getBindedEffects() {
		Exp effects = action.getEffect();

		Exp[] postStates;
		if (effects instanceof AndExp)
			postStates = TermOperations.splitExprElements((AndExp) effects);
		else
			postStates = new Exp[] { effects };

		List<AtomicState> bindedStates = new ArrayList<AtomicState>();
		for (Exp ex : postStates) {
			State exState = new State(ex);
			AtomicState atomic = new AtomicState(exState);

			AtomicState binded = atomic.bind(this.binding);
			bindedStates.add(binded);
		}

		return bindedStates.toArray(new AtomicState[0]);
	}

	public AtomicState[] getBindedPreconditions() {
		Exp preconditions = action.getPrecondition();

		Exp[] preStates;
		if (preconditions instanceof AndExp)
			preStates = TermOperations
					.splitExprElements((AndExp) preconditions);
		else
			preStates = new Exp[] { preconditions };

		List<AtomicState> bindedStates = new ArrayList<AtomicState>();
		for (Exp ex : preStates) {
			State exState = new State(ex);
			AtomicState atomic = new AtomicState(exState);

			AtomicState binded = atomic.bind(this.binding);
			bindedStates.add(binded);
		}

		return bindedStates.toArray(new AtomicState[0]);
	}

	/**
	 * @return True if this instance removes passed state
	 */
	public boolean removes(AtomicState s) {
		if (s.hasUnbindedParams())
			return false; // wait until parameters are binded to decide
		for (AtomicState e : getBindedEffects()) {
			if (e.hasUnbindedParams())
				continue;
			if (e.equals(s) && e.isNegated())
				return true;
		}
		return false;
	}

	/**
	 * @return True if this action can remove passed state with any possible
	 *         binding
	 */
	public boolean canRemove(AtomicState s) {
		for (AtomicState e : getBindedEffects()) {
			if (e.predicateEquals(s) && e.isNegated())
				return true;
		}
		return false;
	}

    public void fillFreeParameters(Set<Constant> constants) {
    	for (Term param : this.action.getParameters()) {
			if (!binding.containsTerm(param)) {
				String randVal = randConstant(constants, binding);
				binding.addBinding(param, randVal);
			}
		}
    }
	
	public List<BindedAction> getPossibleParameterFillings(Set<Constant> constants) {
		List<BindedAction> queue = new ArrayList<BindedAction>();
		List<BindedAction> filled = new ArrayList<BindedAction>();
		
		queue.add(this);
		
		while(queue.size() > 0){
			BindedAction current = queue.remove(0);
			
			if(!current.hasUnbindedParams()){
				filled.add(current);
				continue;
			}
			
			for (Term param : current.action.getParameters()) {
				if (!current.binding.containsTerm(param)) {
					List<String> allVals = otherConstants(constants, binding);
					for (String v : allVals) {
						BindedAction ba = new BindedAction(current);
						ba.binding.addBinding(param, v);
						queue.add(ba);
					}
//					binding.addBinding(param, allVals);
					break;	//get first unbinded
				}
			}
		}
		
//		for (Term param : this.action.getParameters()) {
//			if (!binding.containsTerm(param)) {
//				String randVal = randConstant(constants, binding);
//				binding.addBinding(param, randVal);
//			}
//		}
		
		return filled;
	}
	
	public boolean hasUnbindedParams(){
		for (Term param : this.action.getParameters()) {
			if (!binding.containsTerm(param)) return true;
		}
		return false;
	}

	/**
	 * @param constants
	 *            - values to choose from
	 * @param existingValues
	 *            - values to omit
	 * @return
	 */
	private String randConstant(Set<Constant> constants,
			ParameterBinding existingValues) {
		Constant[] cArray = constants.toArray(new Constant[0]);
		int randIndex = rand.nextInt(cArray.length);

		int iterationLimit = cArray.length + 1;
		int i = 0;
		String val = cArray[randIndex].getImage();
		while (existingValues.containsValue(val) && i < iterationLimit) {
			val = cArray[++randIndex % cArray.length].getImage();
			i++;
		}

		if (i >= iterationLimit)
			return null;
		return val;
	}
	
	private List<String> otherConstants(Set<Constant> constants,
			ParameterBinding existingValues) {
		List<String> possible = new ArrayList<String>();
		Constant[] cArray = constants.toArray(new Constant[0]);
		
		for (Constant constant : cArray) {
			String val = constant.getImage();
			if(!existingValues.containsValue(val))
				possible.add(val);
		}

		return possible;
	}

	public ParameterBinding getBinding() {
		return binding;
	}

	@Override
	public ParameterBinding bindToProduce(AtomicState s) {
		ParameterBinding binding = super.bindToProduce(s);
		if (binding != null && binding.isExtending(this.binding)
				&& binding.validate())
			return binding;
		return null;
	}

	@Override
	public String toString() {
		if (binding == null)
			return (new Action(action)).toString();
		Collection<String> vals = new ArrayList<String>();

		for (Term t : action.getParameters()) {
			String bindVal = binding.getBindingFor(t);
			bindVal = bindVal != null ? bindVal : "?";
			vals.add(bindVal);
		}
		String valuesStr = Arrays.stream(vals.toArray(new String[0])).collect(
				Collectors.joining(","));

		return action.getName() + "(" + valuesStr + ")";
	}
}
