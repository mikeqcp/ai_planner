package planner.algorithm.regression;

public class TreeBuilder {
	private RegTree tree;
	private TreeWalker walker;
	
	public TreeBuilder(RegTree tree) {
		this.tree = tree;
		this.walker = new TreeWalker(tree.getRoot());
	}
	
	
}
