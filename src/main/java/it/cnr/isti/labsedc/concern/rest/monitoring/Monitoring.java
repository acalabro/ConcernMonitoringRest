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
//@Component
//public class AuthFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        if (req.getHeader("Authorization") != null){ //or replace with a further fine grained condition
//            chain.doFilter(request, response);
//        } else {
//            HttpServletResponse res = (HttpServletResponse) response;
//            res.setStatus(401);
//        }
//    }
//}

public class Monitoring {
	
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
 
    @GET
	@Path("/heartbeat")
    @Produces({MediaType.TEXT_HTML})
    public String heartbeat() {
    	
        return "Interface alive" + "<br />Runtime monitoring: " + MonitoringStatus();
        
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
        
        if (destinationID.compareToIgnoreCase("monitoring") == 0 && messageType.compareToIgnoreCase("Heartbeat") == 0 ) {
      		 
    		return MonitoringHeartbeat();
        }
        
    	return "unrecognized command";
		
		}

	private String MonitoringHeartbeat() {
		return MonitoringStatus();
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

