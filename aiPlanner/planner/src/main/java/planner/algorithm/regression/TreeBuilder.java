package planner.algorithm.regression;

import planner.algorithm.strips.AtomicState;

public class TreeBuilder {
	private RegTree tree;
	private WalkQueue queue;
	
	public TreeBuilder(RegTree tree) {
		this.tree = tree;
		this.queue = new WalkQueue(tree.getRoot());
	}

	public WalkQueue getWalker() {
		return queue;
	}
	
	/**
	 * Generates next tree level, by regressing states in curren node
	 */
	public void generateNextLevel(){
		TreeNode node = queue.getNextNode();
		queue.enqueueNodeStates(node);
		
		while(queue.hasStatesToVisit()){
			AtomicState s = queue.getNextState();
			TreeNode[] newNodes = generateNodesForState(s, node);
			for (TreeNode n : newNodes) {
				queue.addNodeToVisit(n);
			}
		}
	}
	
	/**
	 * Generates nodes array, and links them with proper nodes in current tree
	 * @param s - state used to generate new nodes
	 * @param parentNode - s state owner
	 * @return
	 */
	private TreeNode[] generateNodesForState(AtomicState s, TreeNode parentNode){
		return null;
	}
}
