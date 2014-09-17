package planner.algorithm.pop.logs;

import java.util.HashSet;
import java.util.Set;

import planner.algorithm.pop.model.GraphLink;
import planner.algorithm.pop.model.GraphNode;
import planner.algorithm.pop.model.SolutionGraph;

public class PrintableGraph {
	private Set<PrintableNode> nodes;
	private Set<PrintableLink> links;
	
	public PrintableGraph(SolutionGraph graph) {	
		nodes = new HashSet<PrintableNode>();
		links = new HashSet<PrintableLink>();
		
		for (GraphNode n : graph.getAllNodes()) {
			nodes.add(new PrintableNode(n));
		}
		for (GraphLink l : graph.getAllLinks()) {
			links.add(new PrintableLink(l));
		}
	}

	
	public Set<PrintableNode> getNodes(){
		return nodes;
	}

	public Set<PrintableLink> getLinks() {
		return links;
	}
	
	
}
