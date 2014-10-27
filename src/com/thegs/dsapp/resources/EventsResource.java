package com.thegs.dsapp.resources;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.thegs.dsapp.dao.EventDao;
import com.thegs.dsapp.model.Event;



// will map xxx.xxx.xxx/rest/events
@Path("/events")
public class EventsResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String AUTH_KEY = "abc123";
	
	// Return the list of events for client applications/programs
	@GET
	public Collection<Event> getEvents(@HeaderParam("Auth") String auth,
		@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}

		return EventDao.instance.getStore().values();	
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		

		int count = EventDao.instance.getStore().keySet().size();
		return String.valueOf(count);
	}
	
	@Path("{event}")
	public EventResource getEvent(
			@PathParam("event") String id,
			@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		return new EventResource(uriInfo, request, id);
	}
	
}
