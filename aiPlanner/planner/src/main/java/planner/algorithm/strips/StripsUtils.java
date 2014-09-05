package planner.algorithm.strips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pddl4j.exp.action.ActionDef;
import planner.model.Action;
import planner.model.StripsState;

public class StripsUtils {
	public static Set<BindedStripsAction> findApplicableActions(AtomicState state, Set<Action> availableActions){
		Set<BindedStripsAction> applicableActions = new HashSet<BindedStripsAction>();
		
		ParameterBinding b;
		for (Action a : availableActions) {
			if((b = a.bindToProduce(state)) != null){
				BindedStripsAction action = a.bindParameters(b);
				applicableActions.add(action);
			}
		}
		return applicableActions;
	}
	
	/**
	 * Sorts actions into list. In the beginning there are actions that has least non-satisfied preconditions and effects
	 * @param actions - actions to sort
	 * @param currentState	- current state
	 */
	public static List<BindedStripsAction> sortActions(Set<BindedStripsAction> actions, final StripsState currentState, final StripsState goal){
		List<BindedStripsAction> actionList = new ArrayList<BindedStripsAction>(actions);
		
		Collections.sort(actionList, new Comparator<BindedStripsAction>(){

			public int compare(BindedStripsAction o1, BindedStripsAction o2) {
				int o1PreCount = getUnsatisfiedPreconditions(o1, currentState);
				int o2PreCount = getUnsatisfiedPreconditions(o2, currentState);
				
				int o1EffCount = getUnsatisfiedEffects(o1, goal);
				int o2EffCount = getUnsatisfiedEffects(o2, goal);
				
				if(o1PreCount == o2PreCount)
					return Integer.compare(o1EffCount, o2EffCount);
				else
					return Integer.compare(o1PreCount, o2PreCount);
				
			}});
		
		return actionList;
	}
	
	private static int getUnsatisfiedPreconditions(BindedStripsAction a, StripsState s){
		int count = 0;
		AtomicState[] pres = a.getBindedPreconditions();
		for (AtomicState pre : pres) {
			if(!s.satisfies(pre)) count++;
		}
		return count;
	}
	
	private static int getUnsatisfiedEffects(BindedStripsAction a, StripsState goal){
		int count = 0;
		AtomicState[] effs = a.getBindedEffects();
		for (AtomicState e : effs) {
			if(!goal.satisfies(e)) count++;
		}
		return count;
	}
}
