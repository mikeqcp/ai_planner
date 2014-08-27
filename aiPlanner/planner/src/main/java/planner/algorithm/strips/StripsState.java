package planner.algorithm.strips;

import java.util.ArrayList;

import pddl4j.exp.AndExp;
import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import planner.algorithm.logic.TermOperations;
import planner.model.State;

/**
 * @author MichalP
 *State defined by STIPS-type expression
 */
public class StripsState implements State {	
	private Exp state;

	public StripsState(StripsState s) {
		this(s.state);
	}
	
	public StripsState(Exp state) {
		this.state = state;
	}
	
	/**
	 * @param states
	 * Create state with states connected with AND operator
	 */
	public StripsState(Exp... states){
		this.state = TermOperations.joinExprElements(states);
	}

	public Exp getState() {
		return state;
	}
	
	public Exp[] getTerms(){
		if (state instanceof AndExp)
			return TermOperations.splitExprElements((AndExp)state);	//and-expression broke into single ones
		else
			return new Exp[] {state};	//single state
	}
	
	public boolean isAtomic() {
		return state instanceof AtomicFormula;
	}	

	public AtomicState toAtomic(){
		if(!isAtomic()) return null;
		return new AtomicState(this);
	}
		
	public boolean equals(StripsState s){
		return s.getState().equals(this.getState());
	}
	
	public Exp[] minus(StripsState other){
		Exp[] thisExpr = getTerms();
		Exp[] otherExpr = other.getTerms();
		ArrayList<Exp> result = new ArrayList<Exp>();
		
		for (Exp exp : thisExpr) {
			boolean contains = false; 
			for (Exp expOther : otherExpr) {
				if(exp.equals(expOther))
					contains = true;
					break;
			}
			if(!contains) result.add(exp);
		}
		
		return result.toArray(new Exp[0]);
	}

	public boolean satisfies(StripsState s){
		Exp[] terms = s.getTerms();
		for (Exp exp : terms) {
			if(!satisfiesTerm(exp)) return false;
		}
		return true;
	}
	
	private boolean satisfiesTerm(Exp t){
		Exp[] localTerms = this.getTerms();
		for (Exp exp : localTerms) {
			if(exp.equals(t)) return true;
		}
		return false;
	}
}
