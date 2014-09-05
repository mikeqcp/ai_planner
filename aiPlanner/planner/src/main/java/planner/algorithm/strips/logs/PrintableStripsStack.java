package planner.algorithm.strips.logs;

import java.util.Stack;

import planner.algorithm.strips.StackItem;
import planner.algorithm.strips.StripsStack;
import planner.model.Action;

public class PrintableStripsStack{
	private Stack<PrintableStackItem> stack = new Stack<PrintableStackItem>();
	
	public PrintableStripsStack(StripsStack s) {
		Stack<StackItem> stack = s.getStack();
		for (StackItem si : stack) {
			this.stack.add(new PrintableStackItem(si));
		}
	}

	public Stack<PrintableStackItem> getStack() {
		return stack;
	}	
}
