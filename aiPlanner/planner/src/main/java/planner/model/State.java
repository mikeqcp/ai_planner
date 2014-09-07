package planner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pddl4j.exp.AndExp;
import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import pddl4j.exp.NotAtomicFormula;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.strips.AtomicState;

/**
 * @author MichalP
 *State defined by STIPS-type expression
 */
public class State {	
	protected Exp state;

	public State(State s) {
		this(s.state);
	}
	
	public State(Exp state) {
		this.state = state;
	}
	
	/**
	 * @param states
	 * Create state with states connected with AND operator
	 */
	public State(Exp... states){
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
		return (state instanceof AtomicFormula) || (state instanceof NotAtomicFormula);
	}	

	public AtomicState toAtomic(){
		if(!isAtomic()) return null;
		return new AtomicState(this);
	}
		

	public Exp[] minus(State other){
		Exp[] thisExpr = getTerms();
		Exp[] otherExpr = other.getTerms();
		ArrayList<Exp> result = new ArrayList<Exp>();
		
		for (Exp exp : thisExpr) {
			boolean contains = false; 
			for (Exp expOther : otherExpr) {
				if(exp.equals(expOther)){
					contains = true;
					break;
				}
			}
			if(!contains) result.add(exp);
		}
		
		return result.toArray(new Exp[0]);
	}

	/**
	 * @param s
	 * @return True if has exactly the same terms like s
	 */
	public boolean equals(State s){
		return satisfies(s) && 
				(s.getTerms().length == this.getTerms().length);
	}
	
	/**
	 * @param s
	 * @return True if satisfied all terms from s
	 */
	public boolean satisfies(State s){
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
	
	public AtomicState[] breakIntoTerms(){
		Exp[] terms = getTerms();
		AtomicState[] states = new AtomicState[terms.length];
		for (int i = 0; i < terms.length; i++) {
			states[i] = new State(terms[i]).toAtomic();
		}
		return states;
	}

	public State addTerm(AtomicState s){
		if(containsTerm(s.getFormula())) return this;
		
		Exp[] terms = getTerms();
		Exp[] updatedTerms = Arrays.copyOf(terms, terms.length + 1);
		updatedTerms[updatedTerms.length - 1] = s.getFormula();
		
		return new State(updatedTerms);
	}
	
	private boolean containsTerm(Exp target){
		Exp[] terms = getTerms();
		for (Exp t : terms) {
			if(t.equals(target)) return true;
		}
		return false;
	}
	
	public State removeTerm(AtomicState s){
		Exp[] terms = getTerms();
		List<Exp> updatedTerms = new ArrayList<Exp>();
		for (Exp t : terms) {
			if(t.equals(s.getFormula())) continue;
			
			updatedTerms.add(t);
		}
		
		return new State(updatedTerms.toArray(new Exp[0]));
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
	
	public boolean isConsistent(){
		return true;
	}
}
