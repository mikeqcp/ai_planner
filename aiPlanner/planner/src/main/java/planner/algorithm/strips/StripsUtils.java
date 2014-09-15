package planner.algorithm.strips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pddl4j.exp.term.Constant;
import planner.model.Action;
import planner.model.AtomicState;
import planner.model.BindedAction;
import planner.model.ParameterBinding;
import planner.model.State;

public class StripsUtils {
	public static Set<BindedAction> findApplicableActions(AtomicState state, Set<Action> availableActions, Set<Constant> constants){
		Set<BindedAction> applicableActions = new HashSet<BindedAction>();
		
		ParameterBinding b;
		for (Action a : availableActions) {
			if((b = a.bindToProduce(state)) != null){
				BindedAction action = a.bindParameters(b);
				action.fillFreeParameters(constants);
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
	public static List<BindedAction> sortActions(Set<BindedAction> actions, final State currentState, final State goal){
		List<BindedAction> actionList = new ArrayList<BindedAction>(actions);
		
		Collections.sort(actionList, new Comparator<BindedAction>(){

			public int compare(BindedAction o1, BindedAction o2) {
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
	
	private static int getUnsatisfiedPreconditions(BindedAction a, State s){
		int count = 0;
		AtomicState[] pres = a.getBindedPreconditions();
		for (AtomicState pre : pres) {
			if(!s.satisfies(pre)) count++;
		}
		return count;
	}
	
	private static int getUnsatisfiedEffects(BindedAction a, State goal){
		int count = 0;
		AtomicState[] effs = a.getBindedEffects();
		for (AtomicState e : effs) {
			if(!goal.satisfies(e)) count++;
		}
		return count;
	}
}
