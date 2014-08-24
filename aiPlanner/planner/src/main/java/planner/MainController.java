package planner;

import java.util.ArrayList;

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
import planner.model.ProcessHistory;
import planner.model.ProcessStateDump;
import planner.model.ResultPlan;
import planner.model.State;
import data.PddlParser;


@Path("")
public class MainController {
	private PddlParser parser = new PddlParser();
	
	/**
	 * Test method to check connectivity
	 * @return Status OK
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("test")
	public Response getStatusOk() {
		return Response.status(Status.OK).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("solve")
	public ProcessHistory solve(@Context HttpHeaders header, @Context HttpServletResponse response, 
			@FormParam("domain") String domain, @FormParam("instance") String instance) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PDDLObject inputData = parser.parse(domain, instance);
		
		return new ProcessHistory(new ArrayList<ProcessStateDump>(5), new ResultPlan(new ArrayList<State>(3)));
	}
}
