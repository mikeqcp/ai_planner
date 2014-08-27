package planner.algorithm.strips;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pddl4j.exp.action.ActionDef;
import planner.model.Action;
import planner.algorithm.strips.ParameterBinding;

public class StripsUtils {
	public static Set<BindedStripsAction> findApplicableActions(AtomicState state, Set<StripsAction> availableActions){
		Set<BindedStripsAction> applicableActions = new HashSet<BindedStripsAction>();
		
		ParameterBinding b;
		for (StripsAction a : availableActions) {
			if((b = a.bindToProduce(state)) != null){
				BindedStripsAction action = a.bindParameters(b);
				applicableActions.add(action);
			}
		}
		return applicableActions;
	}
	
	public static Set<StripsAction> createActionSet(Iterator<pddl4j.exp.action.ActionDef> actionsIterator){
		Set<StripsAction> actionSet = new HashSet<StripsAction>();
		while(actionsIterator.hasNext()){
			ActionDef srcAction = actionsIterator.next();
			actionSet.add(new StripsAction(srcAction));
		}
		return actionSet;
	}
}
