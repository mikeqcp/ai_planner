package planner.algorithm.pop.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;

import pddl4j.exp.AndExp;
import pddl4j.exp.Exp;
import pddl4j.exp.action.ActionDef;
import planner.algorithm.logic.TermOperations;
import planner.model.Action;
import planner.model.AtomicState;
import planner.model.BindedAction;
import planner.model.ParameterBinding;
import planner.model.State;

public class StartNode extends GraphNode {
	private State initState;
	private AtomicState[] initStates;
	
	public StartNode(State init) {
		initState = init;
		initStates = init.breakIntoAtomic();
		
		pddl4j.exp.action.Action startAction = new pddl4j.exp.action.Action("Start");
		startAction.setEffect(initState.getState());
		this.action = new Action(startAction);
		this.binding = new ParameterBinding();
	}
	
	@Override
	public Set<SubGoal> getPreconditions() {
		return new HashSet<SubGoal>();
	}
	
	@Override
	public String toString() {
//		return Arrays.asList(initStates).toString();
		return "START";
	}
	
	@Override
	public GraphNode clone() {
		StartNode clone = new StartNode(initState);
		clone.id = id;
		return clone;
	}
}
