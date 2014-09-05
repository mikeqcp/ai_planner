package planner.algorithm.regression;

import java.util.List;
import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import pddl4j.exp.term.Constant;
import planner.algorithm.Algorithm;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.regression.logs.RegressionLogBuilder;
import planner.algorithm.strips.AtomicState;
import planner.algorithm.strips.BindedStripsAction;
import planner.algorithm.strips.StackItem;
import planner.algorithm.strips.StripsUtils;
import planner.model.Action;
import planner.model.Constraint;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.StripsState;

public class RegressionAlgorithm extends Algorithm {
	private RegState initialState;
	private RegState currentState;
	private Set<Action> actions;
	private RegState goal;
	private ResultPlan plan;
	private Set<Constant> constants;
	private Set<Constraint> constraints;
	private RegressionLogBuilder logBuilder;
	
	private RegTree tree;
	private TreeBuilder treeBuilder;
	private WalkQueue treeWalker;
	

	public RegressionAlgorithm(PDDLObject input) {
		super(input);
		initializeProblemData(input);
		initializeStructures();
		this.logBuilder = new RegressionLogBuilder();
	}

//	public RegressionAlgorithm(RegressionAlgorithm otherInstance){
//		super(otherInstance.originalData);
//		initializeProblemData(originalData);
//		initializeStructures(otherInstance.tree, otherInstance.currentState, otherInstance.plan);
//		this.logBuilder = otherInstance.logBuilder;
//	}

	private void initializeProblemData(PDDLObject input) {
		this.goal = new RegState(input.getGoal());
		this.constants = getInstanceConstants();

		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new RegState(
				TermOperations.joinExprElements(initialExp));

		this.actions = getInstanceActions();
		this.constraints = getInstanceConstraints();
	}
	
	private void initializeStructures() {
		currentState = initialState;
		plan = new ResultPlan();
		tree = new RegTree(currentState);
		treeBuilder = new TreeBuilder(tree);
		treeWalker = treeBuilder.getWalker();
	}
	
//	private void initializeStructures(RegTree tree, StripsState currentState, ResultPlan currentPlan) {
//		this.tree = new RegTree(tree);
//		this.currentState = new RegState(currentState);
//		this.plan = new ResultPlan(currentPlan);
//	}


	@Override
	public ResultPlan solve() {
		System.out.println("REGRESSION started");
		log();
		ResultPlan finalPlan = execute();
		
		System.out.println(finalPlan);
		
		return finalPlan;
	}
	
	private ResultPlan execute(){
		
		
//		while(!stack.isEmpty()){
//			StackItem topItem = stack.pop();
//			
//			log();	//every item taken
//			
//			boolean succeeded = processStackItem(topItem);
//			if(!succeeded) return null;
//		}
//		return plan;
		return null;
	}
	
//	private boolean processStackItem(StackItem item){
//		boolean succeeded = true;
//		if(item.isActionType()) {
//			BindedStripsAction action = item.getAction();
//			currentState = new RegState(action.applyTo(currentState));
//			plan.addNextStep(action);	//add action to plan
//			log();	//every action added to plan
//		} else {
//			StripsState s = item.getState();
//			if(currentState.satisfies(s)){
//				return true;
//			}
//			if(!s.isAtomic()){	//break complex state into simple ones
//				StripsState[] states = s.breakIntoTerms();
//				for (StripsState st : states) {
////					stack.push(new StackItem(st));
//					log();	//every item added to stack
//				}
//			} else {	//handle simple state
//				succeeded = processStateItem(s.toAtomic());
//			}
//		}
//		return succeeded;
//	}

	/**
	 * @param s
	 * @return true if succeeded, false if there is no solution was found
	 */
//	private boolean processStateItem(AtomicState s) {
//		//find applicable action
//		Set<BindedStripsAction> applicableActions = StripsUtils.findApplicableActions(s, actions);
//		if(applicableActions.isEmpty()) return false;
//		
//		for (BindedStripsAction a : applicableActions) {
//			a.fillFreeParameters(constants);
//		}
//		
//		List<BindedStripsAction> sortedApplicableActions = StripsUtils.sortActions(applicableActions, currentState, goal);
//		
//		//use every applicable actions
//		for (BindedStripsAction a : sortedApplicableActions) {
//			//and now recursion
//			RegressionAlgorithm inner = new RegressionAlgorithm(this);
//			inner.prepareAction(a);
//			
//			ResultPlan p = inner.execute();
//			//solution found, stop process
//			if(p != null) {
//				this.currentState = inner.currentState;
//				this.plan = inner.plan;
////				this.stack = inner.stack;
//				return true;
//			}
//			break;
//		}
//		return true;
//	}
	
//	private void prepareAction(BindedStripsAction actionToUse){		
//		AtomicState[] preconditions = actionToUse.getBindedPreconditions();
//		
//		Exp joinedPreconditions = TermOperations.joinExprElements(preconditions);
//		StripsState joinedState = new StripsState(joinedPreconditions);
//		
////		this.stack.push(new StackItem(actionToUse));
//		log();	//afer selected action pushed on stack
//		
////		this.stack.push(new StackItem(joinedState));
//		log();	//after action preconditions added to stack
//		
//		for (AtomicState atomic : preconditions) {
////			this.stack.push(new StackItem(atomic));
//			log();	//after broken action preconditions added to stack
//		}
//	}
	
	private void log(){
		if(logBuilder == null) return;
		logBuilder.dump(this.currentState, this.tree, this.plan);
	}

	@Override
	public ProcessLog getLog() {
		return logBuilder.getProcessLog();
	}
}
