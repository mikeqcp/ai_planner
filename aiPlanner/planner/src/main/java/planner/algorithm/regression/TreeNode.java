package planner.algorithm.regression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import planner.algorithm.strips.AtomicState;

public class TreeNode {
	private RegState state;
	private Set<AtomicState> atomicStates;
	private Set<TreeLink> outcomingLinks;
	private Set<TreeLink> incomingLinks;
	private Boolean consistent;
	
	public TreeNode(RegState state) {
		this.state = state;
		this.atomicStates = new HashSet<AtomicState>(Arrays.asList(state.breakIntoTerms()));
		this.outcomingLinks = new HashSet<TreeLink>();
		this.incomingLinks = new HashSet<TreeLink>();
	}
	
	public TreeNode(TreeNode other) {
		this.state = new RegState(other.state);
		this.atomicStates = new HashSet<AtomicState>(other.atomicStates);
		this.outcomingLinks = new HashSet<TreeLink>(other.outcomingLinks);
		this.incomingLinks = new HashSet<TreeLink>(other.incomingLinks);
	}
	
	public boolean isConsistent(){
		if(consistent == null)
			consistent = state.isConsistent();
		return consistent;
	}
	
	public Set<AtomicState> getAtomicStates() {
		return atomicStates;
	}

	public RegState getState() {
		return state;
	}

	/**
	 * Creates link from atomic state in this node to target node
	 * @param src - source atomic state, used to create link
	 * @param target - target node
	 * @return created link
	 */
	public TreeLink addLink(AtomicState src, TreeNode target){
		TreeLink link = new TreeLink(this, src, target);
		this.outcomingLinks.add(link);
		target.incomingLinks.add(link);
		return link;
	}
}
