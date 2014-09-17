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
	private ResultPlan plan;
	private String goal;
	
	public void setGraph(SolutionGraph graph) {
		this.graph = new PrintableGraph(graph);
	}
	
	public void setPlan(ResultPlan plan) {
		this.plan = plan;
	}

	public PrintableState getState() {
		return new PrintableState() {
			
			public String getLabel() {
				return "";
			}
			
			@Override
			public String toString() {
				return getLabel();
			}
		};
	}

	public PrintablePlan getPlan() {
		return new PrintablePlan() {
			
			public String getLabel() {
				return "";
			}
			
			@Override
			public String toString() {
				return "";
			}
		};
	}

	public Object getStructure() {
		return graph;
	}	
	
	public String getGoal() {
		return goal;
	}

	public void setGoal(SubGoal goal) {
		if(goal != null) this.goal = goal.getGoal().toString();
	}

}