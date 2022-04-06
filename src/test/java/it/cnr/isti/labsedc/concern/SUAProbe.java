package it.cnr.isti.labsedc.concern;

import java.net.UnknownHostException;
import java.util.Properties;
import javax.jms.JMSException;
import javax.naming.NamingException;
import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;

public class SUAProbe extends ConcernAbstractProbe {

	public SUAProbe(Properties settings) {
		super(settings);
	}

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		//creating a probe
		SUAProbe aGenericProbe = new SUAProbe(
				Manager.createProbeSettingsPropertiesObject(
						"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
						"tcp://bieco.holisun.com:61616","system", "manager",
						"TopicCF","DROOLS-InstanceOne", false, "SUA_probe",	
						"it.cnr.isti.labsedc.concern,java.lang,javax.security,java.util",
						"vera", "griselda"));
		//sending events
		try {
			DebugMessages.line();
			DebugMessages.println(System.currentTimeMillis(), SUAProbe.class.getSimpleName(),"Sending SUA messages");

			sendVelocityMessage(aGenericProbe, "0.1");
			sendScoreMessage(aGenericProbe, "0.1f");
			sendConnectionEventMessage(aGenericProbe);
			sendDisconnectionEventMessage(aGenericProbe);
			
			
		} catch (IndexOutOfBoundsException | NamingException e) {} catch (JMSException e) {
			e.printStackTrace();
		} 
	}

	protected static void sendVelocityMessage(SUAProbe aGenericProbe, String speed) throws JMSException, NamingException {
		aGenericProbe.sendEventMessage(new ConcernBaseEvent<String>(System.currentTimeMillis(),
				"probeOnSUA","Monitoring","noSession","noChecksum","Velocity", speed,
				CepType.DROOLS, "anotherPropertyToFill"), false);
	DebugMessages.line();		
	}

	protected static void sendScoreMessage(SUAProbe aGenericProbe, String score) throws JMSException, NamingException {
		aGenericProbe.sendEventMessage(new ConcernBaseEvent<String>(System.currentTimeMillis(),
				"probeOnSUA","Monitoring","noSession","noChecksum","Score", score,
				CepType.DROOLS, "anotherPropertyToFill"), false);
	DebugMessages.line();		
	}

	protected static void sendConnectionEventMessage(SUAProbe aGenericProbe) throws JMSException, NamingException {
		aGenericProbe.sendEventMessage(new ConcernBaseEvent<String>(System.currentTimeMillis(),
				"probeOnSUA","Monitoring","noSession","noChecksum","Connection", "established",
				CepType.DROOLS, "anotherPropertyToFill"), false);
	DebugMessages.line();		
	}
	
	protected static void sendDisconnectionEventMessage(SUAProbe aGenericProbe) throws JMSException, NamingException {
		aGenericProbe.sendEventMessage(new ConcernBaseEvent<String>(System.currentTimeMillis(),
				"probeOnSUA","Monitoring","noSession","noChecksum","Connection", "closed",
				CepType.DROOLS, "anotherPropertyToFill"), false);
	DebugMessages.line();		
	}
	
	@Override
	public void sendMessage(ConcernBaseEvent<?> event, boolean debug) {
	}	
}