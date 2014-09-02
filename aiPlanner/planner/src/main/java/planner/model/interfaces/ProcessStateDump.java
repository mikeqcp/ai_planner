package planner.model.interfaces;


public interface ProcessStateDump {
	Object getStructure();
	PrintableState getState();
	PrintablePlan getPlan();
}
