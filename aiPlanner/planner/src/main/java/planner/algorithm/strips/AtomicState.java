package planner.algorithm.strips;

import java.util.ArrayList;
import java.util.Iterator;

import pddl4j.exp.AndExp;
import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import pddl4j.exp.NotAtomicFormula;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Substitution;
import pddl4j.exp.term.Term;
import pddl4j.exp.term.Variable;
import planner.algorithm.logic.TermOperations;
import planner.model.StripsState;

public class AtomicState extends StripsState {
	private AtomicFormula state;
	private boolean negation = false;

	public AtomicState(AtomicFormula state) {
		this.state = state;
	}
	
	public AtomicState(NotAtomicFormula state) {
		this.state = state.getExp();
		this.negation = true;
	}
	
	public AtomicFormula getFormula(){
		return state;
	}
	
	public AtomicState(StripsState state) {
		if(state instanceof AtomicState)
			this.state = ((AtomicState)state).state;
		
		if(state.isAtomic()){
			if(state.getState() instanceof AtomicFormula)
				this.state = (AtomicFormula)state.getState();
			if(state.getState() instanceof NotAtomicFormula){
				this.state = ((NotAtomicFormula)state.getState()).getExp();
				this.negation = true;
			}
		}
			
		//othrwise left null
	}
	
	public boolean hasUnbindedParams(){
		return state.getFreeVariables().size() > 0;
	}
	
	public boolean predicateEquals(AtomicState formula){
		return state.getPredicate().equals(formula.state.getPredicate());
	}
	
	public ArrayList<Term> getArguments(){
		ArrayList<Term> args = new ArrayList<Term>();
		Iterator<Term> i = this.state.iterator();
		while(i.hasNext()){
			args.add(i.next());
		}
		return args;
	}
	
	public ParameterBinding bindToSatisfy(AtomicState s){
		ParameterBinding binding = new ParameterBinding();
		
		if(s.satisfies(this)) return binding;
		
		if (s.hasUnbindedParams()) return null;	//cannot bind
		if(!s.predicateEquals(this)) return null;
		
		ArrayList<Term> thisArgs = getArguments();
		ArrayList<Term> otherArgs = s.getArguments();
		
		for (int i = 0; i < thisArgs.size(); i++) {
			Term argATerm = thisArgs.get(i);
			String argAStr = thisArgs.get(i).getImage();
			String argBStr = otherArgs.get(i).getImage();
			
			if(argAStr.charAt(0) == '?'){
				binding.addBinding(argATerm, argBStr);
			} else {
				binding.addBinding(argATerm, argAStr);
			}
		}
		return binding.isEmpty() ? null : binding;
	}
	
	public boolean isNegated(){
		return this.negation;
	}

	public AtomicState bind(ParameterBinding binding){		
		Iterator<Term> i = this.state.iterator();
		
		Substitution subst = new Substitution();
		while(i.hasNext()){
			Term arg = i.next();
			if(arg instanceof Variable){
				String bindedValue = null;
				if((bindedValue = binding.getBindingFor(arg)) != null){
					Constant val = new Constant(bindedValue);
					subst.bind((Variable) arg, val);
				}
			}
		}
		this.state = this.state.apply(subst);
		return this;
	}

	public Exp[] getTerms(){
		return new Exp[] {state};	//single state
	}
	
	@Override
	public boolean isAtomic() {return true;};
	
	@Override
	public String toString() {
		return this.state.toString();
	}
}
