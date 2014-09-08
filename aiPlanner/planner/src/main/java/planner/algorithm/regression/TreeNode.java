package planner.algorithm.regression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import pddl4j.exp.term.Constant;
import planner.algorithm.strips.AtomicState;
import planner.model.Action;
import planner.model.Constraint;
import planner.model.State;

public class TreeNode {
	private State state;
	private Set<AtomicState> atomicStates;
	private Set<TreeLink> outcomingLinks;
	private TreeLink incomingLink;
	private Boolean consistent;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TreeNode(State state) {
		this.state = state;
		this.atomicStates = new HashSet<AtomicState>(Arrays.asList(state.breakIntoAtomic()));
		this.outcomingLinks = new HashSet<TreeLink>();
	}
	
	public TreeNode(TreeNode other) {
		this.state = new State(other.state);
		this.atomicStates = new HashSet<AtomicState>(other.atomicStates);
		this.outcomingLinks = new HashSet<TreeLink>(other.outcomingLinks);
		this.incomingLink = other.incomingLink;
	}
	
	public boolean isConsistent(Set<Constraint> constraints, Set<Constant> paramValues){
		if(consistent == null)
			consistent = state.isConsistent(constraints, paramValues);
		return consistent;
	}
	
	public Set<AtomicState> getAtomicStates() {
		return atomicStates;
	}

	public State getState() {
		return state;
	}
	
	public TreeNode getParentNode(){
		return incomingLink.getSourceNode();
	}

	/**
	 * Creates link from atomic state in this node to target node
	 * @param src - source atomic state, used to create link
	 * @param target - target node
	 * @return created link
	 */
	public TreeLink addLink(AtomicState src, TreeNode target, Action action){
		TreeLink link = new TreeLink(this, src, action, target);
		this.outcomingLinks.add(link);
		target.incomingLink = link;
		return link;
	}

	public Set<TreeLink> getOutcomingLinks() {
		return outcomingLinks;
	}

	public TreeLink getIncomingLink() {
		return incomingLink;
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
}
