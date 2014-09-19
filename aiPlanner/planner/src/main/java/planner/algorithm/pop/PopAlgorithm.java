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
import planner.model.AtomicState;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public class PopAlgorithm extends Algorithm {
	private static final int MAX_ITERATIONS = 1000;

	private PopLogBuilder logBuilder;
	private ConstraintProtector protector;
	private GraphBuilder builder;
	private long iteration = 0;

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
		log(graph, null);
		SolutionGraph finalGraph = solve(graph);
		if(finalGraph != null)
			log(finalGraph, null);
		
		ResultPlan finalPlan = finalGraph != null ? linearize(finalGraph) : new ResultPlan();
		
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
			if(iteration++ > MAX_ITERATIONS) return null;
			builder = new GraphBuilder(this);

			SubGoal nextGoal = graph.nextGoalToSatisfy();
			Set<SolutionGraph> options = builder.satisfyGoal(graph, nextGoal);

			for (SolutionGraph o : options) {
				if(o.getAllNodes().size() > (maxPlanLength + 2)) continue;	//count actions without start and end
				
				log(o, nextGoal);
				SolutionGraph solution = solve(o);
				if (solution != null)
					return solution;
			}
			return null;
		}
		return graph;	//return if complete
	}
	
	private ResultPlan linearize(SolutionGraph graph){
		SolutionLinearizator linearizator = new SolutionLinearizator(graph);
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
