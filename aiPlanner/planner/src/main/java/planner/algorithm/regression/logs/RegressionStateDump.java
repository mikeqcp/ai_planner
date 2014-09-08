package planner.algorithm.regression.logs;

import planner.algorithm.regression.RegTree;
import planner.algorithm.strips.StripsStack;
import planner.model.ResultPlan;
import planner.model.State;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.PrintableState;
import planner.model.interfaces.ProcessStateDump;

public class RegressionStateDump implements ProcessStateDump {

	private PrintableRegTree tree;
	private ResultPlan plan;
	
	public void setTree(RegTree tree) {
		this.tree = new PrintableRegTree(tree);
	}
	
	public void setPlan(ResultPlan plan) {
		this.plan = plan;
	}

	public PrintableState getState() {
		return new PrintableState() {
			
			public String getLabel() {
				return "";
			}
			
			@Override
			public String toString() {
				return getLabel();
			}
		};
	}

	public PrintablePlan getPlan() {
		return new PrintablePlan() {
			
			public String getLabel() {
				return "";
			}
			
			@Override
			public String toString() {
				return "";
			}
		};
	}

	public Object getStructure() {
		return tree;
	}

}
