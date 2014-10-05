package planner.algorithm.regression.logs;

import planner.algorithm.regression.model.RegTree;
import planner.model.ResultPlan;
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

	public Object getStructure() {
		return tree;
	}

}
