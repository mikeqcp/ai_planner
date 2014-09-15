package planner.algorithm.regression;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import planner.algorithm.regression.model.TreeNode;
import planner.model.AtomicState;
import planner.model.State;

public class WalkQueue {
	private Queue<TreeNode> nodesQueue;	//nodes waiting for being visited
	private Queue<AtomicState> statesQueue;	//states waiting for being regressed
	private State goal;
	
	public WalkQueue(TreeNode startNode, State initial) {
		this.nodesQueue = new LinkedList<TreeNode>();
		this.statesQueue = new LinkedList<AtomicState>();
		this.goal = initial;
		addNodeToVisit(startNode);
	}
	
	/**
	 * Enqueue states that need to be regressed
	 */
	public void enqueueNodeStates(TreeNode n){
		List<AtomicState> unsatisfied = new ArrayList<AtomicState>();
		
		//add satisfied first
		for (AtomicState s : n.getAtomicStates()) {
			if(!goal.satisfies(s))
				addStateToRegress(s);
			else
				unsatisfied.add(s);
		}
		
		//and then unsatisfied
		for (AtomicState s : unsatisfied) {
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
		TreeNode nextNode = nodesQueue.poll();
		nextNode.visit();
		return nextNode;
	}
	
	public boolean hasNodesToVisit(){
		return !nodesQueue.isEmpty();
	}
	
	public boolean hasStatesToVisit(){
		return !statesQueue.isEmpty();
	}
}
