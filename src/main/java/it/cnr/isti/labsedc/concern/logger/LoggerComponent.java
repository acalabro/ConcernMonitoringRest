package it.cnr.isti.labsedc.concern.logger;

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



}
