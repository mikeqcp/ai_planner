package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.model.AtomicState;

public class ConstraintProtector {
	private SolutionGraph graph;
	
	/**
	 * Protects passed link in graph.
	 * @param g
	 * @return Graph modified to satisfy link constraint
	 */
	public Set<SolutionGraph> protect(SolutionGraph g){
		this.graph = g;
		Set<SolutionGraph> protectedGraphs = new HashSet<SolutionGraph>();
		
		Set<Threat> possibleThreats = findPotentialThreats();
		Set<Threat> unprotected = selectUnprotectedThreats(possibleThreats);
		
		for (Threat t : unprotected) {
			Set<SolutionGraph> solutions = protectThreat(t);
			protectedGraphs.addAll(solutions);
		}
		
		return protectedGraphs;
	}
	
	private Set<SolutionGraph> protectThreat(Threat t) {
		Set<SolutionGraph> solutions = new HashSet<SolutionGraph>();
		
		SolutionGraph promoted = promoteToProtect(t);
		if(promoted.isConsistent()) solutions.add(promoted);
		
		SolutionGraph demoted = demoteToProtect(t);
		if(demoted.isConsistent()) solutions.add(demoted);
		
		return solutions;
	}
	
	private SolutionGraph promoteToProtect(Threat t){
		SolutionGraph g = new SolutionGraph(graph);
		
		GraphNode threateningNode = g.getNodeById(t.getNode().getId());
		GraphNode threatenedNode = g.getNodeById(t.getLink().getNodeFrom().getId());

		GraphLink protectiveLink = new OrderLink(threateningNode, threatenedNode);
		g.addLink(protectiveLink);
		
		return g;
	}
	
	private SolutionGraph demoteToProtect(Threat t){
		SolutionGraph g = new SolutionGraph(graph);
		
		GraphNode threateningNode = g.getNodeById(t.getNode().getId());
		GraphNode threatenedNode = g.getNodeById(t.getLink().getNodeTo().getId());

		GraphLink protectiveLink = new OrderLink(threatenedNode, threateningNode);
		g.addLink(protectiveLink);
		
		return g;
	}

	private Set<Threat> selectUnprotectedThreats(Set<Threat> all){
		Set<Threat> unprotected = new HashSet<Threat>();
		for (Threat t : all) {
			if(!isProtected(t)) unprotected.add(t);
		}
		return unprotected;
	}
	
	private boolean isProtected(Threat t){
		GraphNode threateningNode = t.getNode();
		
		boolean isDemoted = graph.hasLink(t.getLink().nodeTo, threateningNode);
		boolean isPromoted = graph.hasLink(threateningNode, t.getLink().getNodeFrom());
		
		return isDemoted || isPromoted;
	}
	
	private Set<Threat> findPotentialThreats(){
		Set<Threat> threats = new HashSet<Threat>();
		Set<CasualLink> allLinks = graph.getCasualLinks();
		for (CasualLink l : allLinks) {
			Set<Threat> linkThreats = findThreatsForLink(l);
			threats.addAll(linkThreats);
		}
		return threats;
	}
	
	private Set<Threat> findThreatsForLink(CasualLink link){
		Set<Threat> threats = new HashSet<Threat>();
		AtomicState subgoal = link.getAchieves();
		for (GraphNode n : graph.getAllNodes()) {
			if(n.getBindedAction() != null && n.getBindedAction().removes(subgoal))
				threats.add(new Threat(link, n));
		}
		return threats;
	}
}
