package planner.algorithm.pop.model;

public class OrderLink extends GraphLink {

	public OrderLink(GraphNode nodeFrom, GraphNode nodeTo) {
		super(nodeFrom, nodeTo);
	}

	@Override
	public GraphLink clone() {
		return new OrderLink(nodeFrom, nodeTo);
	}

}
