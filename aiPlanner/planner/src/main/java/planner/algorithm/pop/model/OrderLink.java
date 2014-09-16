package planner.algorithm.pop.model;

public class OrderLink extends GraphLink {

	@Override
	public GraphLink clone() {
		OrderLink link = new OrderLink();
		link.setNodeFrom(this.nodeFrom);
		link.setNodeTo(this.nodeTo);
		return link;
	}

}
