package planner.algorithm.strips;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import pddl4j.exp.AtomicFormula;
import pddl4j.exp.term.Term;
import planner.model.State;

public class AtomicState implements State {
	private AtomicFormula state;

	public AtomicState(AtomicFormula state) {
		this.state = state;
	}
	
	
	public AtomicState(StripsState state) {
		if(state.isAtomic())
			this.state = (AtomicFormula)state.getState();
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
		if (s.hasUnbindedParams()) return null;	//cannot bind
		if(!s.predicateEquals(this)) return null;
		
		ArrayList<Term> thisArgs = getArguments();
		ArrayList<Term> otherArgs = s.getArguments();
		
		ParameterBinding binding = new ParameterBinding();
		
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
	
}
