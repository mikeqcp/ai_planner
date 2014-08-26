package planner.algorithm.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import planner.algorithm.strips.StripsAction;
import planner.model.Action;

public class TermOperations {
	public static Exp joinExprElements(Exp... expressions){
		AndExp and = new AndExp();
		for (Exp e : expressions) {
			and.add(e);
		}
		return and;
	}
	
	public static Exp[] splitExprElements(AndExp expr){
		List<Exp> exprList = new ArrayList<Exp>();
		Iterator<Exp> elementsIterator = expr.iterator();
		while(elementsIterator.hasNext()){
			exprList.add(elementsIterator.next());
		}
		return exprList.toArray(new Exp[0]);
	}

	public static Set<Action> createActionSet(Iterator<pddl4j.exp.action.ActionDef> actionsIterator){
		Set<Action> actionSet = new HashSet<Action>();
		while(actionsIterator.hasNext()){
			ActionDef srcAction = actionsIterator.next();
			actionSet.add(new StripsAction(srcAction));
		}
		return actionSet;
	}
}
