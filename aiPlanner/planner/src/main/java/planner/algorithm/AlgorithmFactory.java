package planner.algorithm;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm.AlgorithmType;
import planner.algorithm.pop.PopAlgorithm;
import planner.algorithm.regression.RegressionAlgorithm;
import planner.algorithm.strips.StripsAlgorithm;

public class AlgorithmFactory {
	public static Algorithm createAlgorithm(AlgorithmType type, PDDLObject data){
		switch (type) {
		case STRIPS:
			return new StripsAlgorithm(data);
		case REGRESSION:
			return new RegressionAlgorithm(data);
		case POP:
			return new PopAlgorithm(data);
		default:
			return new StripsAlgorithm(data);
		}
	}
}
