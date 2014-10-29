package com.thegs.dsapp.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
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
import com.thegs.dsapp.model.helper.IncorrectTimeException;



public class EventResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	String AUTH_KEY = "abc123";
	
	public EventResource(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	// Produces XML or JSON output for a client 'program'			
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Event getEvent(@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)) {
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		Event e = EventDao.instance.getEventById(id);
		if(e==null) {
			throw new WebApplicationException(Response
					.status(Response.Status.NOT_FOUND.getStatusCode())
					.entity("Not Found").build());
		} else {
			return e;
		}
	}
	
	// Produces HTML for browser-based client
	@GET
	@Produces(MediaType.TEXT_XML)
	public Event getEventHTML(@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)) {
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		Event e = EventDao.instance.getEventById(id);
		if(e==null) {
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity("Bad Request").build());
		} else {
			response.setStatus(Response.Status.CREATED.getStatusCode());
			return e;
		}
		
	}
	
	@DELETE
	public String deleteEvent(@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)) {
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		Event e = EventDao.instance.getEventById(id);
		if (e!=null) {
			EventDao.instance.deleteEvent(e);
			return "200 OK";
		} else {
			new RuntimeException("DELETE: Event with " + id +  " not found").printStackTrace();
			throw new WebApplicationException(Response
					.status(Response.Status.NOT_FOUND.getStatusCode())
					.entity("NOT FOUND").build());
		}
	}
	
	@PUT
	//@Produces(MediaType.TEXT_HTML)
	public Response putEvent(
			@HeaderParam("Auth") String auth,
			@Context HttpServletResponse response) {
		try {
			if(auth == null || !auth.equals(AUTH_KEY)) {

				throw new WebApplicationException(Response.status(403)
						.entity("Forbidden")
						.header("authorised", "false").build());
			}
			Event newE = new Event(id);
			Response r = putResponse(newE);
			if (r.getStatus() == 201 || r.getStatus() == 204) {
				return r;
			} else {
				throw new WebApplicationException(Response
						.status(Response.Status.BAD_REQUEST.getStatusCode())
						.entity("Bad Request").build());
			}
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
			throw new WebApplicationException(Response
				.status(Response.Status.BAD_REQUEST.getStatusCode())
				.entity("Bad Request").build());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity("Bad Request").build());
		} catch (IncorrectTimeException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity("Bad Request").build());
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity("Bad Request").build());
		}

	}
	
	private Response putResponse(Event newEvent) {
		
		Response res;
		try {
			if(EventDao.instance.addEvent(newEvent)) {
				res = Response.noContent().build();
			} else {
				res = Response.created(uriInfo.getAbsolutePath()).entity(newEvent.getXML()).build();
			}
		} catch (Exception e) {
			res = Response
					.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity("Bad Request").build();
		}
		
		return res;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	@Path("xml")
	public String getEventXml(@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		if(auth == null || !auth.equals(AUTH_KEY)) {
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		Event e = EventDao.instance.getEventById(id);
		if(e==null) {
			throw new WebApplicationException(Response
					.status(Response.Status.NOT_FOUND.getStatusCode())
					.entity("Not Found").build());
		} else {
			try {
				e.createXMLFile();
				return e.getXML();
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new WebApplicationException(Response
						.status(Response.Status.BAD_REQUEST.getStatusCode())
						.entity("IO Exception. Cannot use file.").build());
			}
		}
	}
	
	@GET
	@Path("{type}/{query}")
	public String queryResult(
			@PathParam("type") String type,
			@PathParam("query") String query,
			@HeaderParam("Auth") String auth,
			@Context final HttpServletResponse response) {
		
		if(auth == null || !auth.equals(AUTH_KEY)) {
			throw new WebApplicationException(Response
					.status(Response.Status.FORBIDDEN.getStatusCode())
					.entity("Forbidden")
					.header("authorised", "false").build());
		}
		
		Event e = EventDao.instance.getEventById(id);
		
		try {
			
			if (type.equals("trade")) {
				
				if (query.equals("xml")) {
					return e.convert("tradexml");
				} else if (query.equals("json")) {
					return e.convert("tradejson");
				} else if (query.equals("totalprice")) {
					return e.convert("tradetotalprice");
				} else {
					throw new WebApplicationException(Response
							.status(Response.Status.BAD_REQUEST.getStatusCode())
							.entity("Bad Request")
							.build());
				}
			} else if (type.equals("quote")) {
				
				if (query.equals("xml")) {
					return e.convert("quotexml");
				} else if (query.equals("json")) {
					return e.convert("quotejson");
				} else {
					throw new WebApplicationException(Response
							.status(Response.Status.BAD_REQUEST.getStatusCode())
							.entity("Bad Request")
							.build());
				}
			} else {
				throw new WebApplicationException(Response
						.status(Response.Status.BAD_REQUEST.getStatusCode())
						.entity("Bad Request")
						.build());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST.getStatusCode())
					.entity("Bad Request")
					.build());
			
		}
		
	}
	
}
