package planner.algorithm.regression;

import planner.algorithm.strips.AtomicState;

public class TreeLink {
	private TreeNode sourceNode;
	private AtomicState sourceState;
	private TreeNode targetNode;
	
	public TreeLink(TreeNode srcNode, AtomicState sourceState, TreeNode targetNode) {
		super();
		this.sourceState = sourceState;
		this.sourceNode = srcNode;
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
	
	
	
}
