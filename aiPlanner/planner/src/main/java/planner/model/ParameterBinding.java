package planner.model;

import java.util.HashMap;
import java.util.Set;

import pddl4j.exp.term.Term;

public class ParameterBinding {
	private HashMap<String, String> binding; //parameter -> value
	
	public ParameterBinding() {
		binding = new HashMap<String, String>();
	}
	
	public ParameterBinding(HashMap<String, String> binding) {
		super();
		this.binding = binding;
	}

	public ParameterBinding(ParameterBinding other) {
		this.binding = new HashMap<String, String>(other.binding);
	}

	public void addBinding(Term term, String value){
		binding.put(term.getImage(), value);
	}
	
	public void addBinding(String term, String value){
		binding.put(term, value);
	}
	
	public HashMap<String, String> getBinding() {
		return binding;
	}
	
	public Set<String> getTerms(){
		return binding.keySet();
	}
	
	public String getBindingFor(Term t){
		return binding.get(t.getImage());
	}
	
	public String getBindingFor(String s){
		return binding.get(s);
	}
	
	public boolean containsTerm(Term t){
		return binding.containsKey(t.getImage());
	}
	
	public boolean containsTerm(String t){
		return binding.containsKey(t);
	}
	
	public boolean containsValue(String val){
		return binding.containsValue(val);
	}
	
	public boolean isEmpty(){
		return binding.isEmpty();
	}
	
	public boolean isExtending(ParameterBinding other){
		for (String t : binding.keySet()) {
			String thisVal = getBindingFor(t);
			String otherVal = other.getBindingFor(t);
			
			if(otherVal != null && otherVal != "?" && thisVal != null && thisVal != "?" && !otherVal.equalsIgnoreCase(thisVal)) return false;
		}
		
		for (String t : other.binding.keySet()) {
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
	
	public boolean validate(){
		String[] values = binding.values().toArray(new String[0]);

		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values.length; j++) {
				if (i != j && values[i] == values[j])
					return false;
			}
		}
		return true;
	}
}
