package it.cnr.isti.labsedc.concern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.jms.JMSException;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.consumer.ConcernAbstractConsumer;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;

public class Consumer {

	public static void main(String[] args) throws InterruptedException {
		String brokerUrl = "tcp://localhost:61616";

		ConcernAbstractConsumer cons = new ConcernAbstractConsumer();
		try {
			cons.init(brokerUrl,"vera", "griselda");
			
			//String ruleToPut = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/rules/genericRulesList/moreThanOneConn.drl");
			//String ruleToPut = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/rules/genericRulesList/coordinates.drl");
			//String ruleToPut = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/rules/genericRulesList/localGlobalAvgDelayCheck.drl");
			//String ruleToPut = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/rules/genericRulesList/digitalTwin.drl");
			String ruleToPut = Consumer.readFile(System.getProperty("user.dir")+ "/src/main/resources/autogenRule.drl");
			ConcernEvaluationRequestEvent<String> ruleToEvaluate = 
					new ConcernEvaluationRequestEvent<String>(
							System.currentTimeMillis(),"Consumer-ONE", "monitoring", 
							"session-ONE", "2392397923", "EvaluationRequest", 
							ruleToPut,
							CepType.DROOLS, "double event", ChannelProperties.GENERICREQUESTS);
							
			
			cons.sendEvaluationRequest("DROOLS-InstanceOne", ruleToEvaluate);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		System.out.println("Rule to be monitored Sent");
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
