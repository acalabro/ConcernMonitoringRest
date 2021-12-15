package it.cnr.isti.labsedc.concern;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernProbeEvent;

public class KieLauncher {

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
				"SLA Alert", "coccole", CepType.DROOLS,"open");
		DroolsKieTest.insertEvent(evt);
		Thread.sleep(1000);
		System.out.println("ora mando altro");
		ConcernProbeEvent<String> evt2 = new ConcernProbeEvent<String>(
				System.currentTimeMillis(), 
				"senderA", "destinationA", "sessionA", 
				"checksum",
				"load_one", "coccole", CepType.DROOLS,"open");
		DroolsKieTest.insertEvent(evt2);
		Thread.sleep(2000);
	}

	public static void printer(String toprint) {
		System.out.println(toprint);
	}
}
