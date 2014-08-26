package planner.algorithm.strips;

import java.util.Collection;
import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import planner.algorithm.Algorithm;
import planner.algorithm.logic.TermOperations;
import planner.model.ProcessSteps;
import planner.model.ResultPlan;

public class StripsAlgorithm extends Algorithm {
	private PDDLObject originalData;
	private StripsState initialState;
	private StripsState currentState;
	private Set<StripsAction> actions;
	private StripsState goal;
	private ResultPlan plan;

	private StripsStack stack;

	public StripsAlgorithm(PDDLObject input) {
		super(input);
		initializeProblemData(input);
		initializeStructures();
	}
	
	public StripsAlgorithm(StripsAlgorithm otherInstance){
		super(otherInstance.originalData);
		initializeProblemData(input);
		initializeStructures(otherInstance.stack, otherInstance.currentState, otherInstance.plan);
	}

	private void initializeProblemData(PDDLObject input) {
		this.originalData = input;
		this.goal = new StripsState(input.getGoal());

		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new StripsState(
				TermOperations.joinExprElements(initialExp));

		this.actions = StripsUtils.createActionSet(input.actionsIterator());
	}

	private void initializeStructures() {
		currentState = initialState;
		plan = new ResultPlan();
		
		stack = new StripsStack();
		stack.push(new StackItem(initialState));
		
		Exp[] statesDiff = goal.minus(currentState);
		for (Exp exp : statesDiff) {
			StripsState expState = new StripsState(exp);
			stack.push(new StackItem(expState));
		}
	}
	
	private void initializeStructures(StripsStack stack, StripsState currentState, ResultPlan currentPlan) {
		this.stack = stack;
		this.currentState = currentState;
		this.plan = currentPlan;
	}

	@Override
	public ProcessSteps getProcessHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultPlan solve() {
		System.out.println("STRIPS started");
		return execute();
	}
	
	private ResultPlan execute(){
		while(!stack.isEmpty()){
			StackItem topItem = stack.pop();
			processStackItem(topItem);
		}
		return plan;
	}
	
	private void processStackItem(StackItem item){
		if(item.isAction()) {
			BindedStripsAction action = item.getAction();
			currentState = action.applyTo(currentState);
			plan.addNextStep(action);	//add action to plan
		} else {
			StripsState s = item.getState();
			if(currentState.satisfies(s)){
				return;
			}
			if(!s.isAtomic()){	//break complex state into simple ones
				Exp[] stateExp = s.getTerms();
				for (Exp exp : stateExp) {
					stack.push(new StackItem(s));
				}
			} else {	//handle simple state
				processStateItem(s);
			}
		}
	}

	private void processStateItem(StripsState s) {
		//find applicable action
		Set<BindedStripsAction> applicableActions = StripsUtils.findApplicableActions(s, actions);
		
		//test
		for (BindedStripsAction a : applicableActions) {
			a.applyTo(currentState);
		}
		
	}
}
