package planner.algorithm.strips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Term;
import planner.algorithm.logic.TermOperations;
import planner.model.Action;
import planner.model.State;

public class BindedAction extends Action {
	private Random rand = new Random();
	private ParameterBinding binding;
	
	
	public BindedAction(Action parent, ParameterBinding binding) {
		super(parent.getAction());
		this.binding = binding;
	}

	/**
	 * @param initial state
	 * @return state after applying action (new object is created, old one is unchanged)
	 */
	public State applyTo(State srcState){
		State resultState = new State(srcState);

		AtomicState[] effects = getBindedEffects();
		for (AtomicState effect : effects) {
			if(!effect.isNegated())
				resultState = resultState.addTerm(effect);
			else
				resultState = resultState.removeTerm(effect);
		}
		
		return resultState;	//TODO
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
	
	public AtomicState[] getBindedPreconditions(){
		Exp preconditions = action.getPrecondition();

		Exp[] preStates;
		if (preconditions instanceof AndExp)
			preStates = TermOperations.splitExprElements((AndExp) preconditions);
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
	
	public void fillFreeParameters(Set<Constant> constants) {
		for (Term param : this.action.getParameters()) {
			if(!binding.containsTerm(param)){
				String randVal = randConstant(constants, binding);
				binding.addBinding(param, randVal);
			}
		};
	}
	
	/**
	 * @param constants - values to choose from
	 * @param existingValues - values to omit
	 * @return
	 */
	private String randConstant(Set<Constant> constants, ParameterBinding existingValues){
		Constant[] cArray = constants.toArray(new Constant[0]);
		int randIndex = rand.nextInt(cArray.length);

		int iterationLimit = cArray.length + 1;
		int i = 0;
		String val = cArray[randIndex].getImage();
		while(existingValues.containsValue(val) && i < iterationLimit){
			val = cArray[++randIndex % cArray.length].getImage();
			i++;
		}
		
		if(i >= iterationLimit) return null;
		return val;
	}
	
	@Override
	public String toString() {
		Collection<String> vals = new ArrayList<String>();
		
		for (Term t : action.getParameters()) {
			vals.add(binding.getBindingFor(t));
		}
		String valuesStr = Arrays.stream(vals.toArray(new String[0])).collect(Collectors.joining(","));
		
		return action.getName() + "(" + valuesStr + ")";
	}
}