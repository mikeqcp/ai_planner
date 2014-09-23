package planner.algorithm.pop.model;

public class OrderLink extends GraphLink {

	public OrderLink(GraphNode nodeFrom, GraphNode nodeTo) {
		super(nodeFrom, nodeTo);
	}

	@Override
	public GraphLink clone(SolutionGraph g) {
		return new OrderLink(nodeFrom, nodeTo);
	}

	@Override
	public boolean isRedundantFor(GraphLink[] existing) {
		return existing.length > 0;
	}

}
