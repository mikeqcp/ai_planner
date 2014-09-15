package planner.algorithm.strips.logs;

import planner.algorithm.strips.model.StripsStack;
import planner.model.ResultPlan;
import planner.model.State;
import planner.model.interfaces.PrintablePlan;
import planner.model.interfaces.PrintableState;
import planner.model.interfaces.ProcessStateDump;

public class StripsStateDump implements ProcessStateDump {

	private PrintableStripsStack stack;
	private PrintableStripsState state;
	private ResultPlan plan;
	
	public void setStack(StripsStack stack) {
		this.stack = new PrintableStripsStack(stack);
	}

	public void setState(State state) {
		this.state = new PrintableStripsState(state);
	}

	public void setPlan(ResultPlan plan) {
		this.plan = plan;
	}

	public PrintableState getState() {
		return new PrintableState() {
			
			public String getLabel() {
				return state.getLabel();
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
