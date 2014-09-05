package planner.algorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pddl4j.PDDLObject;
import pddl4j.exp.action.ActionDef;
import pddl4j.exp.term.Constant;
import planner.model.Action;
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
	
	public Set<Action> getInstanceActions(){
		Iterator<ActionDef> actionsIterator = originalData.actionsIterator(); 
		Set<Action> actionSet = new HashSet<Action>();
		while(actionsIterator.hasNext()){
			ActionDef srcAction = actionsIterator.next();
			if(srcAction.getName().startsWith("fake_constraint")) continue;
			actionSet.add(new Action(srcAction));
		}
		return actionSet;
	}
	
	protected Set<Constraint> getInstanceConstraints(){
		Set<Constraint> constraints = new HashSet<Constraint>();
		Iterator<ActionDef> actionsIterator = originalData.actionsIterator();	//exract from fake actions
		
		while(actionsIterator.hasNext()){
			ActionDef srcAction = actionsIterator.next();
			if(srcAction.getName().startsWith("fake_constraint")){
				pddl4j.exp.action.Action actionInst = (pddl4j.exp.action.Action)srcAction;
				constraints.add(new Constraint(actionInst));
			}
		}
		return constraints;
	}
}
