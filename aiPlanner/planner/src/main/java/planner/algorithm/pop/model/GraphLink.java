package planner.algorithm.pop.model;

public abstract class GraphLink {
	protected GraphNode nodeFrom;
	protected GraphNode nodeTo;
	
	public GraphLink(GraphNode nodeFrom, GraphNode nodeTo) {
		super();
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
	}



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
	
	@Override
	public String toString() {
		return nodeFrom.toString() + " --> " + nodeTo.toString();
	}
}
