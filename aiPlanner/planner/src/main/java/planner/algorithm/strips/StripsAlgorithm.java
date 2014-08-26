package planner.algorithm.strips;

import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import pddl4j.exp.InitEl;
import planner.algorithm.Algorithm;
import planner.algorithm.logic.TermOperations;
import planner.model.Action;
import planner.model.ExprState;
import planner.model.ProcessSteps;
import planner.model.ResultPlan;
import planner.model.State;

public class StripsAlgorithm extends Algorithm {
	private PDDLObject originalData;
	private ExprState initialState;
	private ExprState currentState;
	private Set<Action> actions;
	private ExprState goal;
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
		this.goal = new ExprState(input.getGoal());

		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new ExprState(
				TermOperations.joinExprElements(initialExp));

		this.actions = TermOperations.createActionSet(input.actionsIterator());
	}

	private void initializeStructures() {
		currentState = initialState;
		plan = new ResultPlan();
		
		stack = new StripsStack();
		stack.push(new StackItem(initialState));
		
		Exp[] statesDiff = goal.minus(currentState);
		for (Exp exp : statesDiff) {
			ExprState expState = new ExprState(exp);
			stack.push(new StackItem(expState));
		}
	}
	
	private void initializeStructures(StripsStack stack, ExprState currentState, ResultPlan currentPlan) {
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
			//TODO:apply action
			plan.addNextStep(item.getAction());	//add action to plan
		} else {
			ExprState s = item.getState();
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

	private void processStateItem(ExprState s) {
		if(currentState.satisfies(s)){
			stack.pop();
			return;
		}
		
		//find applicable action
	}
}
