package planner.model;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.Algorithm.AlgorithmType;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.ProcessStateDump;


public class ProcessLog {	
	private List<ProcessStateDump> stateHistory;
	private PrintablePlan resultPlan;
	private AlgorithmType type;
	
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

	public AlgorithmType getType() {
		return type;
	}

	public void setType(AlgorithmType type) {
		this.type = type;
	}
	
	
}
