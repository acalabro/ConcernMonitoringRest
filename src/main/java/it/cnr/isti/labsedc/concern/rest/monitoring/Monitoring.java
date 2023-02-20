package it.cnr.isti.labsedc.concern.rest.monitoring;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    			+"<style type =\"text/css\">\n"		
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
    + "<style>\n"
    + "* {\n"
    + "  box-sizing: border-box;\n"
    + "}\n"
    + "\n"
    + "/* Create two equal columns that floats next to each other */\n"
    + ".column {\n"
    + "  float: left;\n"
    + "  width: 33%;\n"
    + "  padding: 10px;\n"
    + "  height: 300px; /* Should be removed. Only for demonstration */\n"
    + "}\n"
    + "\n"
    + "/* Clear floats after the columns */\n"
    + ".row:after {\n"
    + "  content: \"\";\n"
    + "  display: table;\n"
    + "  clear: both;\n"
    + "}\n"
    + "</style>"
    + "  <style>      "			
    + ".textarea {\n"
    + "  background-color: #dddddd;\n"
    + "  color: #000000;\n"
    + "  padding: 1em;\n"
    + "  border-radius: 10px;\n"
    + "  border: 2px solid transparent;\n"
    + "  outline: none;\n"
    + "  font-family: \"Heebo\", sans-serif;\n"
    + "  font-weight: 500;\n"
    + "  font-size: 12px;\n"
    + "  line-height: 1.4;\n"
    + "  transition: all 0.2s;\n"
    + "}\n"
    + "\n"
    + ".textarea:hover {\n"
    + "  cursor: pointer;\n"
    + "  background-color: #000000;\n"
    + "	 color: #ffffff;"
    + "}\n"
    + "\n"
    +".tab2 {\n"
    + "            tab-size: 4;\n"
    + " margin-left:25px;"
    + " margin-right:25px;"    
    + "        }"
    + ".textarea:focus {\n"
    + "  cursor: text;\n"
    + "  color: #333333;\n"
    + "  background-color: white;\n"
    + "  border-color: #333333;\n"
    + "}"
    + "#ruleslist{\n"
    + "  width:250px;   \n"
    + "  font-size:1em;\n"
    + "}"
    + "</style>"		

    			+"<script>\n"
    			+ "\n"    			
    			+ "setTimeout(\"location.reload(true);\", 45000);\n"
    			+ "\n"
    			+ "</script>"
