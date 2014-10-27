package com.thegs.dsapp.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamSource; 
import javax.xml.transform.stream.StreamResult; 

import org.w3c.dom.Document;
import org.xml.sax.SAXException;



public class EventResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	String AUTH_KEY = "abc123";
	
	private static Document document;
	
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
	@Consumes(MediaType.APPLICATION_XML)
	//@Produces(MediaType.TEXT_HTML)
	public Response putEvent(
			@PathParam ("eventID") String id,
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
		}

	}
	
	private Response putResponse(Event newEvent) {
		
		Response res;
		try {
			if(EventDao.instance.addEvent(newEvent)) {
				res = Response.noContent().build();
			} else {
				res = Response.created(uriInfo.getAbsolutePath()).entity(newEvent).build();
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
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new WebApplicationException(Response
						.status(Response.Status.BAD_REQUEST.getStatusCode())
						.entity("IO Exception. Cannot write to file.").build());
			}
			return e.getXML();
		}
	}
	
    public String convert(String fileXML, String fileXSD) {
    	
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	String ans = null;
        
    	try {
            
            File stylesheet = new File(fileXSD);
            File datafile = new File(fileXML);

            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(datafile);
            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource stylesource = new StreamSource(stylesheet); 
            Transformer transformer = tFactory.newTransformer(stylesource);

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(ans);
            transformer.transform(source, result);
            
            return ans;
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());

            // Use the contained exception, if any
            Throwable x = tce;

            if (tce.getException() != null) {
                x = tce.getException();
            }

            x.printStackTrace();
        } catch (TransformerException te) {
            // Error generated by the parser
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());

            // Use the contained exception, if any
            Throwable x = te;

            if (te.getException() != null) {
                x = te.getException();
            }

            x.printStackTrace();
        } catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception x = sxe;

            if (sxe.getException() != null) {
                x = sxe.getException();
            }

            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }
		return ans;
    }
}
