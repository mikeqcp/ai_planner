package planner.algorithm.pop;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.pop.logs.PopLogBuilder;
import planner.algorithm.pop.model.SolutionGraph;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public class PopAlgorithm extends Algorithm {

	private PopLogBuilder logBuilder;
	private SolutionGraph graph;
	

	public PopAlgorithm(PDDLObject input) {
		super(input);
		initializeStructures();
		this.logBuilder = new PopLogBuilder();
	}
	
	private void initializeStructures() {
		graph = new SolutionGraph(initialState, goal);
	}
	
	@Override
	public ResultPlan solve() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessLog getLog() {
		// TODO Auto-generated method stub
		return null;
	}

}
