package planner.algorithm.pop.logs;

import java.util.ArrayList;
import java.util.List;

import planner.algorithm.Algorithm.AlgorithmType;
import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SubGoal;
import planner.algorithm.regression.logs.RegressionStateDump;
import planner.model.AtomicState;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.ProcessStateDump;

public class PopLogBuilder {
	List<ProcessStateDump> stateHistory = new ArrayList<ProcessStateDump>();
	private ResultPlan finalPlan;
	private boolean markFallback = false;
	
	public void dump(SolutionGraph graph, SubGoal goal){
		PopStateDump dump = new PopStateDump();
		dump.setGraph(graph);
		dump.setGoal(goal);
		
		if(markFallback) {
			markFallback= false;
			dump.markFallback();
		}
			
		stateHistory.add(dump);
	}
	
	public void setFinalPlan(ResultPlan finalPlan) {
		this.finalPlan = finalPlan;
	}

	private PrintablePlan getResultPlan(){
		return new PrintablePlan() {

			public Integer[] getPlanIds() {
				return new Integer[0];
			}
			
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
		PrintablePlan resultPlan = getResultPlan();
		ProcessLog log = new ProcessLog(stateHistory, resultPlan);
		log.setType(AlgorithmType.POP);
		
		return log;
	}

	public void markFallback() {
		this.markFallback = true;
	}
	
	
}
