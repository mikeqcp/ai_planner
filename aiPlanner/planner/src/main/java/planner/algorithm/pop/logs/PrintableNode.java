package planner.algorithm.pop.logs;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.pop.model.GraphNode;
import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SubGoal;

public class PrintableNode {
	private long id;
	private String label;
	private List<String> goals;
	
	public PrintableNode(GraphNode n, SolutionGraph parent) {
		this.id = n.getId();
		this.label = n.toString();
		addGoals(parent);
	}
	
	public void addGoals(SolutionGraph graph) {
		this.goals = new ArrayList<String>();
		for (SubGoal goal : graph.getUnsatisfiedGoals()) {
			if(goal.getNode().getId() == this.id)
				this.goals.add(goal.toString());
		}
	}

	public List<String> getGoals() {
		return goals;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
}
