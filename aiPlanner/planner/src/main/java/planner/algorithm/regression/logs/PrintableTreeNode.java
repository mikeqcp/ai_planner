package planner.algorithm.regression.logs;

import java.util.Collection;
import java.util.HashSet;

import planner.algorithm.regression.model.TreeNode;

public class PrintableTreeNode {
	private int id;
	private Collection<PrintableNodeState> items;
	private TreeNode contextNode;
	private String parentAction;
	private boolean wasVisited;
	
	public PrintableTreeNode(TreeNode node, String parentAction) {
		this.contextNode = node;
		this.parentAction = parentAction;
		items = new HashSet<PrintableNodeState>();
		this.id = node.getId();
		this.wasVisited = node.isVisited();
	}

	public int getId() {
		return id;
	}

	public PrintableNodeState[] getItems() {
		return items.toArray(new PrintableNodeState[0]);
	}
	
	public void addState(PrintableNodeState s){
		items.add(s);
	}
	
	public boolean isValid(){
		return contextNode.isConsistent();
	}

	public String getParentAction() {
		return parentAction;
	}

	public boolean isVisited() {
		return wasVisited;
	}
	
	
}
