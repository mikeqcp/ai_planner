package planner.model;

import java.util.ArrayList;
import java.util.Iterator;

import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import pddl4j.exp.NotAtomicFormula;
import pddl4j.exp.NotExp;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Substitution;
import pddl4j.exp.term.Term;
import pddl4j.exp.term.Variable;

public class AtomicState extends State {
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
	
	public String getPredicate(){
		return state.getPredicate();
	}
	
	public AtomicState(State state) {
		if(state instanceof AtomicState){
			this.state = ((AtomicState)state).state;
			this.negation = ((AtomicState)state).negation;
		}
		
		if(state.isAtomic()){
			if(state.getState() instanceof AtomicFormula)
				this.state = (AtomicFormula)state.getState();
			if(state.getState() instanceof NotAtomicFormula){
				this.state = ((NotAtomicFormula)state.getState()).getExp();
				this.negation = true;
			}
			if(state.getState() instanceof NotExp){
				this.state = (AtomicFormula)(((NotExp)state.getState()).getExp());
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
		
//		if (s.hasUnbindedParams()) return null;	//cannot bind
		if(!s.predicateEquals(this)) return null;
		
		ArrayList<Term> thisArgs = getArguments();
		ArrayList<Term> otherArgs = s.getArguments();
		
		for (int i = 0; i < thisArgs.size(); i++) {
			Term argATerm = thisArgs.get(i);
			String argAStr = thisArgs.get(i).getImage();
			String argBStr = otherArgs.get(i).getImage();
			
			if(argAStr.charAt(0) == '?' && argBStr.charAt(0) != '?'){
				binding.addBinding(argATerm, argBStr);
			} else if(argAStr.charAt(0) != '?' && argBStr.charAt(0) == '?'){
				binding.addBinding(argATerm, argAStr);
			} else if(argAStr == argBStr){
				binding.addBinding(argATerm, argAStr);
			} else if(argAStr.charAt(0) == '?' && argBStr.charAt(0) == '?'){
				continue;
			} else {
				return null;
			}
		}
//		return binding.isEmpty() ? null : binding;
		return binding;
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
		AtomicState binded = new AtomicState(this);
		binded.state = binded.state.apply(subst);
		return binded;
	}
	

	public Exp[] getTerms(){
		return new Exp[] {state};	//single state
	}
	
	@Override
	public boolean isAtomic() {return true;};
	
	public AtomicState toPositive(){
		if(isNegated())
			return new AtomicState(state);
		else
			return this;
	}
	
	@Override
	public String toString() {
		String negation = isNegated() ? "not " : "";
		return negation + this.state.toString();
	}
}
