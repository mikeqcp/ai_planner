package planner.algorithm.regression;

public class RegTree {
	private TreeNode root;
	
	public RegTree(RegState root) {
		this.root = new TreeNode(root);
	}
	
	public RegTree(RegTree other) {
		this.root = new TreeNode(root);
	}

	public TreeNode getRoot() {
		return root;
	}
	
	
}
