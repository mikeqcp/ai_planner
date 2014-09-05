package planner.algorithm.regression;

import java.util.LinkedList;
import java.util.Queue;

import planner.algorithm.strips.AtomicState;

public class TreeWalker {
	private Queue<TreeNode> nodesQueue;	//nodes waiting for being visited
	private Queue<AtomicState> statesQueue;	//states waiting for being regressed
	
	public TreeWalker(TreeNode startNode) {
		this.nodesQueue = new LinkedList<TreeNode>();
		this.statesQueue = new LinkedList<AtomicState>();
		addNodeToVisit(startNode);
		for (AtomicState s : startNode.getAtomicStates()) {
			addStateToRegress(s);
		}
	}
	
	public void addStateToRegress(AtomicState s){
		statesQueue.add(s);
	}
	
	public AtomicState getNextState(){
		return statesQueue.poll();
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
