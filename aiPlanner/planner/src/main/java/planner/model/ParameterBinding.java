package planner.model;

import java.util.HashMap;

import pddl4j.exp.term.Term;

public class ParameterBinding {
	private HashMap<Term, String> binding; //parameter -> value
	
	public ParameterBinding() {
		binding = new HashMap<Term, String>();
	}
	
	public ParameterBinding(HashMap<Term, String> binding) {
		super();
		this.binding = binding;
	}

	public void addBinding(Term parameter, String value){
		binding.put(parameter, value);
	}
	
	public HashMap<Term, String> getBinding() {
		return binding;
	}
	
	public String getBindingFor(Term t){
		return binding.get(t);
	}
	
	public boolean containsTerm(Term t){
		return binding.containsKey(t);
	}
	
	public boolean containsValue(String val){
		return binding.containsValue(val);
	}
	
	public boolean isEmpty(){
		return binding.isEmpty();
	}
}
