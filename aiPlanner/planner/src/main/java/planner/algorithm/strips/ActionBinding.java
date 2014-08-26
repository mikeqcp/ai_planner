package planner.algorithm.strips;

import java.util.HashMap;

import pddl4j.exp.term.Term;

public class ActionBinding {
	private HashMap<Term, String> binding; //parameter -> value
	
	public ActionBinding() {
		binding = new HashMap<Term, String>();
	}
	
	public ActionBinding(HashMap<Term, String> binding) {
		super();
		this.binding = binding;
	}

	public void addBinding(Term parameter, String value){
		binding.put(parameter, value);
	}
	
	public HashMap<Term, String> getBinding() {
		return binding;
	}
	
	
}
