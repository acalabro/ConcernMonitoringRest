package it.cnr.isti.labsedc.concern.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import it.cnr.isti.labsedc.concern.utils.Sub;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://0.0.0.0:8181/";
	private static HttpServer server;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String serverUri) {
        // create a resource config that scans for JAX-RS resources and providers
        // in it.cnr.isti.labsedc.concern.rest package
        final ResourceConfig rc = new ResourceConfig().packages("it.cnr.isti.labsedc.concern.rest");

    	System.out.println("Current IP: " + BASE_URI );
    	
    	/*CLEAN logs*/
    	Sub.cleanFile(System.getProperty("user.dir")+ "/logs/app-debug.log");
    	Sub.cleanFile(System.getProperty("user.dir")+ "/logs/app-info.log");
    	Sub.cleanFile(System.getProperty("user.dir")+ "/logs/notification-info.log");
    	Sub.cleanFile(System.getProperty("user.dir")+ "/logs/storage-info.log");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(serverUri), rc);
    }
    
    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	try {
    		if (args.length>0) {
        		setServer(startServer(args[0]));
        		Thread.currentThread().join();
        	}
    	else {
    		setServer(startServer(BASE_URI));
           	Thread.currentThread().join();
    		}
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
    	System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
    }

	public static HttpServer getServer() {
		return server;
	}

	public static void setServer(HttpServer server) {
		Main.server = server;
	}
}

