package planner.algorithm;

import pddl4j.PDDLObject;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public abstract class Algorithm {
	public static enum AlgorithmType {STRIPS};
	protected PDDLObject input;
	
	public Algorithm(PDDLObject input) {
		this.input = input;
	}
	
	abstract public ProcessLog getProcessHistory();
	abstract public ResultPlan solve();
	abstract public ProcessLog getLog();
	
	public static AlgorithmType typeFromString(String type){
		if(type.equalsIgnoreCase("strips")) return AlgorithmType.STRIPS;
		
		return null;
	}
}
