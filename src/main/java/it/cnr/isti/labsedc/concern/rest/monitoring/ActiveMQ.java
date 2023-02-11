package it.cnr.isti.labsedc.concern.rest.monitoring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import it.cnr.isti.labsedc.concern.ConcernApp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/biecointerface/activemq")

public class ActiveMQ {
	
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String getIt() {
        return getIP();
        
    }
    
    
    private String getIP() {
	
    	if (ConcernApp.isRunning()) {
    		try {
				return InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}    	
    	return "Not Running";
    }
}

