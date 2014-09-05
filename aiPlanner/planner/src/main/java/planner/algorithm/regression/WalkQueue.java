package planner.algorithm.regression;

import java.util.LinkedList;
import java.util.Queue;

import planner.algorithm.strips.AtomicState;

public class WalkQueue {
	private Queue<TreeNode> nodesQueue;	//nodes waiting for being visited
	private Queue<AtomicState> statesQueue;	//states waiting for being regressed
	
	public WalkQueue(TreeNode startNode) {
		this.nodesQueue = new LinkedList<TreeNode>();
		this.statesQueue = new LinkedList<AtomicState>();
		addNodeToVisit(startNode);
	}
	
	/**
	 * Enqueue states that need to be regressed
	 */
	public void enqueueNodeStates(TreeNode n){
		for (AtomicState s : n.getAtomicStates()) {
			addStateToRegress(s);
		}
	}
	
	private void addStateToRegress(AtomicState s){
		statesQueue.add(s);
	}
	
	/**
	 * @return Next state that needs to be regressed
	 */
	public AtomicState getNextState(){
		return statesQueue.poll();
	}
	
	public void addNodeToVisit(TreeNode n){
		nodesQueue.add(n);
	}
	
	public TreeNode getNextNode(){
		return nodesQueue.poll();
	}
	
	public boolean hasNodesToVisit(){
		return nodesQueue.isEmpty();
	}
	
	public boolean hasStatesToVisit(){
		return statesQueue.isEmpty();
	}
}
