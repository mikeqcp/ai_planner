package planner.algorithm.regression.logs;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.Algorithm.AlgorithmType;
import planner.algorithm.regression.RegState;
import planner.algorithm.regression.RegTree;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.interfaces.ProcessStateDump;

public class RegressionLogBuilder {
	List<ProcessStateDump> stateHistory = new ArrayList<ProcessStateDump>();
	
	public void dump(RegState currentState, RegTree tree, ResultPlan currentPlan){
		System.out.println("Log: " + currentState);
		
//		RegStateDump dump = new StripsStateDump();
//		
//		dump.setPlan(new ResultPlan(currentPlan));
//		dump.setStack(new StripsStack(stack));
//		dump.setState(currentState);
//		
//		stateHistory.add(dump);
	}
	
	public ProcessLog getProcessLog(){
		ProcessLog log = new ProcessLog(null, null);
		log.setType(AlgorithmType.REGRESSION);
		return log;
	}
}
