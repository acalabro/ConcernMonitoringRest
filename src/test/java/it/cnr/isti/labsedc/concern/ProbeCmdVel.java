package it.cnr.isti.labsedc.concern;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernCmdVelEvent;

public class ProbeCmdVel {

	public static void main(String[] args) throws InterruptedException {
		//String brokerUrl = "tcp://146.48.84.225:61616";
		String brokerUrl = "tcp://146.48.:61616";

		testCmdVelProbe(brokerUrl);
		
		System.out.println("SENT");
	}

	public static void testCmdVelProbe(String brokerUrl) throws InterruptedException {

		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vera","griselda", brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("DROOLS-InstanceOne");
            MessageProducer producer = session.createProducer(topic);     
			ObjectMessage msg = session.createObjectMessage();
			
			
			ConcernCmdVelEvent<String> event = new ConcernCmdVelEvent<String>(
					System.currentTimeMillis(), 
					"RobotSensor", "Monitoring", "theSessionID", 
					"neverMindNow", "VelocityEvent", "anInformationRelatedToThisEventCategory",
					CepType.DROOLS, false, 0f,0.1f,0f,0.1f,0f,0.2f);
			
 				msg.setObject(event);
				producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}