//    			+ "<script>"
//    			+ "	function suca() {"
//+ "var theArea = document.getElementsByName(\"debugLog\"); \n" 
//+ "var length =  document.getElementsByName(\"debugLog\").value.length;"
//    			+"}"
//    			+ "</script>"
    			+"<body><center><h2 style=\"color: green;\">Runtime Monitoring</h2><h3>Status: " + MonitoringStatus() + "</h3>"

    			+ "<div id=\"logs\">"
    				+ "<textarea class=\"textarea\" name=\"debugLog\" rows=\"25\" cols=\"190\">\n"
					+ getLoggerData() + "</textarea>"
				+ "</div><br />"

    			+ "<div class=\"row\">"
    				
    				+ "<div class=\"column\"> "
    				+ 	"<h2><font color=\"RED\">Component status:</font></h2>"
    					+ getComponentStatus() + "\n"
    				+ "</div>"
    				
    				+"<div class=\"column\">"
	    				+ "<br /><button class=\"tab2\"onclick=\"startMonitoring()\" id=\"startMonitoring\" "+
    				getStartStatus() + 
    				">Start monitoring</button>\n\t\t\t\t"
	    				+ "<button class=\"tab2\"onclick=\"stopMonitoring()\" id=\"stopMonitoring\" "+ 
    				getStopStatus() +
    				">Stop monitoring</button>\n\t\t\t\t"
	    				+"<br /><br /><button class=\"tab2\"onclick=\"loadFunction()\" id=\"loadButton\" " + 
    				getHiddenStatus() +
    				" >Load rules</button>\n\t\t\t\t"
	    				+"<br /><br /><button id=\"deleteButton\" onclick=\"deleteFunction()\" " + 
    				getHiddenStatus() +
    				">Delete rules</button>\n\t\t\t\t"
	    				+"<br /><br /><button id=\"notificationButton\" onclick=\"notificationFunction()\">Notifications</button><br />"
    				+ "</div>"
    				
    				+ "<div class=\"column\">"
    					+ "<h3>Rules loaded: " 
    					+ getAmountOfLoadedRules() + 
    					"</h3>"
    					+ "<h4>Loaded rules list:</h4>" + 
    					getRulesList()
    				+ "</div>"
				
    			+"</div>"		
				+ "    	<script>\n"
				+ "    	function loadFunction() {\n"
				+ "    	    window.open(\"./biecointerface/loadrules\", \"_blank\", \"toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=750,height=550\");\n"
				+ "    	}\n"
				+ "    	</script>"
				+ "    	<script>\n"
				+ "    	function notificationFunction() {\n"
				+ "    	    window.open(\"./biecointerface/notifications\", \"_blank\", \"toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=700,height=520\");\n"
				+ "    	}\n"
				+ "    	</script>"
				+ "    	<script>\n"
				+ "    	function deleteFunction() {\n"
				+ "    	    window.open(\"./biecointerface/deleterules\", \"_blank\", \"toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=500,height=600\");\n"
				+ "    	}\n"
				+ "    	</script>"
				+ "    	<script>\n"
				+ "    	function stopMonitoring() {\n"
				+" var xhttp = new XMLHttpRequest();\n"
		    			+ "    xhttp.onreadystatechange = function() {\n"
		    			+ "         if (this.readystate == XMLHttpRequest.DONE && this.status == 200) {\n"
		    			+ "         }\n"
		    			+ "    };\n"
		    			+ "    xhttp.open(\"POST\", \"http://" + 
		    			ConcernApp.IPAddressWhereTheInstanceIsRunning + ":" + 
		    			ConcernApp.PortWhereTheInstanceIsRunning + "/monitoring/biecointerface\", true);\n"
		    			+ "	   xhttp.setRequestHeader('Authorization','746gfbrenkljhGU');"
		    			+ "    xhttp.setRequestHeader(\"Content-type\", \"application/json\");\n"
		    			+ "    xhttp.send(JSON.stringify({"
		    			+ "    \"jobID\": \"1234\",\n"
		    			+ "    \"timestamp\": \"2023-01-18 08:29:30\",\n"
		    			+ "    \"messageType\": \"Stop\",\n"
		    			+ "    \"sourceID\": \"4\",\n"
		    			+ "    \"event\": \"stop Message\",\n" 
		    			+ "    \"crc\": 1234565\n"
		    			+ "    }));\n"
		    			+"window.alert(\"Monitoring stop\")\n"
		    			+"window.location.reload();\n"
		    			+ "}"    			
				+ "    	</script>"
				+ "    	<script>\n"
				+ "    	function startMonitoring() {\n"
				+" var xhttp = new XMLHttpRequest();\n"
		    			+ "    xhttp.onreadystatechange = function() {\n"
		    			+ "         if (this.readystate == XMLHttpRequest.DONE && this.status == 200) {\n"
		    			+ "         }\n"
		    			+ "    };\n"
		    			+ "    xhttp.open(\"POST\", \"http://" + 
		    			ConcernApp.IPAddressWhereTheInstanceIsRunning + ":" + 
		    			ConcernApp.PortWhereTheInstanceIsRunning + "/monitoring/biecointerface\", true);\n"
		    			+ "	   xhttp.setRequestHeader('Authorization','746gfbrenkljhGU');"
		    			+ "    xhttp.setRequestHeader(\"Content-type\", \"application/json\");\n"
		    			+ "    xhttp.send(JSON.stringify({"
		    			+ "    \"jobID\": \"1234\",\n"
		    			+ "    \"timestamp\": \"2023-01-18 08:29:30\",\n"
		    			+ "    \"messageType\": \"Start\",\n"
		    			+ "    \"sourceID\": \"4\",\n"
		    			+ "    \"event\": \"start Message\",\n" 
		    			+ "    \"crc\": 1234565\n"
		    			+ "    }));\n"
		    			+"window.alert(\"Monitoring starts\")\n"
		    			+"window.location.reload();\n"
		    			+ "}"    			
				+ "    	</script>"
				+ "<br /></center></body></html>";  	  
    
    }
     
	private String getComponentStatus() {
		if (ConcernApp.isRunning()) {
			return ConcernApp.getStartedComponentsList();
		}
		else
			return "";
	}

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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Starting Demo";
	}
	
	private String getRulesList() {
		return ConcernApp.getRulesList();
	}
	
	private String getAmountOfLoadedRules() {
			return Integer.toString(ConcernApp.getAmountOfLoadedRules());
	}
	
	private String getHiddenStatus() {
		if (!ConcernApp.isRunning()) {
			return "hidden"; 
			}
		return "";
	}
	
	private String getStartStatus() {
		if (ConcernApp.isRunning()) {
			return "hidden"; 
			}
		return "";
	}
	
    private String getStopStatus() {
		if (!ConcernApp.isRunning()) {
			return "hidden"; 
			}
		return "";
	}
    
    public static String getLocalIP() {
    	//Enumeration<?> e;
		try {
//			e = NetworkInterface.getNetworkInterfaces();
//    	while(e.hasMoreElements())
//    	{
//    	    NetworkInterface n = (NetworkInterface) e.nextElement();
//    	    Enumeration<InetAddress> ee = n.getInetAddresses();
//    	    String i= "";
//    	    while (ee.hasMoreElements())
//    	    {
//    	        i = ee.nextElement().getHostAddress();
//    	    }
//    	    if (i!= null) {
//    	    return i;
//		}
		return InetAddress.getLocalHost().getHostName();
    	} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
    }
}

