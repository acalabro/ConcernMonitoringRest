package it.cnr.isti.labsedc.concern.rules;

import javax.jms.JMSException;
import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.consumer.ConcernAbstractConsumer;
import it.cnr.isti.labsedc.concern.event.ConcernDTForecast;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RulesGenerator {
	
	private static Logger logger = LogManager.getLogger(RulesGenerator.class);

	public static String brokerUrl = "tcp://0.0.0.0:61616";
	public static RulesGenerator ruleGen;

	public RulesGenerator(String brokerUrl) {
		RulesGenerator.brokerUrl = brokerUrl;
	}
	
	public static void main(String[] args) {
		
		ruleGen = new RulesGenerator("tcp://0.0.0.0:61616");

		ConcernDTForecast<String> dtForecast = new ConcernDTForecast<String>(
				System.currentTimeMillis(),
				"DigitaTwin",
				"Monitoring", "sessionID", "1234", "DTForecasting", 
				"Velocity,Velocity,Score, Velocity,Score,Score", 
				CepType.DROOLS, false, "5","SUA_Probe","PATTERN","0");
		
//		ConcernDTForecast<String> dtEvent = new ConcernDTForecast<String>(
//				System.currentTimeMillis(),
//				"DigitalTwin",
//				"destinationID", "sessionID", "1234", "name", "data", CepType.DROOLS, previousDTEvent);
//		
//		System.out.println(RulesGenerator.createRule(dtEvent));
		
		RulesGenerator.generateRuleFromDTForecast(dtForecast);

		
	}
	
	
	public static void generateRuleFromDTForecast(ConcernDTForecast<String> forecast) {
		
    	System.out.println("------------------------------------------------------");
    	logger.info("-------------Received forecasting from DT-------------");
    	System.out.println("------------------------------------------------------");
		RulesGenerator.injectRule(RulesGenerator.createRule(forecast), forecast.getSessionID());
		System.out.println(RulesGenerator.createRule(forecast));
		
	}

	private static String createRule(ConcernDTForecast<String> forecast) {
		
		String packages = "package it.cnr.isti.labsedc.concern.event;\n\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;\n"
				+ "import it.cnr.isti.labsedc.concern.utils.KieLauncher;\n"
				+ "\n"
				+ "dialect \"java\"\n"
				+ "\n"
				+ "declare ConcernBaseEvent\n"
				+ "    @role( event )\n"
				+ "    @timestamp( timestamp )\n"
				+ "end\n\n";
		
		String header = "rule \"autogen-rule-"+ forecast.getSessionID() + "-"+ forecast.getSenderID() +"-rule\"\n"
				+ "	no-loop\n"
				+ "	salience 10\n"
				+ "	dialect \"java\"\n"
				+ "\n\twhen\n\n";
		
		String[] forecastedEvents = forecast.getData().split(",");
		
		//create an event foreach forecasted event
		String when="";
		if (forecastedEvents.length>0) {
		
			when = when + 	
					"	$0Event : ConcernBaseEvent(\n"
					+ "	this.getName == \"" + forecastedEvents[0].toString().trim()+ "\",\n"
					+ " \tthis.getSenderID == \"" + forecast.getForecastedProbeName()+ "\",\n"
					+ "	this.getSessionID == \"" + forecast.getSessionID() + "\");\n"
					+ "	\n";			
		}
		
		if (forecastedEvents.length>1) {
			for (int i = 1; i<forecastedEvents.length;i++) {
			when = when + 	
					"	$"+ i + "Event : ConcernBaseEvent(\n"
					+ "	this.getName == \"" + forecastedEvents[i].toString().trim()+ "\",\n"
					+ " \tthis.getSenderID == \"" + forecast.getForecastedProbeName()+ "\",\n"
					+ "	this.getSessionID == \"" + forecast.getSessionID() + "\",\n"
					+ " \tthis after $"+ (i-1) + "Event);\n\n";
			}
		}

		String then = "";

		then = "\tthen\n\n"
					+ "\tKieLauncher.printer(\"rule autogen-rule-"+ forecast.getSessionID() + "-"+ forecast.getSenderID() +"-rule matched\");\n"
					+ "end";		
		return packages + header + when + then;
	}

	static void injectRule(String rulesToLoad, String sessionID) {
		ConcernAbstractConsumer cons = new ConcernAbstractConsumer();
		try {
			cons.init(RulesGenerator.brokerUrl,"vera", "griselda");
			ConcernEvaluationRequestEvent<String> ruleToEvaluate = 
					new ConcernEvaluationRequestEvent<String>(
							System.currentTimeMillis(),"Rules-Generator", "Monitoring", 
							sessionID, "checksum", "EvaluationRequest", 
							rulesToLoad,
							CepType.DROOLS, false, "Auto-generated Rule", ChannelProperties.GENERICREQUESTS);
									
			cons.sendEvaluationRequest("DROOLS-InstanceOne", ruleToEvaluate);

	    	System.out.println("------------------------------------------------------");
	    	logger.info("-------------Auto-generated rule sent for injection-------------");
	    	System.out.println("------------------------------------------------------");
	    	
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

//	private static String calculateCheckSum(String rulesToLoad) {
//		Checksum crc32 = new CRC32();
//	    crc32.update(rulesToLoad.getBytes(), 0, rulesToLoad.getBytes().length);
//	    return String.valueOf(crc32.getValue());
//	}
	
}
