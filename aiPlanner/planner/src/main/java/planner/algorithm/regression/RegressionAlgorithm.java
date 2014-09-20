package planner.algorithm.regression;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.regression.logs.RegressionLogBuilder;
import planner.algorithm.regression.model.RegTree;
import planner.algorithm.regression.model.TreeNode;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.State;

public class RegressionAlgorithm extends Algorithm {
	private static final int NODES_VISIT_LIMIT = 2000;

	private RegressionLogBuilder logBuilder;
	
	private RegTree tree;
	private TreeBuilder builder;
	private WalkQueue walker;

	private int visitedNodes = 0;
	

	public RegressionAlgorithm(PDDLObject input) {
		super(input);
		initializeStructures();
		this.logBuilder = new RegressionLogBuilder();
	}
	
	private void initializeStructures() {
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
		System.out.println("Regression finished after visiting " + visitedNodes + " nodes.");
		
		logFinalPlan(finalPlan);
		
		return finalPlan;
	}
	
	private ResultPlan execute(){		
		while(walker.hasNodesToVisit()){
			if(++visitedNodes > NODES_VISIT_LIMIT) break;
			
			TreeNode node = walker.getNextNode();
			
			if(!node.isConsistent(constraints, constants)) continue;
			
			State nodeState = node.getState();
			
			if(nodeState.equals(initialState)) {
				node.visit();
				log();
				return tree.findPlanForNode(node);	//final plan
			}
			
			if(tree.getNodeLvl(node) < (maxPlanLength))
				builder.generateNextLevel(node);
			log();
		}

		return new ResultPlan();
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
