package planner.algorithm.pop.model;

import java.util.Collection;
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
	private HashMap<Long,GraphNode> allNodes;

	/**
	 * Set of all outcoming links in graph
	 */
	private Map<Long, Set<GraphLink>> outcomingLinks;
	/**
	 * Set of all inoming links in graph
	 */
	private Map<Long, Set<GraphLink>> incomingLinks;

	/**
	 * Copy other graph instance
	 * 
	 * @param other
	 */
	public SolutionGraph(SolutionGraph other) {
		Map<GraphNode, GraphNode> nodeMappings = generateNodesCopies(other.allNodes.values());

		this.startNode = nodeMappings.get(other.startNode);
		this.endNode = nodeMappings.get(other.endNode);

		this.allNodes = new HashMap<Long, GraphNode>();
		for (GraphNode node : other.allNodes.values()) {
			GraphNode mappedNode = nodeMappings.get(node);
			this.allNodes.put(mappedNode.id, mappedNode);
		}

		// copy links
		outcomingLinks = new HashMap<Long, Set<GraphLink>>();
		Iterator<Entry<Long, Set<GraphLink>>> it = other.outcomingLinks
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, Set<GraphLink>> entry = (Entry<Long, Set<GraphLink>>) it
					.next();
			Set<GraphLink> links = new HashSet<GraphLink>();

			for (GraphLink l : entry.getValue()) {
				GraphLink newLink = l.clone(this);
				newLink.setNodeFrom(nodeMappings.get(l.getNodeFrom()));
				newLink.setNodeTo(nodeMappings.get(l.getNodeTo()));
				links.add(newLink);
			}
			outcomingLinks.put(entry.getKey(), links);
		}

		incomingLinks = new HashMap<Long, Set<GraphLink>>();
		it = other.incomingLinks.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, Set<GraphLink>> entry = (Entry<Long, Set<GraphLink>>) it
					.next();
			Set<GraphLink> links = new HashSet<GraphLink>();

			for (GraphLink l : entry.getValue()) {
				GraphLink newLink = l.clone(this);
				newLink.setNodeFrom(nodeMappings.get(l.getNodeFrom()));
				newLink.setNodeTo(nodeMappings.get(l.getNodeTo()));
				links.add(newLink);
			}
			incomingLinks.put(entry.getKey(), links);
		}

		this.unsatisfiedGoals = new RandomSet<SubGoal>();
		for (SubGoal g : other.unsatisfiedGoals) {
			this.unsatisfiedGoals.add(new SubGoal(g.getNode(), g.getGoal(this)));
		}
	}

	private Map<GraphNode, GraphNode> generateNodesCopies(
			Collection<GraphNode> oldNodes) {
		Map<GraphNode, GraphNode> nodeMappings = new HashMap<GraphNode, GraphNode>();
		for (GraphNode node : oldNodes) {
			nodeMappings.put(node, node.clone());
		}
		return nodeMappings;
	}

	public SolutionGraph(State initialState, State goal) {
		this.startNode = new StartNode(initialState);
		this.endNode = new EndNode(goal);
		this.unsatisfiedGoals = new RandomSet<SubGoal>();

		outcomingLinks = new HashMap<Long, Set<GraphLink>>();
		incomingLinks = new HashMap<Long, Set<GraphLink>>();

		allNodes = new HashMap<Long, GraphNode>();
		addNode(startNode);
		addNode(endNode);

		GraphLink initialLink = new OrderLink(startNode, endNode);
		addLink(initialLink);
	}

	public RandomSet<SubGoal> getUnsatisfiedGoals() {
		return unsatisfiedGoals;
	}

	public GraphNode getStartNode() {
		return startNode;
	}

	public GraphNode getEndNode() {
		return endNode;
	}

	public boolean isConsistent() {
		SolutionLinearizer linearizator = new SolutionLinearizer(this);
		return linearizator.isConsistent();
	}

	public GraphNode getNodeById(long id) {
		return allNodes.get(id);
	}

	public Collection<GraphNode> getAllNodes() {
		return allNodes.values();
	}

	public Set<GraphLink> getAllLinks() {
		Set<GraphLink> constraints = new HashSet<GraphLink>();
		for (Set<GraphLink> links : outcomingLinks.values()) {
			constraints.addAll(links);
		}
		return constraints;
	}

	public Set<CasualLink> getCasualLinks() {
		Set<GraphLink> allLinks = getAllLinks();
		Set<CasualLink> casualLinks = new HashSet<CasualLink>();
		for (GraphLink graphLink : allLinks) {
			if (graphLink instanceof CasualLink)
				casualLinks.add((CasualLink) graphLink);
		}
		return casualLinks;
	}

	public Set<GraphLink> getOutLinksFor(GraphNode n) {
		return outcomingLinks.get(n.getId());
	}

	public Set<GraphLink> getInLinksFor(GraphNode n) {
		return incomingLinks.get(n.getId());
	}

	public SubGoal nextGoalToSatisfy() {
		return unsatisfiedGoals.getRandomItem();
	}

	public boolean isComplete() {
		return unsatisfiedGoals.size() == 0;
	}

	public void addNode(GraphNode n) {
		allNodes.put(n.id, n);
		unsatisfiedGoals.addAll(n.getPreconditions());

		outcomingLinks.put(n.getId(), new HashSet<GraphLink>());
		incomingLinks.put(n.getId(), new HashSet<GraphLink>());
	}

	public void addLink(GraphLink link) {
		GraphNode fromNode = link.getNodeFrom();
		GraphNode toNode = link.getNodeTo();

		GraphLink[] existing = getLinks(fromNode, toNode);
		if (existing.length > 0) {
			if (link.isRedundantFor(existing))
				return;
		}
		if (fromNode.getId() == toNode.getId())
			return;

		outcomingLinks.get(fromNode.getId()).add(link);
		incomingLinks.get(toNode.getId()).add(link);
	}

	public void removeLink(GraphLink link) {
		GraphNode fromNode = link.getNodeFrom();
		GraphNode toNode = link.getNodeTo();

		outcomingLinks.get(fromNode.getId()).remove(link);
		incomingLinks.get(toNode.getId()).remove(link);
	}

	public boolean hasLink(GraphNode from, GraphNode to) {
		Set<GraphLink> outLinks = outcomingLinks.get(from.getId());
		for (GraphLink l : outLinks) {
			if (l.getNodeTo().getId() == to.getId())
				return true;
		}
		return false;
	}

	public GraphLink[] getLinks(GraphNode from, GraphNode to) {
		Set<GraphLink> matching = new HashSet<GraphLink>();
		Set<GraphLink> outLinks = outcomingLinks.get(from.getId());
		for (GraphLink l : outLinks) {
			if (l.getNodeTo().getId() == to.getId())
				matching.add(l);
		}
		return matching.toArray(new GraphLink[0]);
	}

	@Override
	public String toString() {
		return allNodes.toString();
	}
}
