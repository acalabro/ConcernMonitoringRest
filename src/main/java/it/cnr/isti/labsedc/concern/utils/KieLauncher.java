package it.cnr.isti.labsedc.concern.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernProbeEvent;

public class KieLauncher {

	private static Logger logger = LogManager.getLogger(KieLauncher.class);

	
	public KieLauncher() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException {
		DroolsKieTest asd = new DroolsKieTest();
		asd.start();
		
		DroolsKieTest.loadRule();
		
		Thread.sleep(1000);
		System.out.println("ora mando");
		ConcernProbeEvent<String> evt = new ConcernProbeEvent<String>(
				System.currentTimeMillis(), 
				"senderA", "destinationA", "sessionA", 
				"checksum",
				"SLA Alert", "brumbrum", CepType.DROOLS, false,"open");
		DroolsKieTest.insertEvent(evt);
		Thread.sleep(1000);
		System.out.println("ora mando altro");
		ConcernProbeEvent<String> evt2 = new ConcernProbeEvent<String>(
				System.currentTimeMillis(), 
				"senderA", "destinationA", "sessionA", 
				"checksum",
				"load_one", "brumbrum", CepType.DROOLS, false,"open");
		DroolsKieTest.insertEvent(evt2);
		Thread.sleep(2000);
	}

	public static void printer(String toprint) {
		System.out.println(toprint);
	}
	public static void printWarning(String toPrint) {
		logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		logger.info(toPrint);
		logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	public static void printInfo(String toPrint) {
		logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		logger.info(toPrint);
		logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	}
	
	public static void printEvent(Object evt) {
		logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		logger.info(evt.toString());
		logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	}
}
