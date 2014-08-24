package planner.model;

import java.util.ArrayList;
import java.util.List;


public class ProcessHistory {
	private List<ProcessStateDump> stateHistory;
	private ResultPlan resultPlan;
	
	public ProcessHistory(List<ProcessStateDump> stateHistory, ResultPlan resultPlan) {
		super();
		this.stateHistory = stateHistory;
		this.resultPlan = resultPlan;
	}

	public List<ProcessStateDump> getStateHistory() {
		return stateHistory;
	}

	public void setStateHistory(List<ProcessStateDump> stateHistory) {
		this.stateHistory = stateHistory;
	}

	public ResultPlan getResultPlan() {
		return resultPlan;
	}

	public void setResultPlan(ResultPlan resultPlan) {
		this.resultPlan = resultPlan;
	}
	
	
}
