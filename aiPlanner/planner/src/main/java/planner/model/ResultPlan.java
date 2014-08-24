package planner.model;

import java.util.List;

public class ResultPlan {
	private List<State> plan;

	public List<State> getPlan() {
		return plan;
	}

	public void setPlan(List<State> plan) {
		this.plan = plan;
	}

	public ResultPlan(List<State> plan) {
		this.plan = plan;
	}
}
