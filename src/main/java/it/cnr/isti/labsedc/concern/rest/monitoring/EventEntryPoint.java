package it.cnr.isti.labsedc.concern.rest.monitoring;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.json.JSONObject;

import it.cnr.isti.labsedc.concern.utils.BiecoMessageTypes;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/evententrypoint")


public class EventEntryPoint {

	static String incomingToken = "YeAm0hdkf5W9s";
	private String outcomingToken = "746gfbrenkljhGU";

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response evententrypoint(
			String jsonMessage,
			@Context HttpHeaders headers) {
    	String authorization = headers.getRequestHeader("Authorization").get(0);
		if (authorization.compareTo(outcomingToken) == 0) {
	    	JSONObject bodyMessage = new JSONObject(jsonMessage);
	    	
	    	if (((String)bodyMessage.get("messageType")).compareTo(BiecoMessageTypes.EVENT) == 0 ) {
				return this.messageOK(bodyMessage);
			}
    		else
            	return Response.status(400).entity("invalid messageType").build();
		}
		return Response.status(401).entity("invalid access token").build();
    }

	private Response messageOK(JSONObject bodyMessage) {
		
			return Response.status(200).build();
		}
    
    

}

