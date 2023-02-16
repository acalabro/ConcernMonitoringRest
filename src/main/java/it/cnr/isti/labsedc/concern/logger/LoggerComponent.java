package it.cnr.isti.labsedc.concern.logger;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public final class LoggerComponent {

   protected static final Logger parentLogger = LogManager.getLogger();
   private Logger logger = parentLogger;

   public LoggerComponent() {
   }

   public Logger getLogger() {
       return logger;
   }

   protected void setLogger(Logger logger) {
       this.logger = logger;
   }


   public void log(Marker marker) {
      logger.debug(marker,"Parent log message");
   }

   public static void cleanFile(String fileName) {
   		java.io.FileWriter fw;
   		java.io.PrintWriter pw;
		try {
			fw = new java.io.FileWriter(fileName, false);
	   		pw = new java.io.PrintWriter(fw, false);
	   		pw.flush();
	   		pw.close();   	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
   }
   		
}
