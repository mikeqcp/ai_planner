package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.regression.RegressionAlgorithm;
import planner.algorithm.strips.StripsAlgorithm;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import data.PddlParser;

public class Runner {
	private final static String TEST_DOMAIN = "C:\\Dokumenty\\Studia\\magisterka\\project\\codebase\\data\\pddl\\simple_00\\domain.txt";
	private final static String TEST_INSTANCE = "C:\\Dokumenty\\Studia\\magisterka\\project\\codebase\\data\\pddl\\simple_00\\instance_test.txt";
	
	public static void main(String[] args) {
		PddlParser problemParser = new PddlParser();
		
		String testDomain = Runner.readFileToString(Runner.TEST_DOMAIN);
		String testInstance = Runner.readFileToString(Runner.TEST_INSTANCE);

		PDDLObject problemData = problemParser.parse(testDomain, testInstance);
		
		//run algorithm
		
		for (int i = 0; i < 1; i++) {
			System.out.println("No." + i);
//			Algorithm alg = new StripsAlgorithm(problemData);
			Algorithm alg = new RegressionAlgorithm(problemData);
			
			ResultPlan plan = alg.solve();
			ProcessLog log = alg.getLog();
			
			System.out.println("Algorithm completed successfully.");
			System.out.println("---");
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
