package planner.model;

import java.util.ArrayList;
import java.util.List;

public class ResultPlan {
	private List<Action> plan;

	public ResultPlan() {
		this.plan = new ArrayList<Action>();
	}
	
	public ResultPlan(ResultPlan otherPlan) {
		this.plan = new ArrayList<Action>(otherPlan.getPlan());
	}

	public List<Action> getPlan() {
		return plan;
	}

	public void setPlan(List<Action> plan) {
		this.plan = plan;
	}
	
	public void addNextStep(Action action){
		plan.add(action);
	}
}
