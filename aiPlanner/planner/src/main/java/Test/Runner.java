package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.pop.PopAlgorithm;
import planner.algorithm.regression.RegressionAlgorithm;
import planner.algorithm.strips.StripsAlgorithm;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import planner.model.interfaces.ProcessStateDump;
import data.PddlParser;

public class Runner {
	private final static String TEST_DOMAIN = "C:\\Dokumenty\\Studia\\magisterka\\project\\codebase\\data\\pddl\\simple_00\\domain.txt";
	private final static String TEST_INSTANCE = "C:\\Dokumenty\\Studia\\magisterka\\project\\codebase\\data\\pddl\\simple_00\\instance_test.txt";
	
	public static void main(String[] args) {
		PddlParser problemParser = new PddlParser();
		
		String testDomain = Runner.readFileToString(Runner.TEST_DOMAIN);
		String testInstance = Runner.readFileToString(Runner.TEST_INSTANCE);

		PDDLObject problemData = null;
		try{
			problemData = problemParser.parse(testDomain, testInstance);
		}catch (Exception e ) {
			System.out.println("ERROR");
			return;
		}
		//run algorithm
		
		for (int i = 0; i < 10; i++) {
			
			for(int j = 0; j< 1; j++){
				System.out.println("No." + i);
//				Algorithm alg = new StripsAlgorithm(problemData);
				Algorithm alg = new RegressionAlgorithm(problemData);
//				Algorithm alg = new PopAlgorithm(problemData);
				
				alg.setMaxPlanLength(4);
				
				ResultPlan plan = alg.solve();
				ProcessLog log = alg.getLog();
				
				if(plan == null || plan.isEmpty()) continue;
				
				System.out.println("---");
				System.out.println("SOLVED.");
				System.out.println("---");
				break;
			}
			
		}
	}
	
	private static String readFileToString(String file){
		try {
			return new Scanner(new File(file)).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}
