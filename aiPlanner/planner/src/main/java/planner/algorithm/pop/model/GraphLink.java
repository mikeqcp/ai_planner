package planner.algorithm.pop.model;

public abstract class GraphLink {
	protected GraphNode nodeFrom;
	protected GraphNode nodeTo;
	
	
	
	public GraphNode getNodeFrom() {
		return nodeFrom;
	}



	public void setNodeFrom(GraphNode nodeFrom) {
		this.nodeFrom = nodeFrom;
	}



	public GraphNode getNodeTo() {
		return nodeTo;
	}



	public void setNodeTo(GraphNode nodeTo) {
		this.nodeTo = nodeTo;
	}



	public abstract GraphLink clone();
}
