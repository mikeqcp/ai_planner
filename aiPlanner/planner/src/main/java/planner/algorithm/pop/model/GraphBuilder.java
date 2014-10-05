package planner.algorithm.pop.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
	private ThreatProtector protector;
	private VariableUnifier unifier;
	
	public GraphBuilder(Algorithm parent) {
		this.parent = parent;
		this.protector = new ThreatProtector();
		this.unifier = new VariableUnifier();
	}
	
	/**
	 * @param goal
	 * @return All possible new solution graphs to satisfy goal, with constraints resolved
	 */
	public Set<SolutionGraph> satisfyGoal(SolutionGraph g, SubGoal goal){
		this.graph = new SolutionGraph(g);
		Set<SolutionGraph> results = new LinkedHashSet<SolutionGraph>();
		AtomicState precondition = goal.getGoal(this.graph);

		Map<GraphNode, ParameterBinding> applicableNodes = findNodesToSatisfy(goal);
		for (GraphNode n : applicableNodes.keySet()) {
			SolutionGraph updatedGraph = insertLinksForNode(n, applicableNodes.get(n), goal);
			if(updatedGraph == null) continue;
			Set<SolutionGraph> protectedGraph = protector.protect(updatedGraph);
			for (SolutionGraph sol : protectedGraph) {
				if(sol != null) results.add(sol);
			}
		}
		
		Set<BindedAction> applicableActions = Utils.findApplicableActions(precondition, parent.getActions(), parent.getConstants(), false);
		for (BindedAction a : applicableActions) {
			GraphNode actionNode = new GraphNode(a);
			SolutionGraph updatedGraph = insertAction(actionNode, goal);
			if(updatedGraph == null) continue;
			Set<SolutionGraph> protectedGraph = protector.protect(updatedGraph);
			for (SolutionGraph sol : protectedGraph) {
				if(sol != null) results.add(sol);
			}
		}
		
		return results;
	}
	
	private Map<GraphNode, ParameterBinding> findNodesToSatisfy(SubGoal subgoal){
		AtomicState goal = subgoal.getGoal(this.graph);
		
		Map<GraphNode, ParameterBinding> nodes  = new HashMap<GraphNode, ParameterBinding>();
		for (GraphNode n : this.graph.getAllNodes()) {
			if(n.idEquals(subgoal.getNode())) continue;
			
			ParameterBinding binding = n.bindToProduce(goal);
			
			if(binding != null){
				nodes.put(n, binding);
			}
		}
		return nodes;
	}
	
	private SolutionGraph insertLinksForNode(GraphNode node, ParameterBinding binding, SubGoal goal){
		SolutionGraph updated = new SolutionGraph(graph);
		GraphNode updatedNode = updated.getNodeById(node.getId());
		GraphNode updatedGoalNode = updated.getNodeById(goal.getNode().getId());
		
		updatedNode.mergeBinding(binding);
		
		GraphLink goalOrderLink = new OrderLink(updatedNode, updatedGoalNode);
		updated.addLink(goalOrderLink);
		
		CasualLink casualLink = new CasualLink(updated, updatedNode, updatedGoalNode, goal);
		updated.addLink(casualLink);
		
		SolutionGraph unified = unifier.unifyVariables(updated, casualLink);	
		return unified;
	}
	
	private SolutionGraph insertAction(GraphNode node, SubGoal goal){
		SolutionGraph updated = new SolutionGraph(graph);
		
		updated.addNode(node);
		GraphNode updatedGoalNode = updated.getNodeById(goal.getNode().getId());
		
		GraphLink goalOrderLink = new OrderLink(node, updatedGoalNode);
		updated.addLink(goalOrderLink);
		
		GraphLink startOrderLink = new OrderLink(updated.getStartNode(), node);
		updated.addLink(startOrderLink);
		
		GraphLink endOrderLink = new OrderLink(node, updated.getEndNode());
		updated.addLink(endOrderLink);
		
		CasualLink casualLink = new CasualLink(updated, node, updatedGoalNode, goal);
		updated.addLink(casualLink);
		
		SolutionGraph unified = unifier.unifyVariables(updated, casualLink);
		return unified;
	}
}
