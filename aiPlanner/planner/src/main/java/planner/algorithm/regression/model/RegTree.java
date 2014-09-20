package planner.algorithm.regression.model;

import java.util.ArrayList;
import java.util.Collection;

import planner.model.ResultPlan;
import planner.model.State;

public class RegTree {
	private TreeNode root;
	
	public RegTree(State root) {
		this.root = new TreeNode(root);
	}
	
	public RegTree(RegTree other) {
		this.root = new TreeNode(root);
	}

	public TreeNode getRoot() {
		return root;
	}
	
	/**
	 * Returns path from node to root.
	 * If mark is true, marks visited nodes
	 */
	public ResultPlan findPlanForNode(TreeNode startNode){
		Collection<Integer> ids = new ArrayList<Integer>();
		
		ResultPlan plan = new ResultPlan();		
		TreeNode current = startNode;
		ids.add(startNode.getId());
		
		while(current != root){
			TreeLink stepLink = current.getIncomingLink();
			plan.addNextStep(stepLink.getAction());
			current = current.getParentNode();
			ids.add(current.getId());
		}
		
		plan.setPlanIds(ids.toArray(new Integer[0]));
		return plan;
	}
	
	public int getNodeLvl(TreeNode n){
		int lvl = 0;
		
		TreeNode current = n;
		while(current != root){
			lvl++;
			TreeLink stepLink = current.getIncomingLink();
			current = current.getParentNode();
		}
		return lvl;
	}
}
