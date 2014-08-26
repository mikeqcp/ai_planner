package planner.model;

import java.util.ArrayList;

import pddl4j.exp.AndExp;
import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import planner.algorithm.logic.TermOperations;

/**
 * @author MichalP
 *State defined by STIPS-type expression
 */
public class ExprState implements State {	
	private Exp state;

	public ExprState(Exp state) {
		this.state = state;
	}
	
	/**
	 * @param states
	 * Create state with states connected with AND operator
	 */
	public ExprState(Exp... states){
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
		boolean test = state instanceof AtomicFormula;
		return state instanceof AtomicFormula;
	}
	
	public boolean equals(ExprState s){
		return s.getState().equals(this.getState());
	}
	
	public Exp[] minus(ExprState other){
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

	
	public boolean satisfies(ExprState s){
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
