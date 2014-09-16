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
		
		unsatisfiedGoals.addAll(endNode.getPreconditions());
	}

	
	public GraphNode getStartNode() {
		return startNode;
	}

	public GraphNode getEndNode() {
		return endNode;
	}

	public boolean isConsistent() {
		return true;
	}

	public GraphNode getNodeById(long id){
		for (GraphNode n : allNodes) {
			if(n.getId() == id)
				return n;
		}
		return null;
	}
	
	public Set<GraphLink> getGraphConstraints(){
		Set<GraphLink> constraints = new HashSet<GraphLink>();
		for (Set<GraphLink> links : outcomingLinks.values()) {
			constraints.addAll(links);
		}
		return constraints;
	}
	
	public SubGoal nextGoalToSatisfy(){
		return unsatisfiedGoals.getRandomItem();
	}
	
	public boolean isComplete(){
		return unsatisfiedGoals.size() == 0;
	}

	public void addNode(GraphNode n){
		allNodes.add(n);
		
		outcomingLinks.put(n, new HashSet<GraphLink>());
		incomingLinks.put(n, new HashSet<GraphLink>());
	}
	
	public void addLink(GraphLink link){
		GraphNode fromNode = link.getNodeFrom();
		GraphNode toNode = link.getNodeTo();
		
		outcomingLinks.get(fromNode).add(link);
		incomingLinks.get(toNode).add(link);
	}

	public void removeLink(GraphLink link){
		GraphNode fromNode = link.getNodeFrom();
		GraphNode toNode = link.getNodeTo();
		
		outcomingLinks.get(fromNode).remove(link);
		incomingLinks.get(toNode).remove(link);
	}
}
