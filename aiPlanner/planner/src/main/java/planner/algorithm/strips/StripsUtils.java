package planner.algorithm.strips;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pddl4j.exp.action.ActionDef;
import planner.model.Action;

public class StripsUtils {
	public static Set<BindedStripsAction> findApplicableActions(StripsState state, Set<StripsAction> availableActions){
		Set<BindedStripsAction> applicableActions = new HashSet<BindedStripsAction>();
		for (StripsAction a : availableActions) {
			if(a.canProduce(state)){
				ActionBinding binding = new ActionBinding();
				BindedStripsAction bAction = new BindedStripsAction(a, binding);
				applicableActions.add(bAction);
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
