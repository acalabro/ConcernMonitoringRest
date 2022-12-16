package it.cnr.isti.labsedc.concern.utils;

public class ComplexSimplaRulesScriptCreator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		for (int i = 0; i<300;i++) {
			System.out.println(
					"rule \"autogenRule"+i + "\""+
					"\nno-loop"+
					"\nsalience 10"+
					"\ndialect \"java\""+
					"\nwhen"+
					"\n$aEvent : ConcernDTForecast("+
					"\nthis.getSenderID == \"DigitalTwin\","+
					"\nthis.getDestinationID == \"Monitoring\");"+
					"\nthen"+
					"\nRulesGenerator.generateRuleFromDTForecast($aEvent);"+	
					"\nretract($aEvent);	"+
					"\nend"+
					"\n");
			
			System.out.println(
"	rule \"localGlobalAvgDelayCheck"+i + "\"\n"
+ "	no-loop\n"
+ "	salience 10\n"
+ "	dialect \"java\"\n"
+ "	when\n"
+ "	\n"
+ "		$aEvent: ConcernBaseEvent(this.getSenderID == \"LocalPlanner\", \n"
+ "									this.getDestinationID == \"GlobalPlanner\");\n"
+ "									\n"
+ "		$bEvent: ConcernBaseEvent(this.getSenderID == \"GlobalPlanner\",\n"
+ "									this.getSessionID == $aEvent.getSessionID,\n"
+ "									this.getData == $aEvent.getData,\n"
+ "									this.getName == $aEvent.getName,\n"
+ "									this.getChecksum == $aEvent.getChecksum,\n"
+ "									this after $aEvent);\n"
+ "	then					\n"
+ "		LatencyEvent warning = new LatencyEvent();\n"
+ "		warning.setLatency(\n"
+ "			Calculator.latency(\n"
+ "				$bEvent.getTimestamp(),\n"
+ "                $aEvent.getTimestamp()));\n"
+ "		KieLauncher.printer(\"latencyEventCreated \" + warning.getLatency());\n"
+ "		\n"
+ "		insert(warning);	                    \n"
+ "		retract($aEvent);\n"
+ "    	retract($bEvent);	\n"
+ "	end\n"
+ "	\n"
+ "	rule \"calculateAverage" + i + "\"\n"
+ "	no-loop\n"
+ "	salience 5\n"
+ "	dialect \"java\"\n"
+ "	when\n"
+ "\n"
+ "	    Number( longValue > 20 ) from accumulate(\n"
+ "	        LatencyEvent( $latency : latency ) over window:length( 10 ),\n"
+ "	        average( $latency ) )\n"
+ "	then\n"
+ "		KieLauncher.printer(\"Boundary violated\");\n"
+ "	end"
					);
			
		}
	}

}
