package planner.algorithm;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm.AlgorithmType;
import planner.algorithm.strips.StripsAlgorithm;

public class AlgorithmFactory {
	public static Algorithm createAlgorithm(AlgorithmType type, PDDLObject data){
		switch (type) {
		case STRIPS:
			return new StripsAlgorithm(data);
		default:
			return new StripsAlgorithm(data);
		}
	}
}
