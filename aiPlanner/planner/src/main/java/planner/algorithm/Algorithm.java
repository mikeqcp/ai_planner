package planner.algorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.RequireKey;
import pddl4j.exp.term.Constant;
import planner.model.Constraint;
import planner.model.ProcessLog;
import planner.model.ResultPlan;

public abstract class Algorithm {
	public static enum AlgorithmType {STRIPS, REGRESSION};
	protected PDDLObject originalData;
	
	public Algorithm(PDDLObject input) {
		this.originalData = input;
		getInstanceConstraints();
	}
	
	abstract public ResultPlan solve();
	abstract public ProcessLog getLog();
	
	public static AlgorithmType typeFromString(String type){
		if(type.equalsIgnoreCase("strips")) return AlgorithmType.STRIPS;
		if(type.equalsIgnoreCase("regression")) return AlgorithmType.REGRESSION;
		
		return null;
	}
	
	protected Set<Constant> getInstanceConstants(){
		Set<Constant> constants = new HashSet<Constant>();
		
		Iterator<Constant> iter = originalData.constantsIterator();
		while(iter.hasNext()){
			constants.add(iter.next());
		}
		return constants;
	}
	
	protected Set<Constraint> getInstanceConstraints(){
		Set<Constraint> constraints = new HashSet<Constraint>();
		
		Iterator<RequireKey> i = originalData.requirementsIterator();
		
//		Iterator<Constraint> iter = originalData.constantsIterator();
//		while(iter.hasNext()){
//			constraints.add(iter.next());
//		}
		return constraints;
	}
}
