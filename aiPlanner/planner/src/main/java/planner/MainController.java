package planner;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pddl4j.PDDLObject;
import planner.algorithm.Algorithm;
import planner.algorithm.Algorithm.AlgorithmType;
import planner.algorithm.AlgorithmFactory;
import planner.model.ProcessLog;
import planner.model.ResultPlan;
import data.PddlParser;


@Path("")
public class MainController {
	private static final int TRIES = 5;
	private PddlParser parser = new PddlParser();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("solve")
	public ProcessLog solve(@Context HttpHeaders header, @Context HttpServletResponse response, 
			@FormParam("domain") String domain, @FormParam("instance") String instance, @FormParam("type") String type, @FormParam("limit") int limit) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PDDLObject inputData = parser.parse(domain, instance);
		AlgorithmType algType = Algorithm.typeFromString(type);
		Algorithm alg = null;
		
		for (int i = 0; i < TRIES; i++) {
			alg = AlgorithmFactory.createAlgorithm(algType, inputData);
			alg.setMaxPlanLength(limit);
			ResultPlan plan = alg.solve();

			if(!plan.isEmpty()) break;
		}
		return alg.getLog();
	}
}
