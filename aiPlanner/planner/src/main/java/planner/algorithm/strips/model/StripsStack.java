package planner.algorithm.strips.model;

import java.util.Stack;

public class StripsStack {
	private Stack<StackItem> stack = new Stack<StackItem>();

	public StripsStack(){};
	
	public StripsStack(StripsStack otherStack) {
		this.stack.addAll(otherStack.getStack());
	}

	public Stack<StackItem> getStack() {
		return stack;
	}
	
	public void push(StackItem item){
		stack.push(item);
	}
	
	public StackItem pop(){
		return stack.pop();
	}
	
	public StackItem peek(){
		return stack.peek();
	}
	
	public boolean isEmpty(){
		return stack.isEmpty();
	}
	
	@Override
	public String toString() {
		String s = "---";
		
		for (StackItem i : stack) {
			s += "\n";
			s += i.toString();
		}
		
		s+= "\n---";
		
		return s;
	}

	public int getPlanLength() {
		int actionsCount = 0;
		for (StackItem item : stack) {
			actionsCount += item.isActionType() ? 1 : 0;
		}
		return actionsCount;
	}
}
