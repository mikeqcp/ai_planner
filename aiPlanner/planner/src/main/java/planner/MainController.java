package planner;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import planner.model.State;


@Path("")
public class MainController {
	
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
	public State solve(@Context HttpHeaders header, @Context HttpServletResponse response, 
			@FormParam("domain") String domain, @FormParam("instance") String instance) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		return new State(domain.substring(0, 15) + "..." + instance.substring(0, 15) + "...");
	}
}
