package planner.algorithm.pop.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import planner.model.State;

public class SolutionGraph {
	private RandomSet<SubGoal> unsatisfiedGoals;
	
	private GraphNode startNode;
	private GraphNode endNode;
	private Set<GraphNode> allNodes;
	
	/**
	 * Set of all outcoming links in graph
	 */
	private Map<GraphNode, Set<GraphLink>> outcomingLinks;
	/**
	 * Set of all inoming links in graph
	 */
	private Map<GraphNode, Set<GraphLink>> incomingLinks;
	
	/**
	 * Copy other graph instance
	 * @param other
	 */
	public SolutionGraph(SolutionGraph other) {
		Map<GraphNode, GraphNode> nodeMappings = generateNodesCopies(other.allNodes);
		
		this.startNode = nodeMappings.get(other.startNode);
		this.endNode = nodeMappings.get(other.endNode);
		
		this.allNodes = new HashSet<GraphNode>();
		for (GraphNode node : other.allNodes) {
			GraphNode mappedNode = nodeMappings.get(node);
			this.allNodes.add(mappedNode);
		}
		
		//copy links
		outcomingLinks = new HashMap<GraphNode, Set<GraphLink>>();
		Iterator<Entry<GraphNode, Set<GraphLink>>> it = other.outcomingLinks.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<GraphNode, Set<GraphLink>> entry = (Entry<GraphNode, Set<GraphLink>>)it.next();
	        GraphNode n = entry.getKey();
	        Set<GraphLink> links = new HashSet<GraphLink>();
	        
	        for (GraphLink l : entry.getValue()) {
	        	l.getNodeTo().toString();
				GraphLink newLink = l.clone();
				newLink.setNodeFrom(nodeMappings.get(l.getNodeFrom()));
				newLink.setNodeTo(nodeMappings.get(l.getNodeTo()));
	        	links.add(newLink);
			}
	        outcomingLinks.put(nodeMappings.get(n), links);
	    }
	    
	    incomingLinks = new HashMap<GraphNode, Set<GraphLink>>();
	    it = other.incomingLinks.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<GraphNode, Set<GraphLink>> entry = (Entry<GraphNode, Set<GraphLink>>)it.next();
	        GraphNode n = entry.getKey();
	        Set<GraphLink> links = new HashSet<GraphLink>();
	        
	        for (GraphLink l : entry.getValue()) {
				GraphLink newLink = l.clone();
				newLink.setNodeFrom(nodeMappings.get(l.getNodeFrom()));
				newLink.setNodeTo(nodeMappings.get(l.getNodeTo()));
	        	links.add(newLink);
			}
	        incomingLinks.put(nodeMappings.get(n), links);
	    }
		
		this.unsatisfiedGoals = new RandomSet<SubGoal>(other.unsatisfiedGoals);
	}
	
	private Map<GraphNode, GraphNode> generateNodesCopies(Set<GraphNode> oldNodes){
		Map<GraphNode, GraphNode> nodeMappings = new HashMap<GraphNode, GraphNode>();
		for (GraphNode node : oldNodes) {
			nodeMappings.put(node, node.clone());
		}
		return nodeMappings;
	}
	
	public SolutionGraph(State initialState, State goal) {
		this.endNode = new EndNode(goal);
		this.startNode = new StartNode(initialState);
		this.unsatisfiedGoals = new RandomSet<SubGoal>();
		
		outcomingLinks = new HashMap<GraphNode, Set<GraphLink>>();
		incomingLinks = new HashMap<GraphNode, Set<GraphLink>>();
		
		allNodes = new HashSet<GraphNode>();
		addNode(startNode);
		addNode(endNode);
	}

	
	public GraphNode getStartNode() {
		return startNode;
	}

	public GraphNode getEndNode() {
		return endNode;
	}

	public boolean isConsistent() {
		SolutionLinearizator linearizator = new SolutionLinearizator(this);
		return linearizator.isConsistent();
	}

	public GraphNode getNodeById(long id){
		for (GraphNode n : allNodes) {
			if(n.getId() == id)
				return n;
		}
		return null;
	}
	
	public Set<GraphNode> getAllNodes() {
		return allNodes;
	}

	public Set<GraphLink> getGraphConstraints(){
		Set<GraphLink> constraints = new HashSet<GraphLink>();
		for (Set<GraphLink> links : outcomingLinks.values()) {
			constraints.addAll(links);
		}
		return constraints;
	}
	
	public Set<CasualLink> getCasualLinks(){
		Set<GraphLink> allLinks = getGraphConstraints();
		Set<CasualLink> casualLinks = new HashSet<CasualLink>();
		for (GraphLink graphLink : allLinks) {
			if(graphLink instanceof CasualLink)
				casualLinks.add((CasualLink) graphLink);
		}
		return casualLinks;
	}
	
	public Set<GraphLink> getOutLinksFor(GraphNode n){
		return outcomingLinks.get(n);
	}
	
	public Set<GraphLink> getInLinksFor(GraphNode n){
		return incomingLinks.get(n);
	}
	
	public SubGoal nextGoalToSatisfy(){
		return unsatisfiedGoals.getRandomItem();
	}
	
	public boolean isComplete(){
		return unsatisfiedGoals.size() == 0;
	}

	public void addNode(GraphNode n){
		allNodes.add(n);
		unsatisfiedGoals.addAll(n.getPreconditions());
		
		outcomingLinks.put(n, new HashSet<GraphLink>());
		incomingLinks.put(n, new HashSet<GraphLink>());
	}
	
	public void addLink(GraphLink link){
		GraphNode fromNode = link.getNodeFrom();
		GraphNode toNode = link.getNodeTo();
		
		GraphLink[] existing = getLinks(fromNode, toNode);
		if(existing.length > 0){
			if(link.isRedundantFor(existing)) return;
		}
		if(fromNode == toNode) return;
		
		outcomingLinks.get(fromNode).add(link);
		incomingLinks.get(toNode).add(link);
	}

	public void removeLink(GraphLink link){
		GraphNode fromNode = link.getNodeFrom();
		GraphNode toNode = link.getNodeTo();
		
		outcomingLinks.get(fromNode).remove(link);
		incomingLinks.get(toNode).remove(link);
	}

	public boolean hasLink(GraphNode from, GraphNode to){
		Set<GraphLink> outLinks = outcomingLinks.get(from);
		for (GraphLink l : outLinks) {
			if(l.getNodeTo() == to) return true;
		}
		return false;
	}
	
	public GraphLink[] getLinks(GraphNode from, GraphNode to){
		Set<GraphLink> matching =  new HashSet<GraphLink>();
		Set<GraphLink> outLinks = outcomingLinks.get(from);
		for (GraphLink l : outLinks) {
			if(l.getNodeTo() == to)
				matching.add(l);
		}
		return matching.toArray(new GraphLink[0]);
	}
	
	@Override
	public String toString() {
		return allNodes.toString();
	}
}
