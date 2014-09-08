package planner.algorithm.regression.logs;

import java.util.Collection;
import java.util.HashSet;

import planner.algorithm.regression.TreeNode;

public class PrintableTreeNode {
	private String id;
	private Collection<PrintableNodeState> items;
	private TreeNode contextNode;
	
	public PrintableTreeNode(TreeNode node) {
		this.contextNode = node;
		items = new HashSet<PrintableNodeState>();
	}

	public String getId() {
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
}
