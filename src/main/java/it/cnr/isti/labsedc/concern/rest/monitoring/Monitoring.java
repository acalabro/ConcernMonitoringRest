package it.cnr.isti.labsedc.concern.rest.monitoring;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import it.cnr.isti.labsedc.concern.ConcernApp;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/biecointerface")
public class Monitoring {
	
	//ConcernApp monitorInstance;
	public static Thread monitoringInstance;

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
    	return"<!DOCTYPE html><head><meta charset=\"utf-8\"><title>Concern - Monitoring Infrastructure</title>"
    			+ "</head><style>\n"
    			+ "body {\n"
    			+ "  background-color: #E6E6FA;\n"
    			+ "}\n"
    			+ "</style><body><h2>Monitoring Infrastructure</h2><h3>Monitoring status: " + MonitoringStatus() + "</h3><br /><br />"
    			+ postDetails() + "</body></html>";
    }
    
    private String postDetails() {
    	return "POST method parameters required:<br />"
        		+ "@QueryParam(\"jobID\"),<br />"
        		+ "@QueryParam(\"timestamp\"),<br />"
        		+ "@QueryParam(\"messageClass\"),<br />"
        		+ "@QueryParam(\"source-ip\"),<br />"
        		+ "@QueryParam(\"source-id\"),<br />"
        		+ "@QueryParam(\"destination-ip\"),<br />"
        		+ "@QueryParam(\"event\"),<br />"
        		+ "@QueryParam(\"accessLevel\"),<br />"
        		+ "@QueryParam(\"priority\"),<br />"
        		+ "@QueryParam(\"crc\"),<br />"
        		+ "@QueryParam(\"body-format\"),<br />"
        		+ "@QueryParam(\"body-compression\"),<br />"
        		+ "@QueryParam(\"body\")";
    }
    
    @POST
    @Produces({MediaType.TEXT_PLAIN})
	public String biecointerface(
			@QueryParam("jobID") String jobID,
			@QueryParam("timestamp") double timestamp,
			@QueryParam("messageClass") String messageClass,
			@QueryParam("source-ip") String sourceIP,
			@QueryParam("source-id") String sourceID,
			@QueryParam("destination-id") String destinationID,
			@QueryParam("event") String event,
			@QueryParam("accessLevel") String accessLevel,
			@QueryParam("priority") int priority,
			@QueryParam("crc") double crc,
			@QueryParam("body-format") String bodyFormat,
			@QueryParam("body-compression") String bodyCompression,
			@QueryParam("body") String body
			) {
    	if (destinationID.compareToIgnoreCase("monitoring") == 0 && event.compareToIgnoreCase("start") == 0 ) {
    		
    		return MonitoringStart();
    	} else
        	if (destinationID.compareToIgnoreCase("monitoring") == 0 && event.compareToIgnoreCase("stop") == 0 ) {
        		 
        		return MonitoringStop();
        	}
    	return "unrecognized command";
		
		}

	private String MonitoringStart() {	
		try {
			ConcernApp.getInstance();
			return "Monitoring started";
		} catch (InterruptedException e) {
			return "failed to start monitoring";
		}	
	}
	
	private String MonitoringStop() {
		ConcernApp.killInstance();
		return "Monitoring stopped";
	}
	
	private String MonitoringStatus() {
		if (ConcernApp.isRunning()) {
			return " running";
		}
		return " stopped";
	}
}

