package planner.algorithm.pop.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pddl4j.exp.term.Term;
import planner.model.ParameterBinding;

public class VariableUnifier {
	private SolutionGraph graph;
	
	/**
	 * @param graph - context graph
	 * @param node - inserted node
	 */
	public SolutionGraph unifyVariables(SolutionGraph graph, CasualLink link){
		this.graph = new SolutionGraph(graph);
		
		List<GraphLink> visited = new ArrayList<GraphLink>();
		Queue<GraphLink> queue = new LinkedList<GraphLink>();
		queue.add(link);
		while(!queue.isEmpty()){
			GraphLink l = queue.poll();
			if(visited.contains(l)) continue;
			visited.add(l);
			if(l instanceof CasualLink){
				CasualLink cl = (CasualLink)l;
				if(!cl.getNodeFrom().hasUnbindedParams() && !cl.getNodeTo().hasUnbindedParams()) continue;
				
				queue.addAll(graph.getOutLinksFor(cl.getNodeTo()));
				queue.addAll(graph.getInLinksFor(cl.getNodeFrom()));
				
				if(!unifyLinkVariables(cl)) return null;;
			}
		}
		
		refreshGraphGoals(graph);
		
		return this.graph;
	}
	
	private void refreshGraphGoals(SolutionGraph g) {
		for (SubGoal goal : g.getUnsatisfiedGoals()) {
			GraphNode oldNode = goal.getNode();
			GraphNode node = g.getNodeById(oldNode.id);
			ParameterBinding currentBinding = node.getBinding();
			goal.updateBinding(currentBinding);
		}
	}

	private boolean unifyLinkVariables(CasualLink link){
		GraphNode fromNode = graph.getNodeById(link.nodeFrom.getId());
		GraphNode toNode = graph.getNodeById(link.nodeTo.getId());
		
		if(toNode instanceof EndNode) return true;
		if(!fromNode.binding.validate() || !toNode.binding.validate()) return false;
		
		SubGoal goal = link.getSubgoal();
		
		ParameterBinding fromBinding = fromNode.getBinding();
		ParameterBinding toBinding = toNode.getBinding();
		
		String predicate = goal.getGoal(graph).getPredicate();
		ArrayList<ArrayList<Term>> fromTermsList = fromNode.getBindedAction().getEffectParams(predicate);
		ArrayList<Term> toTerms = goal.getGoal(graph).getParams();
		
		for (ArrayList<Term> fromTerms : fromTermsList) {
			ParameterBinding[] mergedBindings = unifyTermBinding(fromTerms, toTerms, fromBinding, toBinding);
			if(mergedBindings == null) continue;
			
			fromNode.setBinding(mergedBindings[0]);
			toNode.setBinding(mergedBindings[1]);
			
			if(!fromNode.binding.validate() || !toNode.binding.validate()) return false;
			
			link.getSubgoal().updateBinding(mergedBindings[1]);
			
			if(!graph.isConsistent()) return false;
		}
		return true;
	}

	private ParameterBinding[] unifyTermBinding(ArrayList<Term> fromTerms, ArrayList<Term> toTerms, ParameterBinding fromBinding, ParameterBinding toBinding){
		if(fromTerms.size() != toTerms.size()) return null;
		ParameterBinding[] bindings = new ParameterBinding[2];
		
		bindings[0] = fromBinding;
		bindings[1] = toBinding;
		
		for (int i = 0; i < fromTerms.size(); i++) {
			Term a = fromTerms.get(i);
			Term b = toTerms.get(i);
			
			boolean isAFree = isFree(a, fromBinding);
			boolean isBFree = isFree(b, toBinding);
			
			if(isAFree && !isBFree){
				bindings[0].addBinding(a, getBindingFor(b, toBinding));
			} else if(!isAFree && isBFree){
				bindings[1].addBinding(b, getBindingFor(a, fromBinding));
			}
			if(!isAFree && !isBFree && getBindingFor(a, fromBinding) != getBindingFor(b, toBinding))
				return null;
		}
		
		return bindings;
	}

	private String getBindingFor(Term t, ParameterBinding binding){
		if(t.getImage().startsWith("?"))
			return binding.getBindingFor(t);
		else
			return t.getImage();
	}
	
	private boolean isFree(Term t, ParameterBinding binding){
		return binding.getBindingFor(t) == null && t.getImage().startsWith("?");
	}
}
