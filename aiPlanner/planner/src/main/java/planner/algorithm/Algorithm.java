package planner.algorithm;

import pddl4j.PDDLObject;
import planner.model.ProcessSteps;
import planner.model.ResultPlan;

public abstract class Algorithm {
	protected PDDLObject input;
	
	public Algorithm(PDDLObject input) {
		this.input = input;
	}
	
	abstract public ProcessSteps getProcessHistory();
	abstract public ResultPlan solve();
}
