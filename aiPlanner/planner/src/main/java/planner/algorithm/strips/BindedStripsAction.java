package planner.algorithm.strips;

import java.util.Random;
import java.util.Set;

import pddl4j.exp.Exp;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Term;

public class BindedStripsAction extends StripsAction {
	private Random rand = new Random();
	private ParameterBinding binding;
	
	
	public BindedStripsAction(StripsAction parent, ParameterBinding binding) {
		super(parent.action);
		this.binding = binding;
	}

	/**
	 * @param initial state
	 * @return state after applying action (new object is created, old one is unchanged)
	 */
	public StripsState applyTo(StripsState s){
		Exp pre = action.getPrecondition();
		Exp post = action.getEffect();
		
		return s;	//TODO
	}

	public void fillFreeParameters(Set<Constant> constants) {
		for (Term param : this.action.getParameters()) {
			if(!binding.containsTerm(param)){
				String randVal = randConstant(constants, binding);
				binding.addBinding(param, randVal);
			}
		};
	}
	
	/**
	 * @param constants - values to choose from
	 * @param existingValues - values to omit
	 * @return
	 */
	private String randConstant(Set<Constant> constants, ParameterBinding existingValues){
		Constant[] cArray = constants.toArray(new Constant[0]);
		int randIndex = rand.nextInt(cArray.length);

		int iterationLimit = cArray.length + 1;
		int i = 0;
		String val = cArray[randIndex].getImage();
		while(existingValues.containsValue(val) && i < iterationLimit){
			val = cArray[++randIndex % cArray.length].getImage();
			i++;
		}
		
		if(i >= iterationLimit) return null;
		return val;
	}
}
