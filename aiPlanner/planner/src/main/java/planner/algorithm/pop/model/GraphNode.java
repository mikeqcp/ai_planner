package planner.algorithm.pop.model;

import planner.model.Action;
import planner.model.ParameterBinding;

/**
 * @author MichalP
 *	Represents graph node - action to be performed in final plan
 *	Contains its preconditions and effects
 */
public class GraphNode {
	private Action action;
	private ParameterBinding binding;
	
	private OrderLink[] outcomingOrderLinks;
	private OrderLink[] incomingOrderLinks;
	private CasualLink[] outcomingCasualLinks;
	private CasualLink[] intcomingCasualLinks;
	
	public GraphNode(Action action) {
		this.action = action;
	}
	
	public GraphNode(GraphNode other) {
		// TODO Auto-generated constructor stub
	}
	
	public void promote(){}
	public void demote(){}
	
}
