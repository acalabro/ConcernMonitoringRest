package it.cnr.isti.labsedc.concern.rest.monitoring;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import it.cnr.isti.labsedc.concern.ConcernApp;

@Path("monitoring/biecointerface/notifications")


public class Notifications {
	
	 @GET
	    @Produces({MediaType.TEXT_HTML})
	    public String getIt() {
	        return homePage();     
	    }

	 private String homePage() {
		return"<!DOCTYPE html><head><meta charset=\"utf-8\"><title>Runtime Monitoring</title>"
				+ "</head>"
				+"<style type =\"text/css\">\n"
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
			    + "}</style>"	
			    +"<style type=\"text/css\">\n"
				+ "div {\n"
				+ "	display: flex;\n"
				+ "	flex-direction: column;\n"
				+ "	align-items: center;\n"
				+ "}\n"
				+ "</style>"
				+"<script>\n"
				+ "\n"    			
				+ "setTimeout(\"location.reload(true);\", 45000);\n"
				+ "\n"
				+ "</script>"
				+"<body bgcolor=”#800000\"><center><h2 style=\"color: green;\">Runtime Monitoring Notifications</h2><h3 style=\"color: white;\">" 
				+ "<div id=\"logs\"><textarea class=\"textarea\" name=\"debugLog\" rows=\"14\" cols=\"80\">\n"
				+ getNotificationData() + "</textarea></div><br />"
				+ "<br /></center></body></html>";  
	 }
	 
	 private String getNotificationData() {
		 
			return ConcernApp.getNotificationData();
		}
	 	 
}
