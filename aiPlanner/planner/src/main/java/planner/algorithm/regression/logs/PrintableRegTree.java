package planner.algorithm.regression.logs;

import java.util.Collection;
import java.util.HashSet;

import planner.algorithm.regression.RegTree;
import planner.algorithm.regression.TreeNode;
import planner.algorithm.strips.AtomicState;

public class PrintableRegTree {
	PrintableTreeNode root;
	
	public PrintableRegTree(RegTree tree) {		
		this.root = fillTree(tree.getRoot(), "");
	}

	private PrintableTreeNode fillTree(TreeNode srcNode, String parentAction){
		PrintableTreeNode node = new PrintableTreeNode(srcNode, parentAction);
		for (AtomicState s : srcNode.getAtomicStates()) {
			Collection<PrintableTreeNode> printableChildren = new HashSet<PrintableTreeNode>();
			TreeNode[] childrenNodes = srcNode.getChildrenFromState(s);
			
			for (TreeNode n : childrenNodes) {
				String parentActionName = n.getIncomingLink().getAction().toString();
				PrintableTreeNode childNode = fillTree(n, parentActionName);
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
