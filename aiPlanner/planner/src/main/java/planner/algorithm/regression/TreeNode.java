package planner.algorithm.regression;

import java.util.Arrays;
import java.util.Collection;
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
	
	private boolean belongsToFinalPlan = false;
	private boolean wasVisited = false; 
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void markFinal(boolean mark) {
		this.belongsToFinalPlan = mark;
	}

	public boolean isMarkedFinal() {
		return belongsToFinalPlan;
	}
	

	public boolean isVisited() {
		return wasVisited;
	}

	public void visit() {
		this.wasVisited = true;
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
		this.id = other.id;
	}
	
	/**
	 * Check if is consistent
	 */
	public boolean isConsistent(Set<Constraint> constraints, Set<Constant> paramValues){
		if(consistent == null)
			consistent = state.isConsistent(constraints, paramValues);
		return consistent;
	}
	
	/**
	 * Return previously computed value
	 */
	public boolean isConsistent(){
		return consistent!= null ? consistent : true;
	}
	
	public void markUnconsistent(){
		this.consistent = false;
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
	
	public TreeLink[] getLinksFromState(AtomicState s){
		Collection<TreeLink> links = new HashSet<TreeLink>();
		for (TreeLink l : getOutcomingLinks()) {
			if(l.getSourceState() == s)
				links.add(l);
		}
		return links.toArray(new TreeLink[0]);
	}
	
	public TreeNode[] getChildrenFromState(AtomicState s){
		TreeLink[] links = getLinksFromState(s);
		Collection<TreeNode> children = new HashSet<TreeNode>();
		for (TreeLink link : links) {
			children.add(link.getTargetNode());
		}
		return children.toArray(new TreeNode[0]);
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
}
