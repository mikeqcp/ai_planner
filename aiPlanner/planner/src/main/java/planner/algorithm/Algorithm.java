package planner.algorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import pddl4j.exp.term.Constant;
import planner.algorithm.logic.TermOperations;
import planner.model.Action;
import planner.model.Constraint;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.State;

public abstract class Algorithm {
	public static enum AlgorithmType {STRIPS, REGRESSION, POP};
	protected PDDLObject originalData;
	protected Set<Constant> constants;
	protected Set<Constraint> constraints;
	protected Set<Action> actions;
	protected State initialState;
	protected State goal;
	protected int maxPlanLength = 10;
	
	public Algorithm(PDDLObject input) {
		initializeProblemData(input);
	}
	
	abstract public ResultPlan solve();
	abstract public ProcessLog getLog();
	
	public static AlgorithmType typeFromString(String type){
		if(type.equalsIgnoreCase("strips")) return AlgorithmType.STRIPS;
		if(type.equalsIgnoreCase("regression")) return AlgorithmType.REGRESSION;
		if(type.equalsIgnoreCase("pop")) return AlgorithmType.POP;		
		return null;
	}
	
	private void initializeProblemData(PDDLObject input) {
		this.originalData = input;
		this.constraints = produceInstanceConstraints();
		
		this.goal = new State(input.getGoal());
		this.constants = produceInstanceConstants();

		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new State(TermOperations.joinExprElements(initialExp));

		this.actions = produceInstanceActions();
		this.constraints = produceInstanceConstraints();
	}
	
	protected Set<Constant> produceInstanceConstants(){
		Set<Constant> constants = new HashSet<Constant>();
		
		Iterator<Constant> iter = originalData.constantsIterator();
		while(iter.hasNext()){
			constants.add(iter.next());
		}
		return constants;
	}
	
	public Set<Action> produceInstanceActions(){
		Iterator<ActionDef> actionsIterator = originalData.actionsIterator(); 
		Set<Action> actionSet = new HashSet<Action>();
		while(actionsIterator.hasNext()){
			ActionDef srcAction = actionsIterator.next();
			if(srcAction.getName().startsWith("fake_constraint")) continue;
			actionSet.add(new Action(srcAction));
		}
		return actionSet;
	}
	
	protected Set<Constraint> produceInstanceConstraints(){
		Set<Constraint> constraints = new HashSet<Constraint>();
		Iterator<ActionDef> actionsIterator = originalData.actionsIterator();	//exract from fake actions
		
		while(actionsIterator.hasNext()){
			ActionDef srcAction = actionsIterator.next();
			if(srcAction.getName().startsWith("fake_constraint")){
				pddl4j.exp.action.Action actionInst = (pddl4j.exp.action.Action)srcAction;
				constraints.add(new Constraint(actionInst));
			}
		}
		return constraints;
	}

	public Set<Constant> getConstants() {
		return constants;
	}

	public Set<Constraint> getConstraints() {
		return constraints;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setMaxPlanLength(int maxPlanLength) {
		this.maxPlanLength = maxPlanLength;
	}

}
