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
    @Produces({MediaType.TEXT_PLAIN})
    public String getIt() {
        return "Methods available:\nPOST:\n"
        		+ "@QueryParam(\"jobID\"),\n"
        		+ "@QueryParam(\"timestamp\"),\n"
        		+ "@QueryParam(\"messageClass\"),\n"
        		+ "@QueryParam(\"source-ip\"),\n"
        		+ "@QueryParam(\"source-id\"),\n"
        		+ "@QueryParam(\"destination-ip\"),\n"
        		+ "@QueryParam(\"event\"),\n"
        		+ "@QueryParam(\"accessLevel\"),\n"
        		+ "@QueryParam(\"priority\"),\n"
        		+ "@QueryParam(\"crc\"),\n"
        		+ "@QueryParam(\"body-format\"),\n"
        		+ "@QueryParam(\"body-compression\"),\n"
        		+ "@QueryParam(\"body\")"
        		+ "\n\n\nMonitoring status:" + MonitoringStatus();
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
			return " is running";
		}
		return " is stopped";
	}
}

