package planner.algorithm.pop;

import java.util.Set;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.pop.logs.PopLogBuilder;
import planner.algorithm.pop.model.ConstraintProtector;
import planner.algorithm.pop.model.GraphBuilder;
import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SolutionLinearizator;
import planner.algorithm.pop.model.SubGoal;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public class PopAlgorithm extends Algorithm {
	private PopLogBuilder logBuilder;
	private ConstraintProtector protector;
	private GraphBuilder builder;
	

	public PopAlgorithm(PDDLObject input) {
		super(input);
		initializeStructures();
		
		this.logBuilder = new PopLogBuilder();
		this.protector = new ConstraintProtector();
	}
	
	private void initializeStructures() {
	}
	
	@Override
	public ResultPlan solve() {
		SolutionGraph graph = new SolutionGraph(initialState, goal);
		log();
		ResultPlan finalPlan = solve(graph);
		
		if(finalPlan!= null)
			System.out.println(finalPlan);
		else
			System.out.println("Algorithm did't find any solution");
		
		return finalPlan;
	}

	public ResultPlan solve(SolutionGraph graph){
		while(!graph.isComplete()){
			builder = new GraphBuilder(this);
			
			SubGoal nextGoal = graph.nextGoalToSatisfy();
			Set<SolutionGraph> options = builder.satisfyGoal(graph, nextGoal);
			
			for (SolutionGraph o : options) {
				ResultPlan solution = solve(o);
				if(solution != null) return solution;
			}
			
			if(options.isEmpty()) return null;
			
		}
		System.out.println(graph);
		SolutionLinearizator linearizator = new SolutionLinearizator(graph);
		ResultPlan plan = linearizator.linearizeSolution();
		return plan;
	}
	
	@Override
	public ProcessLog getLog() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void log(){
		logBuilder.dump();
	}

}
