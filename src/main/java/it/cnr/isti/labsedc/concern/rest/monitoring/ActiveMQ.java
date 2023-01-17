package it.cnr.isti.labsedc.concern.rest.monitoring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import it.cnr.isti.labsedc.concern.ConcernApp;

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

