package planner.algorithm.regression;

import java.util.ArrayList;
import java.util.List;

import planner.model.Action;
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
	
	public ResultPlan findPlanForNode(TreeNode startNode){
		ResultPlan plan = new ResultPlan();		
		TreeNode current = startNode;
		while(current != root){
			TreeLink stepLink = current.getIncomingLink();
			plan.addNextStep(stepLink.getAction());
			current = current.getParentNode();
		}
		return plan;
	}
}
