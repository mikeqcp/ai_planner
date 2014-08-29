package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.strips.StripsAlgorithm;
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
		Algorithm alg = new StripsAlgorithm(problemData);
		
		ResultPlan plan = alg.solve();
		
		System.out.println("Algorithm completed successfully.");
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
