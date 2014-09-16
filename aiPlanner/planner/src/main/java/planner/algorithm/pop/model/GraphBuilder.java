package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import planner.algorithm.Algorithm;
import planner.algorithm.strips.Utils;
import planner.model.AtomicState;
import planner.model.BindedAction;
import planner.model.ParameterBinding;

/**
 * @author MichalP
 *	Creates new nodes and links in graph, and chooses action to take
 */
public class GraphBuilder {
	private Algorithm parent;
	private SolutionGraph graph;
	private ConstraintProtector protector;
	
	public GraphBuilder(Algorithm parent) {
		this.parent = parent;
		this.protector = new ConstraintProtector();
	}
	
	/**
	 * @param goal
	 * @return All possible new solution graphs to satisfy goal, with constraints resolved
	 */
	public Set<SolutionGraph> satisfyGoal(SolutionGraph graph, SubGoal goal){
		this.graph = graph;
		Set<SolutionGraph> results = new LinkedHashSet<SolutionGraph>();
		AtomicState precondition = goal.getGoal();

		Set<GraphNode> applicableNodes = findNodesToSatisfy(precondition);
		for (GraphNode n : applicableNodes) {
			SolutionGraph updatedGraph = insertLinksForAction(n, goal);
			SolutionGraph protectedGraph = protector.protect(updatedGraph);
			if(protectedGraph != null) results.add(protectedGraph);
		}
		
		Set<BindedAction> applicable = Utils.findApplicableActions(precondition, parent.getActions(), parent.getConstants());
		for (BindedAction a : applicable) {
			GraphNode actionNode = new GraphNode(a);
			SolutionGraph updatedGraph = insertAction(actionNode, goal);
			SolutionGraph protectedGraph = protector.protect(updatedGraph);
			if(protectedGraph != null) results.add(protectedGraph);
		}
		
		return results;
	}
	
	private Set<GraphNode> findNodesToSatisfy(AtomicState goal){
		Set<GraphNode> nodes  = new HashSet<GraphNode>();
		for (GraphNode n : this.graph.getAllNodes()) {
			BindedAction a = n.getBindedAction();
			
			ParameterBinding binding = a == null ? null : a.bindToProduce(goal);
			
			if(binding != null){
				n.setBinding(binding);
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	private SolutionGraph insertLinksForAction(GraphNode node, SubGoal goal){
		SolutionGraph updated = new SolutionGraph(graph);
		GraphNode updatedNode = updated.getNodeById(node.getId());
		GraphNode updatedGoalNode = updated.getNodeById(goal.getNode().getId());
		
		GraphLink casualLink = new CasualLink(updatedNode, updatedGoalNode, goal.getGoal());
		updated.addLink(casualLink);
		
		GraphLink goalOrderLink = new OrderLink(updatedNode, updatedGoalNode);
		updated.addLink(goalOrderLink);
		
		return updated;
	}
	
	private SolutionGraph insertAction(GraphNode node, SubGoal goal){
		SolutionGraph updated = new SolutionGraph(graph);
		
		updated.addNode(node);
		GraphNode updatedGoalNode = updated.getNodeById(goal.getNode().getId());
		
		GraphLink casualLink = new CasualLink(node, updatedGoalNode, goal.getGoal());
		updated.addLink(casualLink);
		
		GraphLink goalOrderLink = new OrderLink(node, updatedGoalNode);
		updated.addLink(goalOrderLink);
		
		GraphLink startOrderLink = new OrderLink(updated.getStartNode(), node);
		updated.addLink(startOrderLink);
		
		GraphLink endOrderLink = new OrderLink(node, updated.getEndNode());
		updated.addLink(endOrderLink);
		
		return updated;
	}
}
