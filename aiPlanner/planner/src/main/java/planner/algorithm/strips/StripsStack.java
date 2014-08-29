package planner.algorithm.strips;

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
}
