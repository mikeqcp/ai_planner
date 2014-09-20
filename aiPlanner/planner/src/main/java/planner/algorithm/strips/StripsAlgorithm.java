package planner.algorithm.strips;

import java.util.List;
import java.util.Set;

import javax.print.attribute.standard.MediaSize.Other;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import pddl4j.exp.term.Constant;
import planner.algorithm.Algorithm;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.strips.logs.StripsLogBuilder;
import planner.algorithm.strips.model.StackItem;
import planner.algorithm.strips.model.StripsStack;
import planner.model.Action;
import planner.model.AtomicState;
import planner.model.BindedAction;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.State;

public class StripsAlgorithm extends Algorithm {
	private State currentState;
	private ResultPlan plan;
	private StripsStack stack;
	private StripsLogBuilder logBuilder;
	private int iteration = 0;

	public StripsAlgorithm(PDDLObject input) {
		super(input);
		initializeStructures();
		this.logBuilder = new StripsLogBuilder();
	}
	
	public StripsAlgorithm(StripsAlgorithm otherInstance){
		super(otherInstance.originalData);
		initializeStructures(otherInstance.stack, otherInstance.currentState, otherInstance.plan);
		this.logBuilder = otherInstance.logBuilder;
		this.iteration = otherInstance.iteration;
		this.maxPlanLength = otherInstance.maxPlanLength;
	}
	
	private void initializeStructures() {
		currentState = initialState;
		plan = new ResultPlan();
		
		stack = new StripsStack();
		stack.push(new StackItem(goal));
		
		Exp[] statesDiff = goal.minus(currentState);
		for (Exp exp : statesDiff) {
			State expState = new State(exp);
			stack.push(new StackItem(expState));
		}
	}
	
	private void initializeStructures(StripsStack stack, State currentState, ResultPlan currentPlan) {
		this.stack = new StripsStack(stack);
		this.currentState = new State(currentState);
		this.plan = new ResultPlan(currentPlan);
	}


	@Override
	public ResultPlan solve() {
		System.out.println("STRIPS started");
		log();
		ResultPlan finalPlan = execute();
		
		if(finalPlan == null)
			finalPlan = new ResultPlan();
		
		System.out.println(finalPlan);
		System.out.println("STRIPS finished.");
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
//		boolean succeeded = true;
		if(item.isActionType()) {
			BindedAction action = item.getAction();
			
			if(!verifyActionPreconditions(action)) return false;	//preconditions are not satisfied
			
			currentState = action.applyTo(currentState);
			plan.addNextStep(action);	//add action to plan
			log();	//every action added to plan
			return true;
		} else {
			State s = item.getState();
			if(currentState.satisfies(s)){
				return true;
			}
			if(!s.isAtomic()){
//				return true;
				//break complex state into simple ones
				State[] states = s.breakIntoAtomic();
				for (State st : states) {
					stack.push(new StackItem(st));
				}
				log();
				return true;
			} else {	//handle simple state
				if((stack.getPlanLength() + plan.getLength()) >= maxPlanLength) return false;
				
				return processStateItem(s.toAtomic());
			}
		}
//		return succeeded;
	}

	private boolean verifyActionPreconditions(BindedAction action){
		AtomicState[] pre = action.getBindedPreconditions();
		for (AtomicState p : pre) {
			if(!currentState.satisfies(p)) return false;
		}
		return true;
	}
	
	/**
	 * @param s
	 * @return true if succeeded, false if no solution was found
	 */
	private boolean processStateItem(AtomicState s) {
		//find applicable action
		Set<BindedAction> applicableActions = Utils.findApplicableActions(s, actions, constants);
		if(applicableActions.isEmpty()) return false;
		
		List<BindedAction> sortedApplicableActions = Utils.sortActions(applicableActions, currentState, goal);
		
		//use every applicable action
		for (BindedAction a : sortedApplicableActions) {
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
//			break;
//			return false;	
		}
		return false;//no action led to solution
	}
	
	private void prepareAction(BindedAction actionToUse){		
		AtomicState[] preconditions = actionToUse.getBindedPreconditions();
		
		Exp joinedPreconditions = TermOperations.joinExprElements(preconditions);
		State joinedState = new State(joinedPreconditions);
		
		this.stack.push(new StackItem(actionToUse));
		log();	//afer selected action pushed on stack
		
		this.stack.push(new StackItem(joinedState));
//		log();	//after action preconditions added to stack
		
		for (AtomicState atomic : preconditions) {
			this.stack.push(new StackItem(atomic));
		}
		log();	//after broken action preconditions added to stack
	}
	
	private void log(){
		if(logBuilder == null) return;
		logBuilder.dump(this.currentState, this.stack, this.plan);
	}

	@Override
	public ProcessLog getLog() {
		return logBuilder.getProcessLog();
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}
}
