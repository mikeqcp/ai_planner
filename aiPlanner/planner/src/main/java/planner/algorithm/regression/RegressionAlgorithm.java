package planner.algorithm.regression;

import pddl4j.PDDLObject;
import pddl4j.exp.Exp;
import planner.algorithm.Algorithm;
import planner.algorithm.logic.TermOperations;
import planner.algorithm.regression.logs.RegressionLogBuilder;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.State;

public class RegressionAlgorithm extends Algorithm {
	private State initialState;
	private State currentState;
	private State goal;
	private ResultPlan plan;
	private RegressionLogBuilder logBuilder;
	
	private RegTree tree;
	private TreeBuilder builder;
	private WalkQueue walker;
	

	public RegressionAlgorithm(PDDLObject input) {
		super(input);
		initializeProblemData(input);
		initializeStructures();
		this.logBuilder = new RegressionLogBuilder();
	}


	private void initializeProblemData(PDDLObject input) {
		this.goal = new State(input.getGoal());
		this.constants = produceInstanceConstants();

		Exp[] initialExp = input.getInit().toArray(new Exp[0]);
		this.initialState = new State(
				TermOperations.joinExprElements(initialExp));

		this.actions = produceInstanceActions();
		this.constraints = produceInstanceConstraints();
	}
	
	private void initializeStructures() {
		plan = new ResultPlan();
		tree = new RegTree(goal);
		builder = new TreeBuilder(tree, goal, this);
		builder.setActions(this.actions);
		walker = builder.getWalker();
	}
	
	@Override
	public ResultPlan solve() {
		System.out.println("REGRESSION started");
		log();
		ResultPlan finalPlan = execute();
		
		System.out.println(finalPlan);
		
		logFinalPlan(finalPlan);
		
		return finalPlan;
	}
	
	private ResultPlan execute(){		
		while(walker.hasNodesToVisit()){
			TreeNode node = walker.getNextNode();
			
			if(!node.isConsistent(constraints, constants)) continue;
			
			State nodeState = node.getState();
			if(nodeState.equals(initialState)) 
				return tree.findPlanForNode(node);	//final plan
			
			builder.generateNextLevel(node);
			log();
		}

		return null;
	}
	

	
	private void log(){
		if(logBuilder == null) return;
		logBuilder.dump(this.tree);
	}
	
	private void logFinalPlan(ResultPlan plan){
		if(logBuilder == null) return;
		logBuilder.setResultPlan(plan);
	}

	@Override
	public ProcessLog getLog() {
		return logBuilder.getProcessLog();
	}
}
