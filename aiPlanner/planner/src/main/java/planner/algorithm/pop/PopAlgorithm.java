package planner.algorithm.pop;

import java.util.Set;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.pop.logs.PopLogBuilder;
import planner.algorithm.pop.model.ThreatProtector;
import planner.algorithm.pop.model.GraphBuilder;
import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SolutionLinearizer;
import planner.algorithm.pop.model.SubGoal;
import planner.model.AtomicState;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public class PopAlgorithm extends Algorithm {
	private static final int MAX_ITERATIONS = 10000;

	private PopLogBuilder logBuilder;
	private GraphBuilder builder;
	private long iteration = 0;

	public PopAlgorithm(PDDLObject input) {
		super(input);
		initializeStructures();

		this.logBuilder = new PopLogBuilder();
	}

	private void initializeStructures() {
	}

	@Override
	public ResultPlan solve() {
		SolutionGraph graph = new SolutionGraph(initialState, goal);
		log(graph, null);
		SolutionGraph finalGraph = solve(graph);
		
		if(finalGraph != null)
			log(finalGraph, null);
		
		ResultPlan finalPlan = finalGraph != null ? linearize(finalGraph) : new ResultPlan();
		if(finalPlan == null) finalPlan = new ResultPlan();
		
		logBuilder.setFinalPlan(finalPlan);

		if (finalGraph != null)
			System.out.println(finalPlan);
		else
			System.out.println("Algorithm did't find any solution");

		System.out.println("Finished in " + iteration + " iterations.");
		return finalPlan;
	}

	public SolutionGraph solve(SolutionGraph graph) {
		while (!graph.isComplete()) {
			if(iteration >= MAX_ITERATIONS) return null;
			iteration++;
			builder = new GraphBuilder(this);

			SubGoal nextGoal = graph.nextGoalToSatisfy();
			Set<SolutionGraph> options = builder.satisfyGoal(graph, nextGoal);
			
			for (SolutionGraph o : options) {
				if(o.getAllNodes().size() > (maxPlanLength + 2)) continue;	//count actions without start and end
				log(graph, nextGoal);
				log(o, null);
				SolutionGraph solution = solve(o);
				if (solution != null)
					return solution;
			}
			
			logBuilder.markFallback();
			return null;
		}
		return graph;	//return if complete
	}
	
	private ResultPlan linearize(SolutionGraph graph){
		SolutionLinearizer linearizator = new SolutionLinearizer(graph);
		ResultPlan plan = linearizator.linearizeSolution();		
		return plan;
	}

	@Override
	public ProcessLog getLog() {
		return logBuilder.getProcessLog();
	}

	private void log(SolutionGraph graph, SubGoal goal) {
		 logBuilder.dump(graph, goal);
	}

}
