package it.cnr.isti.labsedc.concern.rest.monitoring;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.utils.BiecoMessageTypes;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/biecointerface")

public class Monitoring {
	
	private String incomingToken = "YeAm0hdkf5W9s";
	private String outcomingToken = "746gfbrenkljhGU";
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getIt() {
        return homePage();
        
    }
    
    
    private String homePage() {
	
    	return"<!DOCTYPE html><head><meta charset=\"utf-8\"><title>Monitoring Framework</title>"
    			+ "</head><style>\n"
    			+ "body {\n"
    			+ "  background-color: #ddebef;\n"
    			+ "}\n"
    			+ "</style><body><h2>Runtime Monitoring</h2><h3>Status: " + MonitoringStatus() + "</h3>"
    			+ "<h4>Monitoring logs:</h4><textarea id=\"logs\" name=\"debugLog\" rows=\"30\" cols=\"200\">\n"
				+ getLoggerData() + "</textarea></body></html>";
    	    	
    }
     
	@POST
	public Response biecointerface(
			@Context HttpHeaders headers,
			@QueryParam("jobID") String jobID,
			@QueryParam("timestamp") String timestamp,
			@QueryParam("messageType") String messageType,
			@QueryParam("sourceIP") String sourceIP,
			@QueryParam("sourceID") String sourceID,
			@QueryParam("destinationID") String destinationID,
			@QueryParam("event") String event,
			@QueryParam("accessLevel") String accessLevel,
			@QueryParam("priority") int priority,
			@QueryParam("CRC") long CRC,
			@QueryParam("bodyFormat") String bodyFormat,
			@QueryParam("bodyCompression") String bodyCompression,
			@QueryParam("body") String body
			) {
	
		String authorization = headers.getRequestHeader("Authorization").get(0);
		if (authorization.compareTo(outcomingToken) == 0) {
			if (destinationID.compareToIgnoreCase("10") == 0) {
				if (messageType.compareTo(BiecoMessageTypes.HEARTBEAT) == 0 ) {
					return Response.status(200).build();
				}
				return Response.status(401).entity("invalid messageType").build();	
			}
			return Response.status(505).entity("invalid destination").build();
		}
		return Response.status(401).entity("invalid access token").build();
	}

	private Response start() {
		return Response.status(200)
				.entity(MonitoringStart()).header("Authorization", incomingToken)
				.build();
	}
	
	private Response stop() {
		return Response.status(200)
		.entity(MonitoringStop()).header("Authorization", incomingToken)
		.build();
	}
	
	private Response heartbeat() {
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		return Response.status(200).build();
	}
	
	private Response getStatus() {
		return Response.status(200).entity(MonitoringStatus()).build();
	}
	
	private Response configure() {
		return Response.status(404).build();
	}
	
	private Response data() {
		return Response.status(404).build();
	}
	
	private Response event() {
		return Response.status(404).build();
	}
	
	 private void parseMessage(String message) {
	        switch(message) {
	            case BiecoMessageTypes.GETSTATUS:
	                this.getStatus();
	                break;
	            case BiecoMessageTypes.HEARTBEAT:
	                this.heartbeat();
	                break;
	            case BiecoMessageTypes.CONFIGURE:
	                this.configure();
	                break;
	            case BiecoMessageTypes.DATA:
	                this.data();
	                break;
	            case BiecoMessageTypes.EVENT:
	                this.event();
	                break;
	            case BiecoMessageTypes.START:
	                this.start();
	                break;
	            case BiecoMessageTypes.STOP:
	                this.stop();
	                break;
	            case BiecoMessageTypes.HALT:
	                this.stop();
	                break;
	        }
	    }
	
	private String MonitoringStart() {	
		try {
			ConcernApp.getInstance();
			return "Monitoring started";
		} catch (InterruptedException e) {
			return "failed to start monitoring";
		}	
	}
	
	private String getLoggerData() {
		return ConcernApp.getLoggerData();
	}
	
	private String MonitoringStop() {
		if (ConcernApp.isRunning()) {
			ConcernApp.killInstance();
			return "Monitoring stopped";
		} else
			return "Monitoring was not running"; 
		
	}
	
	private String MonitoringStatus() {
		if (ConcernApp.isRunning()) {
			return "Running";
		}
		return "Online";
	}
}

