package planner.algorithm.regression.logs;

import java.util.Collection;
import java.util.HashSet;

import planner.algorithm.regression.RegTree;
import planner.algorithm.regression.TreeNode;
import planner.algorithm.strips.AtomicState;

public class PrintableRegTree {
	PrintableTreeNode root;
	
	public PrintableRegTree(RegTree tree) {		
		this.root = fillTree(tree.getRoot());
	}

	private PrintableTreeNode fillTree(TreeNode srcNode){
		PrintableTreeNode node = new PrintableTreeNode(srcNode);
		for (AtomicState s : srcNode.getAtomicStates()) {
			Collection<PrintableTreeNode> printableChildren = new HashSet<PrintableTreeNode>();
			TreeNode[] childrenNodes = srcNode.getChildrenFromState(s);
			
			for (TreeNode n : childrenNodes) {
				PrintableTreeNode childNode = fillTree(n);
				printableChildren.add(childNode);
			}
			
			node.addState(new PrintableNodeState(s.toString(), printableChildren.toArray(new PrintableTreeNode[0])));
		}
		return node;
	}
	
	public PrintableTreeNode getRoot() {
		return root;
	}
	
	
}
