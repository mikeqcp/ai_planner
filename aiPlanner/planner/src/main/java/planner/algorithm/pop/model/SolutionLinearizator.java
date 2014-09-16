package planner.algorithm.pop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import planner.model.ResultPlan;

public class SolutionLinearizator {
	private SolutionGraph graph;
	
	public SolutionLinearizator(SolutionGraph graph) {
		super();
		this.graph = graph;
	}

	public boolean isConsistent(){
		return linearizeSolution() != null;
	}
	
	public ResultPlan linearizeSolution(){
		ResultPlan plan = new ResultPlan();
		List<GraphNode> planNodes = new ArrayList<GraphNode>();
		Set<GraphNode> nodes = new HashSet<GraphNode>(graph.getAllNodes());
		
		while(nodes.size() > 0){
			Iterator<GraphNode> nodesIter = nodes.iterator();
			GraphNode selectedNode = null;
			while(nodesIter.hasNext()){
				GraphNode n = nodesIter.next();
				
				if(canBeInserted(n, planNodes)){
					selectedNode = n;
					break;
				}
			}
			
			if(selectedNode == null) return null;
			planNodes.add(selectedNode);
			nodes.remove(selectedNode);
		}
		
		for (GraphNode sNode : planNodes) {
			plan.addNextStep(sNode.getBindedAction());
		}
		
		return plan;
	}
	
	private boolean canBeInserted(GraphNode n, List<GraphNode> planNodes){
		Set<GraphLink> nLinks = graph.getInLinksFor(n);
		for (GraphLink l : nLinks) {
			if(!planNodes.contains(l.getNodeFrom())) return false;
		}
		return true;
	}
}
