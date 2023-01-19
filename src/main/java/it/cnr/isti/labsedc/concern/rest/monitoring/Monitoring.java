package it.cnr.isti.labsedc.concern.rest.monitoring;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

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
	
    	return"<!DOCTYPE html><head><meta charset=\"utf-8\"><title>Runtime Monitoring</title>"
    			+ "</head>"
    			+"<style type=\"text/css\">\n"
    			+ "div {\n"
    			+ "	display: flex;\n"
    			+ "	flex-direction: column;\n"
    			+ "	align-items: center;\n"
    			+ "}\n"
    			+ "textarea {\n"
    			+ "	margin-top: 15px;\n"
    			+ "	width: 70%;\n"
    			+ "}\n"
    			+ "</style>"
    			+"<body><center><h2 style=\"color: green;\">Runtime Monitoring</h2><h3>Status: " + MonitoringStatus() + "<br /><br />Rules loaded: " 
    			+ getAmountOfLoadedRules() + "</h3><h4>Loaded rules list:</h4>" + getRulesList()
    			+ "<div id=\"logs\"><h4>Monitoring logs:</h4><textarea name=\"debugLog\" rows=\"30\" cols=\"200\">\n"
				+ getLoggerData() + "</textarea></div>"
    			+ " <button onclick=\"show()\">Show/Hide log window</button>\n"
				+"<br /><br />     	<button onclick=\"myFunction()\">Load rules</button>\n"
				+"<br /><br />     	<button onclick=\"myFunction2()\">Delete rules</button>\n<br />"
				+ "\n"
				+ "    	<script>\n"
				+ "    	function myFunction() {\n"
				+ "    	    window.open(\"./biecointerface/loadrules\", \"_blank\", \"toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=400,height=600\");\n"
				+ "    	}\n"
				+ "    	</script>"
				+ "    	<script>\n"
				+ "    	function myFunction2() {\n"
				+ "    	    window.open(\"./biecointerface/deleterules\", \"_blank\", \"toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=400,height=600\");\n"
				+ "    	}\n"
				+ "    	</script>"
    			+ "<script>function show() {\n"
    			+"  var x = document.getElementById(\"logs\");\n"
    			+ "  if (x.style.display === \"none\") {\n"
    			+ "    x.style.display = \"block\";\n"
    			+ "  } else {\n"
    			+ "    x.style.display = \"none\";\n"
    			+ "  }}</script>"
				+ "<br /></center></body></html>";  	
    
    

    
    
    }
     
	private String getRulesList() {
		if (ConcernApp.isRunning()) {
			return ConcernApp.getRulesList();
		}
		return "";
	}
	
	private String getAmountOfLoadedRules() {
		if (ConcernApp.isRunning()) {
			return Integer.toString(ConcernApp.getAmountOfLoadedRules());
		}
		return "0";
	}
	
	private String getLastLoadedRuleName() {
		if (ConcernApp.isRunning()) {
			return ConcernApp.getLastRuleLoadedName();
		}
		return "N/A";
	}
	

	/*@POST
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
	}*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response biecointerface(
			String jsonMessage,
			@Context HttpHeaders headers) {
    	String authorization = headers.getRequestHeader("Authorization").get(0);
		if (authorization.compareTo(outcomingToken) == 0) {
	    	JSONObject bodyMessage = new JSONObject(jsonMessage);
	    	
	    	if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.HEARTBEAT) == 0 ) {
				return this.heartbeat();
			}
	    	else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.START) == 0 ) {
	    			return this.start();
			}
    		else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.STOP) == 0 ) {
    			return this.stop();
    		}
    		else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.GETSTATUS) == 0 ) {
    			return this.getStatus();
    		} 
    		else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.CONFIGURE) == 0 ) {
    			return this.configure();
    		} 
    		else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.DATA) == 0 ) {
    			return this.data();
    		} 
    		else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.EVENT) == 0 ) {
    			return this.event();
    		} 
    		else if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.DEMO) == 0 ) {
    			return this.demo();
    		}
    		else
            	return Response.status(400).entity("invalid messageType").build();
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
		return Response.status(200).build();
	}
	
	private Response getStatus() {
		return Response.status(200).entity(MonitoringStatus()).build();
	}
	private Response demo() {
		return Response.status(200).entity(DemoStatus()).build();
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
	private String DemoStatus() {
		try {
			ConcernApp.getInstance();
			ConcernApp.DemoStart();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Starting Demo";
	}
}

