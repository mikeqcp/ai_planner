package planner.algorithm.strips;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import pddl4j.exp.term.Constant;
import planner.algorithm.Algorithm;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.strips.logs.StripsLogBuilder;
import planner.model.Action;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.StripsState;

public class StripsAlgorithm extends Algorithm {
	private StripsState initialState;
	private StripsState currentState;
	private Set<Action> actions;
	private StripsState goal;
	private ResultPlan plan;
	private StripsStack stack;
	private Set<Constant> constants;
	private StripsLogBuilder logBuilder;

	public StripsAlgorithm(PDDLObject input) {
		super(input);
		initializeProblemData(input);
		initializeStructures();
		this.logBuilder = new StripsLogBuilder();
	}
	
	public StripsAlgorithm(StripsAlgorithm otherInstance){
		super(otherInstance.originalData);
		initializeProblemData(originalData);
		initializeStructures(otherInstance.stack, otherInstance.currentState, otherInstance.plan);
		this.logBuilder = otherInstance.logBuilder;
	}

	private void initializeProblemData(PDDLObject input) {
		this.goal = new StripsState(input.getGoal());
		this.constants = getInstanceConstants();

		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new StripsState(
				TermOperations.joinExprElements(initialExp));

		this.actions = getInstanceActions();
	}
	
	private void initializeStructures() {
		currentState = initialState;
		plan = new ResultPlan();
		
		stack = new StripsStack();
		stack.push(new StackItem(goal));
		
		Exp[] statesDiff = goal.minus(currentState);
		for (Exp exp : statesDiff) {
			StripsState expState = new StripsState(exp);
			stack.push(new StackItem(expState));
		}
	}
	
	private void initializeStructures(StripsStack stack, StripsState currentState, ResultPlan currentPlan) {
		this.stack = new StripsStack(stack);
		this.currentState = new StripsState(currentState);
		this.plan = new ResultPlan(currentPlan);
	}


	@Override
	public ResultPlan solve() {
		System.out.println("STRIPS started");
		log();
		ResultPlan finalPlan = execute();
		
		System.out.println(finalPlan);
		
		return finalPlan;
	}
	
	private ResultPlan execute(){
		while(!stack.isEmpty()){
			StackItem topItem = stack.pop();
			
			log();	//every item taken
			
			boolean succeeded = processStackItem(topItem);
			if(!succeeded) return null;
		}
		return plan;
	}
	
	private boolean processStackItem(StackItem item){
		boolean succeeded = true;
		if(item.isActionType()) {
			BindedStripsAction action = item.getAction();
			currentState = action.applyTo(currentState);
			plan.addNextStep(action);	//add action to plan
			log();	//every action added to plan
		} else {
			StripsState s = item.getState();
			if(currentState.satisfies(s)){
				return true;
			}
			if(!s.isAtomic()){	//break complex state into simple ones
				StripsState[] states = s.breakIntoTerms();
				for (StripsState st : states) {
					stack.push(new StackItem(st));
					log();	//every item added to stack
				}
			} else {	//handle simple state
				succeeded = processStateItem(s.toAtomic());
			}
		}
		return succeeded;
	}

	/**
	 * @param s
	 * @return true if succeeded, false if there is no solution was found
	 */
	private boolean processStateItem(AtomicState s) {
		//find applicable action
		Set<BindedStripsAction> applicableActions = StripsUtils.findApplicableActions(s, actions);
		if(applicableActions.isEmpty()) return false;
		
		for (BindedStripsAction a : applicableActions) {
			a.fillFreeParameters(constants);
		}
		
		List<BindedStripsAction> sortedApplicableActions = StripsUtils.sortActions(applicableActions, currentState, goal);
		
		//use every applicable actions
		for (BindedStripsAction a : sortedApplicableActions) {
			//and now recursion
			StripsAlgorithm inner = new StripsAlgorithm(this);
			inner.prepareAction(a);
			
			ResultPlan p = inner.execute();
			//solution found, stop process
			if(p != null) {
				this.currentState = inner.currentState;
				this.plan = inner.plan;
				this.stack = inner.stack;
				return true;
			}
			break;
		}
		return true;
	}
	
	private void prepareAction(BindedStripsAction actionToUse){		
		AtomicState[] preconditions = actionToUse.getBindedPreconditions();
		
		Exp joinedPreconditions = TermOperations.joinExprElements(preconditions);
		StripsState joinedState = new StripsState(joinedPreconditions);
		
		this.stack.push(new StackItem(actionToUse));
		log();	//afer selected action pushed on stack
		
		this.stack.push(new StackItem(joinedState));
		log();	//after action preconditions added to stack
		
		for (AtomicState atomic : preconditions) {
			this.stack.push(new StackItem(atomic));
			log();	//after broken action preconditions added to stack
		}
	}
	
	private void log(){
		if(logBuilder == null) return;
		logBuilder.dump(this.currentState, this.stack, this.plan);
	}

	@Override
	public ProcessLog getLog() {
		return logBuilder.getProcessLog();
	}
}
