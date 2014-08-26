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
	private State initialState;
	private State currentState;
	private Set<Action> actions;
	private State goal;
	
	private StripsStack stack;
	
	public StripsAlgorithm(PDDLObject input) {
		super(input);
		this.goal = new ExprState(input.getGoal());
		
		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new ExprState(TermOperations.joinExprElements(initialExp));
		
		this.actions = TermOperations.createActionSet(input.actionsIterator());
		
		initialize();
	}
	
	private void initialize(){
		stack = new StripsStack();
		stack.push(new StackItem(initialState));
		
		currentState = initialState;
	}
	
	@Override
	public ProcessSteps getProcessHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultPlan solve() {
		System.out.println("STRIPS started");
		// TODO Auto-generated method stub
		return null;
	}
}
