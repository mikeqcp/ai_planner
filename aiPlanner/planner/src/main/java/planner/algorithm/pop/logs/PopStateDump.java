package planner.algorithm.pop.logs;

import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SubGoal;
import planner.model.AtomicState;
import planner.model.ResultPlan;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.PrintableState;
import planner.model.interfaces.ProcessStateDump;

public class PopStateDump implements ProcessStateDump {

	private PrintableGraph graph;
	private SolutionGraph originalGraph;
	private ResultPlan plan;
	private String goal;
	private long goalNodeId;
	private boolean fallback = false;
	
	public void setGraph(SolutionGraph graph) {
		this.originalGraph = graph;
		this.graph = new PrintableGraph(graph);
	}
	
	public void setPlan(ResultPlan plan) {
		this.plan = plan;
	}

	public Object getStructure() {
		return graph;
	}	
	
	public String getGoal() {
		return goal;
	}

	public long getGoalNodeId() {
		return goalNodeId;
	}

	public void setGoal(SubGoal goal) {
		if(goal != null) {
			this.goal = goal.getGoal(originalGraph).toString();
			this.goalNodeId = goal.getNode().getId();
		}
	}
	
	public String getGoals(){
		return graph.getGoals();
	}

	public void markFallback() {
		this.fallback = true;
	}

	public boolean isFallback() {
		return fallback;
	}
	
	

}
