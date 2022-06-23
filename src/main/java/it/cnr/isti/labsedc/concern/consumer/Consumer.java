package it.cnr.isti.labsedc.concern.consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jms.JMSException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;

public class Consumer extends Thread{

	private static Logger logger;

	public void run() {
		sendRule();
	}
	
	private static void sendRule() {
		String brokerUrl = "tcp://localhost:61616";

		ConcernAbstractConsumer cons = new ConcernAbstractConsumer();
		try {
			cons.init(brokerUrl,"vera", "griselda");
			String digitalTwin = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/rules/genericRulesList/digitalTwin.drl");
			logger.info("sending rule");
			cons.sendEvaluationRequest("DROOLS-InstanceOne", sendingRule(digitalTwin, "DTMetaRule"));
			logger.info("rule sent");
			Thread.sleep(1000);
			logger.info("sending rule");
			String onlyOneConn = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/rules/genericRulesList/onlyOneConn.drl");
			cons.sendEvaluationRequest("DROOLS-InstanceOne", sendingRule(onlyOneConn,"onlyOneConnectionInLocalPlanner"));
			logger.info("rule sent");
			
		} catch (JMSException | InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Rule to be monitored Sent");		
	}

	public static void main(String[] args) throws InterruptedException {
		logger = LogManager.getLogger(Consumer.class);
		sendRule();
	}
	
	private static ConcernEvaluationRequestEvent<String> sendingRule(String ruleToPut, String ruleName) {
		ConcernEvaluationRequestEvent<String> ruleToEvaluate = 
				new ConcernEvaluationRequestEvent<String>(
						System.currentTimeMillis(),"Consumer-ONE", "monitoring", 
						"session-ONE", "2392397923", "EvaluationRequest", 
						ruleToPut,
						CepType.DROOLS, false, ruleName, ChannelProperties.GENERICREQUESTS);
		
    	System.out.println("------------------------------------------------------");
    	System.out.println("Sending rule:"+ ruleName + " to the Complex Event Processor");
    	System.out.println("------------------------------------------------------");
		
		return ruleToEvaluate;
	}
	
	private static String readFile(String ruleFile) {

		String rule="";
		
		try {
		Path fileName = Path.of(ruleFile);

	    rule = Files.readString(fileName);
	    		} catch (IOException e) {
			e.printStackTrace();
		}
	    return rule;

	}
}


/*ConcernEvaluationRequestEvent<String> ruleToEvaluate = 
new ConcernEvaluationRequestEvent<String>(
		System.currentTimeMillis(),"Consumer-ONE", "monitoring", "session-ONE", "2392397923", "EvaluationRequest", "//created on: Nov 08, 2021\n"
				+ "package it.cnr.isti.labsedc.concern.event;\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.notification.NotificationManager;\n"
				+ "\n"
				+ "dialect \"java\"\n"
				+ "\n"
				+ "declare ConcernBaseEvent\n"
				+ "    @role( event )\n"
				+ "    @timestamp( timestamp )\n"
				+ "end\n"
				+ "            \n"
				+ "		rule \"SERVICE_NAME_MACHINE_IP_INFRASTRUCTUREVIOLATION\"\n"
				+ "		no-loop\n"
				+ "		salience 10\n"
				+ "		dialect \"java\"\n"
				+ "		when\nSLA Alert\");\n"
				+ "			\n"
				+ "			$bEvent : ConcernBaseEvent(\n"
				+ "			this.getName == \"overload_one\",\n"
				+ "			this after[0,10s] $aEvent);\n"
				+ "		then\n"
				+ "			NotificationManager.NotifyToConsumer(\"consumerName\", \"Rules enacted - violation occurs\");	\n"
				+ "			retract($aEvent);\n"
				+ "			retract($bEvent);	\n"
				+ "		end"
				+ "", CepType.DROOLS, "monitor a after b", ChannelProperties.GENERICREQUESTS);*/
