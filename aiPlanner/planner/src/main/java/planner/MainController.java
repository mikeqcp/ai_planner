package planner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("")
public class MainController {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("test")
	public Response getPatients() {
		return Response.status(Status.OK).build();
	}
}
