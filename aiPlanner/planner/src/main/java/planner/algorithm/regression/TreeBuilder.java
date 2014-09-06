package planner.algorithm.regression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import planner.algorithm.strips.AtomicState;
import planner.algorithm.strips.BindedAction;
import planner.algorithm.strips.StripsUtils;
import planner.model.Action;
import planner.model.State;

public class TreeBuilder {
//	private RegTree tree;
	private WalkQueue queue;
	private Set<Action> actions = new HashSet<Action>();
	
	public TreeBuilder(RegTree tree, State initial) {
//		this.tree = tree;
		this.queue = new WalkQueue(tree.getRoot(), initial);
	}

	public WalkQueue getWalker() {
		return queue;
	}
	
	
	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	/**
	 * Generates next tree level, by regressing states in current node
	 */
	public void generateNextLevel(TreeNode node){
		queue.enqueueNodeStates(node);
		
		while(queue.hasStatesToVisit()){
			AtomicState s = queue.getNextState();
			Collection<TreeNode> newNodes = generateNodesForState(s, node);
			for (TreeNode n : newNodes) {
				queue.addNodeToVisit(n);
			}
		}
	}
	
	/**
	 * Generates nodes array, and links them with proper nodes in current tree
	 * @param srcState - state used to generate new nodes
	 * @param parentNode - s state owner
	 * @return
	 */
	private Collection<TreeNode> generateNodesForState(AtomicState srcState, TreeNode parentNode){
		Set<TreeNode> nodes = new HashSet<TreeNode>();
		
		Set<BindedAction> applicable = StripsUtils.findApplicableActions(srcState, actions);
		for (BindedAction action : applicable) {
			TreeNode newNode = generateNewState(parentNode.getState(), action, srcState);
			parentNode.addLink(srcState, newNode, action);
			nodes.add(newNode);
		}
		return nodes;
	}
	
	private TreeNode generateNewState(State oldState, BindedAction action, AtomicState srcState){
		AtomicState[] preconditions = action.getBindedPreconditions();
		AtomicState[] effects = action.getBindedEffects();
		
		State generatedState = new State(oldState);
		generatedState = generatedState.removeTerm(srcState);
		for (AtomicState s : preconditions) {
			generatedState = generatedState.addTerm(s);
		}
		return new TreeNode(generatedState);
	}
}
