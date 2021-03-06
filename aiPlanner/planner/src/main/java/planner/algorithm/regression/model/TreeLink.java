package planner.algorithm.regression.model;

import planner.model.Action;
import planner.model.AtomicState;

public class TreeLink {
	private TreeNode sourceNode;
	private AtomicState sourceState;
	private TreeNode targetNode;
	private Action linkAction;
	
	public TreeLink(TreeNode srcNode, AtomicState sourceState, Action action, TreeNode targetNode) {
		super();
		this.sourceState = sourceState;
		this.sourceNode = srcNode;
		this.linkAction = action;
		this.targetNode = targetNode;
	}

	public AtomicState getSourceState() {
		return sourceState;
	}

	public TreeNode getEffect() {
		return targetNode;
	}

	public TreeNode getSourceNode() {
		return sourceNode;
	}

	public TreeNode getTargetNode() {
		return targetNode;
	}
	
	public Action getAction(){
		return linkAction;
	}
}
