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

	public ParameterBinding(ParameterBinding other) {
		this.binding = new HashMap<Term, String>(other.binding);
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
	
	public boolean isExtending(ParameterBinding other){
		for (Term t : binding.keySet()) {
			String thisVal = getBindingFor(t);
			String otherVal = other.getBindingFor(t);
			
			if(otherVal != null && otherVal != "?" && thisVal != null && thisVal != "?" && !otherVal.equalsIgnoreCase(thisVal)) return false;
		}
		
		for (Term t : other.binding.keySet()) {
			String thisVal = getBindingFor(t);
			String otherVal = other.getBindingFor(t);
			
			if(otherVal != null && otherVal != "?" && thisVal != null && thisVal != "?" && !otherVal.equalsIgnoreCase(thisVal)) return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return binding.toString();
	}
}
