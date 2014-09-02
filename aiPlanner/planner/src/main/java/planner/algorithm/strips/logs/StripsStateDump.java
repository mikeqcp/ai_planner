package planner.algorithm.strips.logs;

import planner.algorithm.strips.StripsStack;
import planner.algorithm.strips.StripsState;
import planner.model.ResultPlan;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.PrintableStack;
import planner.model.interfaces.PrintableState;
import planner.model.interfaces.ProcessStateDump;

public class StripsStateDump implements ProcessStateDump {

	private StripsStack stack;
	private StripsState state;
	private ResultPlan plan;
	
	public void setStack(StripsStack stack) {
		this.stack = stack;
	}

	public void setState(StripsState state) {
		this.state = state;
	}

	public void setPlan(ResultPlan plan) {
		this.plan = plan;
	}

	public PrintableState getState() {
		return new PrintableState() {
			
			public String getLabel() {
				return state.toString();
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
				return plan.toString();
			}
			
			@Override
			public String toString() {
				return getLabel();
			}
		};
	}

	public Object getStructure() {
		return stack;
	}

}
