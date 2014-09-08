package planner.algorithm.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.OrExp;
import planner.algorithm.strips.AtomicState;

public class TermOperations {
	
	public static Exp joinExprElements(AtomicState... expressions){
		AndExp and = new AndExp();
		for (AtomicState s : expressions) {
			and.add(s.getFormula());
		}
		return and;
	}
	
	public static Exp joinExprElements(Exp... expressions){
		AndExp and = new AndExp();
		for (Exp e : expressions) {
			and.add(e);
		}
		return and;
	}
	
	public static Exp[] splitExprElements(Exp expr){
		Iterator<Exp> elementsIterator;
		if(expr instanceof AndExp){
			elementsIterator = ((AndExp)expr).iterator();
		} else if(expr instanceof OrExp){
			elementsIterator = ((OrExp)expr).iterator();
		} else {
			return new Exp[] {expr};
		}
		
		List<Exp> exprList = new ArrayList<Exp>();
		while(elementsIterator.hasNext()){
			exprList.add(elementsIterator.next());
		}
		return exprList.toArray(new Exp[0]);
	}
}
