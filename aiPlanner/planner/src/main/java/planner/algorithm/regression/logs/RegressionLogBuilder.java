package planner.algorithm.regression.logs;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.Algorithm.AlgorithmType;
import planner.algorithm.regression.RegTree;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.State;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.ProcessStateDump;

public class RegressionLogBuilder {
	List<ProcessStateDump> stateHistory = new ArrayList<ProcessStateDump>();
	private ResultPlan finalPlan;
	
	public void setResultPlan(ResultPlan plan){
		this.finalPlan = plan;
	}
	
	public void dump(RegTree tree){
		
		RegressionStateDump dump = new RegressionStateDump();
		dump.setTree(tree);

		stateHistory.add(dump);
	}
	
	private PrintablePlan getPrintablePlan() {
		return new PrintablePlan() {

			public String getLabel() {
				return finalPlan.toString();
			}

			@Override
			public String toString() {
				return getLabel();
			}
		};
	}
	
	public ProcessLog getProcessLog(){
		ProcessLog log = new ProcessLog(stateHistory, getPrintablePlan());
		log.setType(AlgorithmType.REGRESSION);
		return log;
	}
}
