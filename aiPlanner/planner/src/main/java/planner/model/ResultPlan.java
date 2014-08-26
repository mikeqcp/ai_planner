package planner.model;

import java.util.ArrayList;
import java.util.List;

public class ResultPlan {
	private List<Action> plan;

	public List<Action> getPlan() {
		return plan;
	}

	public void setPlan(List<Action> plan) {
		this.plan = plan;
	}

	public ResultPlan() {
		this.plan = new ArrayList<Action>();
	}
}
