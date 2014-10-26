package com.thegs.dsapp.resources;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
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
	
	// Return the list of events to the user in the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Map<String, Event> getEventsBrowser(
			@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		
		return EventDao.instance.getStore(); 
	}
	
	// Return the list of events for client applications/programs
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Map<String, Event> getEvents(@HeaderParam("Auth") String auth,
		@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}

		return EventDao.instance.getStore();	
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount(@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		

		int count = EventDao.instance.getStore().keySet().size();
		return String.valueOf(count);
	}
	
    // Client should set Content Type accordingly
	/*@PUT
	@Produces(MediaType.TEXT_HTML)
	@Path("{event}")
	public String newEvent(
			@PathParam("event") String id,
			@Context HttpServletResponse servletResponse,
			@HeaderParam("Auth") String auth
	) throws IOException {
		if(auth == null || !auth.equals(AUTH_KEY)){
			servletResponse.setHeader("authorised", "false");
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
				.header("authorised", "false").build());
		} else {
			String uri = null; //TODO: what is this?
			Event e = new Event(id, uri);
			EventDao.instance.addEvent(e);
		}
		
		// Redirect to some HTML page  
		// You need to create this file under WEB-INF
		//servletResponse.setHeader("cost", cost);
		//servletResponse.setHeader("uri", "/events/" + id);
		//servletResponse.setHeader("authorised", "true");
		//servletResponse.sendRedirect("../create_event.html");
		
		return "done";
	}*/
	
	
	// Important to note that this Path annotation define.
	// This will match xxx.xxx.xxx/rest/events/{event}
	// It says 'the thing that comes after events/ is a parameter
	// and it is passed to the EventResource class for processing
	// e.g., http://localhost:8080/cs9322.simple.rest.events/rest/events/3
        // This matches this method which returns EventResource.
	@Path("{event}")
	public EventResource getEvent(
			@PathParam("event") String id,
			@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)){
			response.setHeader("authorised", "false");
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		return new EventResource(uriInfo, request, id);
	}
	
}
