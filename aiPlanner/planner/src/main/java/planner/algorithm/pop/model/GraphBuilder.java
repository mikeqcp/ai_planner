package planner.algorithm.pop.model;

import java.util.HashSet;
import java.util.Set;

import planner.algorithm.Algorithm;
import planner.algorithm.strips.Utils;
import planner.model.AtomicState;
import planner.model.BindedAction;

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
		
		Set<SolutionGraph> results = new HashSet<SolutionGraph>();
		
		AtomicState precondition = goal.getGoal();
		Set<BindedAction> applicable = Utils.findApplicableActions(precondition, parent.getActions(), parent.getConstants());
		for (BindedAction a : applicable) {
			GraphNode actionNode = new GraphNode(a);
			SolutionGraph updatedGraph = insertAction(actionNode, goal);
			SolutionGraph protectedGraph = protector.protect(updatedGraph, actionNode);
			if(protectedGraph != null) results.add(protectedGraph);
		}
		
		return results;
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
		
		return updated;
	}
}
