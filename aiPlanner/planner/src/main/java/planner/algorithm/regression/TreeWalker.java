package planner.algorithm.regression;

import java.util.LinkedList;
import java.util.Queue;

public class TreeWalker {
	private Queue<TreeNode> nodesQueue;	//waiting to being visited
	
	public TreeWalker(TreeNode startNode) {
		this.nodesQueue = new LinkedList<TreeNode>();
		addNodeToVisit(startNode);
	}
	
	public void addNodeToVisit(TreeNode n){
		nodesQueue.add(n);
	}
	
	public TreeNode getNextNode(){
		return nodesQueue.poll();
	}
	
	public boolean isEmpty(){
		return nodesQueue.isEmpty();
	}
}
