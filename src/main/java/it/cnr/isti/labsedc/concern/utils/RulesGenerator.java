package it.cnr.isti.labsedc.concern.utils;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.jms.JMSException;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.consumer.ConcernAbstractConsumer;
import it.cnr.isti.labsedc.concern.event.ConcernDTEvent;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;

public class RulesGenerator {

	public static String brokerUrl = "tcp://0.0.0.0:61616";
	public static RulesGenerator ruleGen;

	public RulesGenerator(String brokerUrl) {
		RulesGenerator.brokerUrl = brokerUrl;
	}
	
	public static void main(String[] args) {
		
		ruleGen = new RulesGenerator("tcp://0.0.0.0:61616");

		ConcernDTEvent<String> previousDTEvent = new ConcernDTEvent<String>(
				System.currentTimeMillis(),
				"DigitalTwin",
				"destinationID", "sessionID", "1234", "name", "data", CepType.DROOLS, null);
		
		ConcernDTEvent<String> dtEvent = new ConcernDTEvent<String>(
				System.currentTimeMillis(),
				"DigitalTwin",
				"destinationID", "sessionID", "1234", "name", "data", CepType.DROOLS, previousDTEvent);
		
		System.out.println(RulesGenerator.createRule(dtEvent));
		RulesGenerator.generateRuleFromDTEvent(dtEvent);

		
	}
	
	
	public static void generateRuleFromDTEvent(ConcernDTEvent<?> event) {
		
		System.out.println("called");
		RulesGenerator.injectRule(RulesGenerator.createRule(event), event.getSessionID());
		
	}

	private static String createRule(ConcernDTEvent<?> event) {
		
		String packages = "package it.cnr.isti.labsedc.concern.event;\n\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernProbeEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.KieLauncher;\n"
				+ "\n"
				+ "dialect \"java\"\n"
				+ "\n"
				+ "declare ConcernBaseEvent\n"
				+ "    @role( event )\n"
				+ "    @timestamp( timestamp )\n"
				+ "end\n\n";
		
		String header = "rule \""+ event.getName() + "-" + event.getSessionID() + "-rule\"\n"
				+ "	no-loop\n"
				+ "	salience 10\n"
				+ "	dialect \"java\"\n";

		String when = "";
		
		if (event.getPrevious() == null) {
			
			when = "\n\twhen\n\n"
					+ "	$aEvent : ConcernBaseEvent(\n"
					+ "	this.getName == \"" + event.getName() + "\",\n"
					+ "	this.getData == \"" + event.getData() + "\",\n"
					+ " \tthis.getSenderID == \"" + event.getSenderID() + "\",\n"
					+ "	this.getSessionID == \"" + event.getSessionID() + "\");";
		} else {
		
		when = "\n\twhen\n\n"
				+ "	$aEvent : ConcernBaseEvent(\n"
				+ "	this.getName == \"" + event.getPrevious().getName() + "\",\n"
				+ "	this.getData == \"" + event.getPrevious().getData() + "\",\n"
				+ " \tthis.getSenderID == \"" + event.getPrevious().getSenderID() + "\",\n"
				+ "	this.getSessionID == \"" + event.getPrevious().getSessionID() + "\");\n"
				+ "	\n"
				+ "	$bEvent : ConcernBaseEvent(\n"
				+ "	this.getName == \"" + event.getName() + "\",\n"
				+ "	this.getData == \"" + event.getData() + "\",\n"
				+ " \tthis.getSenderID == \"" + event.getSenderID() + "\",\n"
				+ "	this.getSessionID == \"" + event.getSessionID() + "\",\n"
				+ "	this after $aEvent);";
		}

		String then = "";

		if (event.getPrevious() == null) {
			then = "\n\n\tthen\n\n"
					+ "\tKieLauncher.printer(\"rule "+ event.getName() + "-" + event.getSessionID() + "-rule matched\");\n"
					+ "end";
		} else {
			then = "\n\n\tthen\n\n"
				+ "\tKieLauncher.printer(\"rule "+ event.getName() + "-" + event.getSessionID() + "-rule matched\");\n"
				+ "	retract($aEvent);\n"
				+ "end";		
		}
		
		return packages + header + when + then;
	}

	static void injectRule(String rulesToLoad, String sessionID) {
		ConcernAbstractConsumer cons = new ConcernAbstractConsumer();
		try {
			cons.init(RulesGenerator.brokerUrl,"vera", "griselda");
			ConcernEvaluationRequestEvent<String> ruleToEvaluate = 
					new ConcernEvaluationRequestEvent<String>(
							System.currentTimeMillis(),"Rules-Generator", "monitoring", 
							sessionID, RulesGenerator.calculateCheckSum(rulesToLoad), "EvaluationRequest", 
							rulesToLoad,
							CepType.DROOLS, "Auto-generated Rule", ChannelProperties.GENERICREQUESTS);
									
			cons.sendEvaluationRequest("DROOLS-InstanceOne", ruleToEvaluate);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		System.out.println("Rule to be monitored Sent");
	}

	private static String calculateCheckSum(String rulesToLoad) {
		Checksum crc32 = new CRC32();
	    crc32.update(rulesToLoad.getBytes(), 0, rulesToLoad.getBytes().length);
	    return String.valueOf(crc32.getValue());
	}
	
}
