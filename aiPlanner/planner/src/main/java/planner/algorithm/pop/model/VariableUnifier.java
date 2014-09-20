package planner.algorithm.pop.model;

import java.util.ArrayList;

import pddl4j.exp.term.Term;
import planner.model.AtomicState;
import planner.model.ParameterBinding;

public class VariableUnifier {
	private SolutionGraph graph;
	
	/**
	 * @param graph - context graph
	 * @param node - inserted node
	 */
	public boolean unifyVariables(SolutionGraph graph, CasualLink link){
		this.graph = graph;
		
		if (!unifyLinkVariables(link)) return false;
		refreshGraphGoals(graph);
		
		return true;
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
		GraphNode fromNode = link.nodeFrom;
		GraphNode toNode = link.nodeTo;
		
		if(toNode instanceof EndNode) return true;
		
		SubGoal goal = link.getSubgoal();
		
		ParameterBinding fromBinding = fromNode.getBinding();
		ParameterBinding toBinding = toNode.getBinding();
		
		String predicate = goal.getGoal().getPredicate();
		ArrayList<ArrayList<Term>> fromTermsList = fromNode.getBindedAction().getEffectParams(predicate);
		ArrayList<Term> toTerms = goal.getGoal().getParams();
		
		for (ArrayList<Term> fromTerms : fromTermsList) {
			ParameterBinding[] mergedBindings = unifyTermBinding(fromTerms, toTerms, fromBinding, toBinding);
			if(mergedBindings == null) continue;
			
			fromNode.setBinding(mergedBindings[0]);
			toNode.setBinding(mergedBindings[1]);
			
			if(!fromNode.binding.validate() || !toNode.binding.validate()) return false;
			
			link.getSubgoal().updateBinding(mergedBindings[1]);
			
			for (GraphLink l : graph.getOutLinksFor(toNode)) {
				if(l instanceof CasualLink){
					CasualLink cl = (CasualLink)l;
					if(!cl.getAchieves().hasUnbindedParams()) continue;
					
					if(!graph.isConsistent()) continue;
					if(!unifyLinkVariables(cl)) continue;
				}
			}
			
			return true;
		}
		return false;
	}

	private ParameterBinding[] unifyTermBinding(ArrayList<Term> fromTerms, ArrayList<Term> toTerms, ParameterBinding fromBinding, ParameterBinding toBinding){
		if(fromTerms.size() != toTerms.size()) return null;
		ParameterBinding[] bindings = new ParameterBinding[3];
		
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
