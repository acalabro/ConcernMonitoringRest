package it.cnr.isti.labsedc.concern.rest.monitoring;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
				+ "setTimeout(\"location.reload(true);\", 3000);\n"
				+ "\n"
				+ "</script>"
				+"<body bgcolor=”#800000\"><center><h2 style=\"color: green;\">Runtime Monitoring Notifications</h2><h3 style=\"color: white;\">" 
				+ "<div id=\"logs\"><textarea class=\"textarea\" name=\"debugLog\" rows=\"20\" cols=\"120\">\n"
				+ getNotificationData() + "</textarea></div><br />"
				+ "<button onClick=\"window.location.reload();\">Reload Notifications</button>"
				+ "<br /></center></body></html>";  
	 }
	 
	 private String getNotificationData() {
		 
			return ConcernApp.getNotificationData();
		}
	 	 
}
