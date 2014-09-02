package planner.algorithm;

import pddl4j.PDDLObject;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public abstract class Algorithm {
	protected PDDLObject input;
	
	public Algorithm(PDDLObject input) {
		this.input = input;
	}
	
	abstract public ProcessLog getProcessHistory();
	abstract public ResultPlan solve();
	abstract public ProcessLog getLog();
}
