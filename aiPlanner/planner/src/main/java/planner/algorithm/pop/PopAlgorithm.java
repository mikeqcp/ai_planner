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
	private static final int MAX_STEPS = 50;

	private PopLogBuilder logBuilder;
	private ConstraintProtector protector;
	private GraphBuilder builder;
	private long step = 0;

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
		ResultPlan finalPlan = solve(graph);

		if (finalPlan != null && step < MAX_STEPS)
			System.out.println(finalPlan);
		else
			System.out.println("Algorithm did't find any solution");

		System.out.println("Finished in " + step + " steps.");
		return finalPlan;
	}

	public ResultPlan solve(SolutionGraph graph) {
		while (!graph.isComplete() && step++ <= MAX_STEPS) {
			builder = new GraphBuilder(this);

			SubGoal nextGoal = graph.nextGoalToSatisfy();
			Set<SolutionGraph> options = builder.satisfyGoal(graph, nextGoal);

			for (SolutionGraph o : options) {
				log(o, nextGoal);
				ResultPlan solution = solve(o);
				if (solution != null)
					return solution;
			}

			if (options.isEmpty())
				return null;

		}
		
		SolutionLinearizator linearizator = new SolutionLinearizator(graph);
		ResultPlan plan = linearizator.linearizeSolution();
		
		logBuilder.setFinalPlan(plan);
		log(graph, null);	//final solution
		
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
