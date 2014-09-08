package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import pddl4j.Domain;
import pddl4j.ErrorManager;
import pddl4j.ErrorManager.Message;
import pddl4j.PDDLObject;
import pddl4j.Parser;
import pddl4j.Problem;
import pddl4j.RequireKey;

public class PddlParser {
	private final String TMP_PATH = "\\solver_tmp\\";

	public PDDLObject parse(String domain, String instance) {
		try {
			createMissingDirectory(TMP_PATH);
			
			ConstraintParser cp = new ConstraintParser(domain);
			domain = cp.parse();
			
			String[] files = createFiles(domain, instance);
			return parseFiles(files);
		} catch (IOException ex) {
			System.out.println("Error parsing PPDL.");
			System.out.println(ex);
		}
		return null;
	}

	private void createMissingDirectory(String path) {
		File theDir = new File(path);

		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			try {
				theDir.mkdir();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * @param domainContent
	 * @param instanceContent
	 * @return Array of created files names
	 * @throws FileNotFoundException
	 */
	private String[] createFiles(String domainContent, String instanceContent)
			throws FileNotFoundException {
		String filename = TMP_PATH + System.currentTimeMillis();
		String filenames[] = { filename + "_domain.pddl",
				filename + "_instance.pddl" };

		PrintWriter out = new PrintWriter(filenames[0]);
		out.println(domainContent);
		out.close();

		out = new PrintWriter(filenames[1]);
		out.println(instanceContent);
		out.close();

		return filenames;
	}

	private PDDLObject parseFiles(String[] files) throws FileNotFoundException {
		Properties options = new Properties();
		options.put(RequireKey.STRIPS, true);
		options.put(RequireKey.NEGATIVE_PRECONDITIONS, true);
		options.put(RequireKey.DISJUNCTIVE_PRECONDITIONS, true);

		// Creates an instance of the java pddl parser
		Parser parser = new Parser(options);
		Domain domain = parser.parse(new File(files[0]));
		Problem problem = parser.parse(new File(files[1]));
		PDDLObject obj = parser.link(domain, problem);
		
		
		// Gets the error manager of the pddl parser
		ErrorManager mgr = parser.getErrorManager();
		if (mgr.contains(Message.ERROR)) {
			mgr.print(Message.ALL);
		} // else we print the warnings
		else {
			mgr.print(Message.WARNING);
			System.out.println("\nParsing domain \"" + domain.getDomainName()
					+ "\" done successfully ...");
			System.out.println("Parsing problem \"" + problem.getProblemName()
					+ "\" done successfully ...\n");
		}
		return obj;
	}
}
