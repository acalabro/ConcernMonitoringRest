package it.cnr.isti.labsedc.concern.rest.monitoring;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.json.JSONObject;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.consumer.Consumer;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/biecointerface/browseruledb")

public class BrowseRuleDB {
		
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
	
    	return"<!DOCTYPE html>\n"
    			+ "<html>\n"
    			+ "<head lang=\"en\">\n"
    			+ "    <meta charset=\"UTF-8\"/>\n"
    			+ "    <title>Browse rules DB</title>\n"
    			+ "    <style type=\"text/css\">\n"
    			+ "    div {\n"
    			+ "        display: flex;\n"
    			+ "        flex-direction: column;\n"
    			+ "        align-items: center;\n"
    			+ "    }\n"
    			+ "    input {\n"
    			+ "        margin-top: 10px;\n"
    			+ "    }\n"
    			+ "     \n"
    		    + "#ruleslist{\n"
    		    + "  width:350px;   \n"
    		    + "  font-size:1em;\n"
    		    + "}"
    		    +"	button{\n"
    		    + "            outline: 0;\n"
    		    + "            border: 0;\n"
    		    + "            cursor: pointer;\n"
    		    + "            will-change: box-shadow,transform;\n"
    		    + "            background: radial-gradient( 100% 100% at 100% 0%, #89E5FF 0%, #5468FF 100% );\n"
    		    + "            box-shadow: 0px 2px 4px rgb(45 35 66 / 40%), 0px 7px 13px -3px rgb(45 35 66 / 30%), inset 0px -3px 0px rgb(58 65 111 / 50%);\n"
    		    + "            padding: 0 32px;\n"
    		    + "            border-radius: 6px;\n"
    		    + "            color: #fff;\n"
    		    + "            height: 48px;\n"
    		    + "            font-size: 18px;\n"
    		    + "            text-shadow: 0 1px 0 rgb(0 0 0 / 40%);\n"
    		    + "            transition: box-shadow 0.15s ease,transform 0.15s ease;\n"
    		    + "            :hover {\n"
    		    + "                box-shadow: 0px 4px 8px rgb(45 35 66 / 40%), 0px 7px 13px -3px rgb(45 35 66 / 30%), inset 0px -3px 0px #3c4fe0;\n"
    		    + "                transform: translateY(-2px);\n"
    		    + "            }\n"
    		    + "            :active{\n"
    		    + "                box-shadow: inset 0px 3px 7px #3c4fe0;\n"
    		    + "                transform: translateY(2px);\n"
    		    + "            }"
    		    +"button:disabled,\n"
    		    + "button[disabled]{\n"
    		    + "  border: 1px solid #999999;\n"
    		    + "  background-color: #cccccc;\n"
    		    + "  color: #666666;\n"
    		    + "}"
    			+ "</style>\n"
    			+ "<style style=\"text/css\">\n"
    			+ "  	.hoverTable{\n"
    			+ "		width:100%; \n"
    			+ "		border-collapse:collapse; \n"
    			+ "	}\n"
    			+ "	.hoverTable td{ \n"
    			+ "		padding:7px; border:#4e95f4 1px solid;\n"
    			+ "	}\n"
    			+ "	/* Define the default color for all the table rows */\n"
    			+ "	.hoverTable tr{\n"
    			+ "		background: #b8d1f3;\n"
    			+ "	}\n"
    			+ "	/* Define the hover highlight color for the table row */\n"
    			+ "    .hoverTable tr:hover {\n"
    			+ "          background-color: #ffff99;\n"
    			+ "    }\n"
    			+ "</style>"
    			//+ "window.opener.location.reload(false);\n"    			
    			+ " \n"
    			+ "<body>    <center>\n"
    			+ "        <h1 style=\"color: green;\">\n"
    			+ "          Concern Rules DB Browsing\n"
    			+ "        </h1>\n"
    			
    			+ "    </center>\n<br /> <br /><br />"
    			 + "<h4> " + ConcernApp.storageManager.getRules() + "</h4>"
    			+ "    \n"
    			+ "</body>\n"
    			+ "</html>\n";
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response biecointerface(
			String message,
			@Context HttpHeaders headers) {

    	JSONObject bodyMessage = new JSONObject(message);
    	if (((String)bodyMessage.get("messageType")).compareTo("loadRules") == 0 ) {
	    		if (loadRule(
	    				((JSONObject) bodyMessage)
	    					.get("event").toString()
    					))
	 			return Response.status(200).entity("Rule/s sent correctly").build();	
    	}
	return Response.status(401).entity("error").build();
	}
    
    private boolean loadRule(String rule) {
        	Consumer internalConsumer = new Consumer();
        	return internalConsumer.run(rule);
        }    
}

