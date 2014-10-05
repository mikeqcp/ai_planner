package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import planner.model.AtomicState;

public class ThreatProtector {
	private SolutionGraph graph;
	
	/**
	 * Protects passed link in graph.
	 * @param g
	 * @return Graph modified to satisfy link constraint
	 */
	public Set<SolutionGraph> protect(SolutionGraph g){
		this.graph = g;
		Queue<SolutionGraph> queue = new LinkedList<SolutionGraph>();
		if(g.isConsistent())
			queue.add(g);
		
		Set<Threat> possibleThreats = findPotentialThreats();
		Set<Threat> unprotected = selectUnprotectedThreats(possibleThreats);
		
		for (Threat t : unprotected) {
			Set<SolutionGraph> protectedGraphs = new HashSet<SolutionGraph>();
			for (SolutionGraph solutionGraph : queue) {
				Set<SolutionGraph> solutions = protectThreat(t, solutionGraph);
				protectedGraphs.addAll(solutions);
			}
			queue = new LinkedList<SolutionGraph>(protectedGraphs);
		}
		
//		if(unprotected.size() == 0)
//			queue.add(g);
		
		Iterator<SolutionGraph> iter = queue.iterator();
		while(iter.hasNext()){
			SolutionGraph iterGraph = iter.next();
			if(!iterGraph.isConsistent())
				iter.remove();
		}
		
		return new HashSet<SolutionGraph>(queue);
	}
	
	private Set<SolutionGraph> protectThreat(Threat t, SolutionGraph g) {
		Set<SolutionGraph> solutions = new HashSet<SolutionGraph>();
		
		SolutionGraph promoted = promoteToProtect(t, g);
		if(promoted != null && promoted.isConsistent()) solutions.add(promoted);
		
		SolutionGraph demoted = demoteToProtect(t, g);
		if(demoted != null && demoted.isConsistent()) solutions.add(demoted);
		
		return solutions;
	}
	
	private SolutionGraph promoteToProtect(Threat t, SolutionGraph graph){
		SolutionGraph g = new SolutionGraph(graph);
		
		GraphNode threateningNode = g.getNodeById(t.getNode().getId());
		GraphNode threatenedNode = g.getNodeById(t.getLink().getNodeFrom().getId());
		
		if(threatenedNode instanceof StartNode) return null;

		GraphLink protectiveLink = new OrderLink(threateningNode, threatenedNode);
		g.addLink(protectiveLink);
		
		return g;
	}
	
	private SolutionGraph demoteToProtect(Threat t, SolutionGraph graph){
		SolutionGraph g = new SolutionGraph(graph);
		
		GraphNode threateningNode = g.getNodeById(t.getNode().getId());
		GraphNode threatenedNode = g.getNodeById(t.getLink().getNodeTo().getId());

		if(threatenedNode instanceof EndNode) return null;
		
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
		boolean isPromoted = graph.hasLink(threateningNode, t.getLink().nodeFrom);
		
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
//			if(n.getBindedAction() != null && n.getBindedAction().canRemove(subgoal)){
			if(n.getBindedAction() != null && !n.hasUnbindedParams() && !subgoal.hasUnbindedParams() && n.getBindedAction().removes(subgoal)){
				if((link.getNodeTo().getId() != n.getId()) && (link.getNodeFrom().getId() != n.getId())){
					threats.add(new Threat(link, n));
				}
			}
		}
		return threats;
	}
}
