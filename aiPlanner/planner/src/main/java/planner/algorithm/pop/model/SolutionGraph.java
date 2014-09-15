package planner.algorithm.pop.model;

import java.util.Set;

public class SolutionGraph {
	private Set<GraphLink> constraints;
	
	private GraphNode startNode;
	private GraphNode endNode;
	
	/**
	 * Copy other graph instance
	 * @param other
	 */
	public SolutionGraph(SolutionGraph other) {
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean isConsistent() {
		return true;
	}
	
	public boolean fixConstraints(){
		return true;
	}

}
