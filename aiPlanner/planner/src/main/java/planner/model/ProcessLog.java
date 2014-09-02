package planner.model;

import java.util.List;

import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.ProcessStateDump;


public class ProcessLog {
	private List<ProcessStateDump> stateHistory;
	private PrintablePlan resultPlan;
	
	public ProcessLog(List<ProcessStateDump> stateHistory, PrintablePlan resultPlan) {
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

	public PrintablePlan getResultPlan() {
		return resultPlan;
	}

	public void setResultPlan(PrintablePlan resultPlan) {
		this.resultPlan = resultPlan;
	}
	
	
}
