package planner.model;

import java.util.ArrayList;
import java.util.List;

public class ResultPlan{
	private List<Action> plan;
	private Integer[] planIds;

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
		if(action == null) return;
		plan.add(action);
	}
	
	@Override
	public String toString() {
		String planString = "";
		
		for (Action action : plan) {
			planString += action.toString();
			planString += " -> ";
		}
		
		int endIndex = planString.length() - 4;
		return planString.substring(0, endIndex >=0 ? endIndex : 0 );
	}

	public Integer[] getPlanIds() {
		return planIds;
	}

	public void setPlanIds(Integer[] planIds) {
		this.planIds = planIds;
	}
	
	public boolean isEmpty(){
		return plan.size() == 0;
	}
	
	public int getLength(){
		return plan.size();
	}
}
