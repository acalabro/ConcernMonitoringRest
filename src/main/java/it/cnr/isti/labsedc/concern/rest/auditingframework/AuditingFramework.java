package it.cnr.isti.labsedc.concern.rest.auditingframework;

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
@Path("auditingframework/biecointerface")
public class AuditingFramework {
	
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
	
    	return"<!DOCTYPE html><head><meta charset=\"utf-8\"><title>Auditing Framework</title>"
    			+ "</head><style>\n"
    			+ "body {\n"
    			+ "  background-color: #ddebef;\n"
    			+ "}\n"
    			+ "</style><body><h2>Runtime Monitoring</h2><h3>Status: " + MonitoringStatus() + "</h3>"
    			+ "</style><body><h2>Predictive Simulation</h2><h3>Status: " + PredictiveSimulationStatus() + "</h3><br />"
    			+ "<h4>Monitoring logs:</h4><textarea id=\"logs\" name=\"debugLog\" rows=\"30\" cols=\"200\">\n"
				+ getLoggerData() + "</textarea></body></html>";
    	
    	
    	/*return"<!DOCTYPE html><head><meta charset=\"utf-8\"><title>Concern - Monitoring Infrastructure</title>"
    			+ "</head><style>\n"
    			+ "body {\n"
    			+ "  background-color: #E6E6FA;\n"
    			+ "}\n"
    			+ "</style><body><h2>Monitoring Infrastructure</h2><h3>Monitoring status: " + MonitoringStatus() + "</h3><br /><br />"
    			+ "<textarea id=\"logs\" name=\"debugLog\" rows=\"30\" cols=\"200\">\n"
				+ getLoggerData() + "</textarea></body></html>";*/
    	
    	
    }
    
	/*
	 * private String postDetails() { return
	 * "POST method parameters required:<br />" + "@QueryParam(\"jobID\"),<br />" +
	 * "@QueryParam(\"timestamp\"),<br />" + "@QueryParam(\"messageClass\"),<br />"
	 * + "@QueryParam(\"source-ip\"),<br />" + "@QueryParam(\"source-id\"),<br />" +
	 * "@QueryParam(\"destination-ip\"),<br />" + "@QueryParam(\"event\"),<br />" +
	 * "@QueryParam(\"accessLevel\"),<br />" + "@QueryParam(\"priority\"),<br />" +
	 * "@QueryParam(\"crc\"),<br />" + "@QueryParam(\"body-format\"),<br />" +
	 * "@QueryParam(\"body-compression\"),<br />" + "@QueryParam(\"body\")"; }
	 */
    
    private String PredictiveSimulationStatus() {
		return "Stopped";
	}

    @GET
	@Path("/heartbeat")
    @Produces({MediaType.TEXT_HTML})
    public String heartbeat() {
        return "Interface alive" + "<br />Runtime monitoring: " + MonitoringStatus() + "<br /> Predictive Simulation: " + PredictiveSimulationStatus();
        
    }
    
	@POST
    @Produces({MediaType.TEXT_PLAIN})
	public String biecointerface(
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
	
    	if (destinationID.compareToIgnoreCase("monitoring") == 0 && messageType.compareToIgnoreCase("Start") == 0 ) {
    		
    		return MonitoringStart();
    	}
    	
        if (destinationID.compareToIgnoreCase("monitoring") == 0 && messageType.compareToIgnoreCase("Stop") == 0 ) {
       		 
        		return MonitoringStop();
        }
    
    	if (destinationID.compareToIgnoreCase("predictive") == 0 && messageType.compareToIgnoreCase("Start") == 0 ) {
    		
    		return PredictiveSimulationStart();
    	}
    	
        if (destinationID.compareToIgnoreCase("predictive") == 0 && messageType.compareToIgnoreCase("Stop") == 0 ) {
       		 
        	return PredictiveSimulationStop();
        }
        
        if (destinationID.compareToIgnoreCase("monitoring") == 0 && messageType.compareToIgnoreCase("Heartbeat") == 0 ) {
      		 
    		return MonitoringHeartbeat();
        }

        if (destinationID.compareToIgnoreCase("predictive") == 0 && messageType.compareToIgnoreCase("Heartbeat") == 0 ) {
		
		return PredictiveSimulationHeartbeat();
        }
        
    	return "unrecognized command";
		
		}

	private String PredictiveSimulationHeartbeat() {
		return PredictiveSimulationStatus();
	}

	private String MonitoringHeartbeat() {
		return MonitoringStatus();
	}

	private String PredictiveSimulationStop() {
		return null;
	}

	private String PredictiveSimulationStart() {
		return null;
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
		ConcernApp.killInstance();
		return "Monitoring stopped";
	}
	
	private String MonitoringStatus() {
		if (ConcernApp.isRunning()) {
			return "Running";
		}
		return "Online";
	}
}

