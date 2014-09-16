package planner.algorithm.pop.logs;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.Algorithm.AlgorithmType;
import planner.model.ProcessLog;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.ProcessStateDump;

public class PopLogBuilder {
	List<ProcessStateDump> stateHistory = new ArrayList<ProcessStateDump>();
	
	public void dump(){
		return;
	}
	
	private PrintablePlan getResultPlan(){
		return null;
	}
	
	public ProcessLog getProcessLog(){
		PrintablePlan resultPlan = getResultPlan();
		ProcessLog log = new ProcessLog(stateHistory, resultPlan);
		log.setType(AlgorithmType.POP);
		
		return log;
	}
}
