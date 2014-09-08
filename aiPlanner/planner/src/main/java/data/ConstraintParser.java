package data;

import java.util.ArrayList;
import java.util.List;

public class ConstraintParser {
	private String input;
	private List<String> constraints;
	int counter = 0;
	
	public ConstraintParser(String input) {
		this.input = input;
		constraints = new ArrayList<String>();
	}
	
	private String insertFakeAction(String constraint, String params){
		String name = "fake_constraint_" + counter++;
		return "(:action " + name + " \n"
				+ ":parameters (" + params + "\n"
				+ ":precondition " + constraint +  "\n"
				+ ":effect " + "(arm-empty)" + ")\n";
	}
	
	
	private String[] getConstraintValue(int startInd){	
		String[] values = new String[] {"(", "(", ""};
		int i = startInd + 1;
		int bracketsOpened = 1;
		int fullfilled = 0;
		while(true){
			char c = input.charAt(i++);
			
			values[0] += c;
			if(bracketsOpened >= 3 && fullfilled >= 1)			
				values[1] += c;
			
			if(bracketsOpened ==3 && fullfilled == 0)			
				values[2] += c;
			
			if(c == '(') bracketsOpened++;
			if(c == ')') {
				bracketsOpened--;
				fullfilled++;
			}
			
			if(bracketsOpened == 0) break;
		}
		return values;
	}
	
	public String parse(){
		String parsed = input;
		String safetyStr = "(:safety";
		while(true){
			int pos = parsed.indexOf(safetyStr);
			if(pos < 0) break;
			
			String[] oldValues = getConstraintValue(pos);
			String newValue = insertFakeAction(oldValues[1], oldValues[2]);
			parsed = parsed.replace(oldValues[0], newValue);
		}
		
		return parsed;
	}
	
	
}
