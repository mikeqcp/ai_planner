package planner.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pddl4j.exp.Exp;
import pddl4j.exp.NotExp;
import pddl4j.exp.OrExp;
import pddl4j.exp.action.Action;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Term;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.strips.AtomicState;
import planner.algorithm.strips.ParameterBinding;

/**
 * @author MichalP
 *	Constraints are limited to 'forall' type
 */
public class Constraint {
	private State state;
	private Set<Term> params;
	
	/**
	 * Extracts constraint from fake action
	 * @param actionInst
	 */
	public Constraint(Action action) {
		params = action.getParameters();
		state = new State(action.getPrecondition());
	}
	
	public Constraint(State s){
		state = s;
	}

	public State getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
	
	public boolean isSatisfiedIn(State s, Set<Constant> paramValues){
		Collection<List<String>> possibleBindings = generatePossibleParamValues(paramValues, this.params.size());
		State normalForm = state.getConjunctiveNormalForm();
		Exp[] andElems = normalForm.getTerms();
		Collection<AtomicState[]> allExpressionsToSatisfy = new HashSet<AtomicState[]>();
		ParameterBinding binding;
		
		//generate all or-expression to satisfy
		for (List<String> bindingVals : possibleBindings) {
			//evaluate one of possible params binding (ex. x = 'first' y='second')
			binding = generateBinding(bindingVals);
			for (Exp orExp : andElems) {
				Exp[] splitted = TermOperations.splitExprElements(orExp);
				Collection<AtomicState> exprAtomics = new HashSet<AtomicState>();
				for (Exp atomicExp : splitted) {	
					State state = new State(atomicExp);
					AtomicState atomic = new AtomicState(state);
					exprAtomics.add(atomic.bind(binding));
				}
				allExpressionsToSatisfy.add(exprAtomics.toArray(new AtomicState[0]));
			}
		}

		for (AtomicState[] orExp : allExpressionsToSatisfy) {
			boolean evaluated = s.evaluateOrExpression(orExp);
			if(!evaluated) {
//				s.evaluateOrExpression(orExp);
				return false;
			}
		}
		
		return true;
	}

	private ParameterBinding generateBinding(List<String> bindingVals) {
		ParameterBinding binding = new ParameterBinding();
		int i = 0;
		for (Term t : this.params) {
			binding.addBinding(t, bindingVals.get(i++));
		}
		return binding;
	}
	
	
	
	private Collection<List<String>> generatePossibleParamValues(Collection<List<String>> prevPerms, Set<Constant> params, int outputLength, int fillIndex){
		Collection<List<String>> perms = new HashSet<List<String>>();
		
		for (List<String> perm : prevPerms) {
			for (Constant param : params) {
				String cVal = param.getImage();
				if(!perm.contains(cVal)){
					List<String> newPerm = new ArrayList<String>(perm);
					newPerm.add(cVal);
					perms.add(newPerm);
				}
			}
		}
		
		if(fillIndex == outputLength -1)
			return perms;
		else
			return generatePossibleParamValues(perms, params, outputLength, fillIndex+1);
	}
	
	private Collection<List<String>> generatePossibleParamValues(Set<Constant> params, int outputLength){
		Collection<List<String>> perms = new HashSet<List<String>>();
		
		perms.add(new ArrayList<String>(outputLength));
		
		return generatePossibleParamValues(perms, params, outputLength, 0);
	}
	
	private AtomicState[] bindParams(ParameterBinding bind){
		Exp[] states = state.getTerms();

		List<AtomicState> bindedStates = new ArrayList<AtomicState>();
		for (Exp ex : states) {
			State exState = new State(ex);
			AtomicState atomic = new AtomicState(exState);
			
			AtomicState binded = atomic.bind(bind);
			bindedStates.add(binded);
		}
		
		return bindedStates.toArray(new AtomicState[0]);
	}
}
