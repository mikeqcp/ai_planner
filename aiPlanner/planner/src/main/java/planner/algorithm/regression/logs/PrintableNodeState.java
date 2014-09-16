package planner.algorithm.regression.logs;

public class PrintableNodeState {

	private String label;
	private PrintableTreeNode[] children;
	
	public PrintableNodeState(String label, PrintableTreeNode[] children) {
		super();
		this.label = label;
		this.children = children;
	}

	public String getLabel() {
		return label;
	}

	public PrintableTreeNode[] getChildren() {
		return children;
	}

	
	
}
