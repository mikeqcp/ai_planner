package planner.algorithm.strips.logs;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.strips.StripsStack;
import planner.algorithm.strips.StripsState;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.ProcessStateDump;

public class StripsLogBuilder {
	List<ProcessStateDump> stateHistory = new ArrayList<ProcessStateDump>();
	
	public void dump(StripsState currentState, StripsStack stack, ResultPlan currentPlan){
		StripsStateDump dump = new StripsStateDump();
		
		dump.setPlan(new ResultPlan(currentPlan));
		dump.setStack(new StripsStack(stack));
		dump.setState(currentState);
		
		stateHistory.add(dump);
	}
	
	private PrintablePlan getResultPlan(){
		ProcessStateDump finalState =  stateHistory.get(stateHistory.size() - 1);
		return finalState.getPlan();
	}
	
	public ProcessLog getProcessLog(){
		PrintablePlan resultPlan = getResultPlan();
		return new ProcessLog(stateHistory, resultPlan);
	}
}
