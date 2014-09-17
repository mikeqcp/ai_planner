package planner.algorithm.pop.model;

public class Threat {
	private CasualLink link;
	private GraphNode node;
	
	public Threat(CasualLink link, GraphNode node) {
		super();
		this.link = link;
		this.node = node;
	}
	public CasualLink getLink() {
		return link;
	}
	public GraphNode getNode() {
		return node;
	}
}
