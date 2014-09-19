package planner.algorithm.pop.logs;

import java.util.HashSet;
import java.util.Set;

import planner.algorithm.pop.model.GraphLink;
import planner.algorithm.pop.model.GraphNode;
import planner.algorithm.pop.model.SolutionGraph;
import planner.algorithm.pop.model.SubGoal;

public class PrintableGraph {
	private Set<PrintableNode> nodes;
	private Set<PrintableLink> links;
	private String goals = "";
	
	public PrintableGraph(SolutionGraph graph) {	
		nodes = new HashSet<PrintableNode>();
		links = new HashSet<PrintableLink>();
		
		for (GraphNode n : graph.getAllNodes()) {
			nodes.add(new PrintableNode(n));
		}
		for (GraphLink l : graph.getAllLinks()) {
			links.add(new PrintableLink(l));
		}
		links = mergeLinks(links);
		for (SubGoal sg : graph.getUnsatisfiedGoals()) {
			if(sg != null && sg.getGoal() != null)
				goals += sg.getGoal().toString() + ", ";
		}
	}

	private Set<PrintableLink> mergeLinks(Set<PrintableLink> links){
		Set<PrintableLink> merged = new HashSet<PrintableLink>();
		for (PrintableLink l : links) {
			PrintableLink mergedLink = getLink(l.getIdFrom(), l.getIdTo(), merged);
			if(mergedLink == null) {
				merged.add(l);
			} else {
				String mergedLabel = mergedLink.getParam();
				String otherLabel = l.getParam();
				String joiner = "";
				if(!otherLabel.equals(""))
					joiner = (mergedLabel != "") ? " , " : ""; 
					mergedLabel = mergedLabel + joiner + otherLabel;
				mergedLink.setParam(mergedLabel);
			}
		}
		return merged;
	}
	
	private PrintableLink getLink(long idFrom, long idTo, Set<PrintableLink> links){
		for (PrintableLink l : links) {
			if(l.getIdFrom() == idFrom && l.getIdTo() == idTo)
				return l;
		}
		return null;
	}
	
	public Set<PrintableNode> getNodes(){
		return nodes;
	}

	public Set<PrintableLink> getLinks() {
		return links;
	}

	public String getGoals() {
		return goals;
	}
	
	
}
