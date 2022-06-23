package it.cnr.isti.labsedc.concern.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
	//public static final String BASE_URI = "http://0.0.0.0:8181/";
	public static final String BASE_URI = "http://gattoboy.isti.cnr.it:8181/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String serverUri) {
        // create a resource config that scans for JAX-RS resources and providers
        // in it.cnr.isti.labsedc.concern.rest package
        final ResourceConfig rc = new ResourceConfig().packages("it.cnr.isti.labsedc.concern.rest");

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
    		HttpServer server;
    		if (args.length>0) {
        		server = startServer(args[0]);
        		Thread.currentThread().join();
        	}
    	else {
    		server = startServer(BASE_URI);
           	Thread.currentThread().join();
    		}
        } catch (InterruptedException e) {
		// TODO Auto-generated catch block
        	e.printStackTrace();
        }
    	System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
//        System.in.read();
//        server.stop();
    }
}

