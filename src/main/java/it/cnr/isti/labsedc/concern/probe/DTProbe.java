package it.cnr.isti.labsedc.concern.probe;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.event.ConcernDTForecast;
import it.cnr.isti.labsedc.concern.utils.DebugMessages;
import it.cnr.isti.labsedc.concern.utils.ConnectionManager;

public class DTProbe extends ConcernAbstractProbe {

	public DTProbe(Properties settings) {
		super(settings);
	}
	
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		DTProbe aGenericProbe = new DTProbe(
				ConnectionManager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						"tcp://localhost:61616","system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "DT_probe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), DTProbe.class.getSimpleName(),"Sending SUA messages");

			sendDTForecastEvents(aGenericProbe, "Velocity,Velocity,Score,Velocity", "5");

		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}

	protected static void sendDTForecastEvents(DTProbe aGenericProbe, String forecastedEvents, String confidenceIntervalInSeconds) throws JMSException,NamingException {

		DebugMessages.print(System.currentTimeMillis(), DTProbe.class.getSimpleName(),
					"Creating Message ");
		try
		{
			ConcernDTForecast<String> forecast = new ConcernDTForecast<String>(System.currentTimeMillis(),
					"DT_probe","Monitoring","noSession","noChecksum","DTForecasting", forecastedEvents,	
					CepType.DROOLS, confidenceIntervalInSeconds, "SUA_Probe");
			ObjectMessage messageToSend = publishSession.createObjectMessage();
			messageToSend.setJMSMessageID(String.valueOf(MESSAGEID++));
			messageToSend.setObject(forecast);
			DebugMessages.ok();
			DebugMessages.print(System.currentTimeMillis(), DTProbe.class.getSimpleName(),"Publishing message  ");
			mProducer.send(messageToSend);
			DebugMessages.ok();
			DebugMessages.line();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(ConcernBaseEvent<?> event, boolean debug) {
		// TODO Auto-generated method stub
		
	}
}
