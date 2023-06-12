package it.cnr.isti.labsedc.concern;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;

public class Probe {

	private static Logger logger = LogManager.getLogger(Probe.class);

	public static void testProbe(String brokerUrl, String topicName, 
									String username, String password, 
									String eventData, String eventName,
									String extension, String checksum,
									String sessionID, String sender,
									String destination) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);     
			ObjectMessage msg = session.createObjectMessage();
			
			ConcernBaseEvent<String> event = new ConcernBaseEvent<String>(
					System.currentTimeMillis(), 
					sender, destination, sessionID, 
					checksum, eventName, eventData,
					CepType.DROOLS,  false,extension);
			
 				msg.setObject(event);
				logger.info(".......sending probe message");
				producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		String brokerUrl = "tcp://172.17.0.2:61616";
		
		TestmoreThanOneConn(brokerUrl);

		System.out.println("SENT");
	}

/*	private static void testNetworkCongestion(String brokerUrl) {

		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vera","griselda", brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("DROOLS-InstanceOne");
            MessageProducer producer = session.createProducer(topic);     
			  
            ObjectMessage msg = session.createObjectMessage();
            
			ConcernNetworkEvent<String> connectionAmount = new ConcernNetworkEvent<String>(
					System.currentTimeMillis(), 
					"probeROS", "monitoring","sessionOne",
					"asd", "connectionEvent", "7", 
					CepType.DROOLS, false);  
            
            
			msg.setObject(connectionAmount);
			
			producer.send(msg);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}*/

	public static void testDTProbe(String brokerUrl) throws InterruptedException {

		//TODO: update with new DTForecast event
		
		
//		try {
//			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vera","griselda", brokerUrl);
//			Connection connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//            Topic topic = session.createTopic("DROOLS-InstanceOne");
//            MessageProducer producer = session.createProducer(topic);     
//			ObjectMessage msg = session.createObjectMessage();
//			
//			ConcernDTEvent<String> previous = null;
//			
//			ConcernDTEvent<String> event = new ConcernDTEvent<String>(
//					System.currentTimeMillis(), 
//					"DigitalTwin", "Monitoring", "123098", 
//					"rfng3o49bfn", "NextEventWillBE", "930",
//					CepType.DROOLS, previous);
// 				msg.setObject(event);
//				producer.send(msg);
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static void TestDTValidation(String brokerUrl) throws InterruptedException {
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "930", "NextEventWillBE", "giogio", "asd", "123098", "DigitalTwin", "Monitoring");	
	}
	
	
	public static void TestmoreThanOneConn(String brokerUrl) throws InterruptedException {
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "established", "Connection", "port:1234", "checksum", "sessionA", "SUA_Probe", "Monitoring");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "established", "Connection", "port:1234", "checksum", "sessionA", "SUA_Probe", "Monitoring");
	}
	
	public static void TestlocalGlobalAvgDelayCheck(String brokerUrl) throws InterruptedException {
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "LocalPlanner", "GlobalPlanner");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "GlobalPlanner", "Monitoring");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "LocalPlanner", "GlobalPlanner");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "GlobalPlanner", "Monitoring");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "LocalPlanner", "GlobalPlanner");
		Thread.sleep(1000);
		testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "genericEvent", "comBetweenLocalGlobal", "noOne", "sameChecksum", "123456123456", "GlobalPlanner", "Monitoring");

	}

//
//	private static void printHello() {		
//System.out.println("  _     _    __   _  _  _  _  _ \n"
//		+ " /_`| |/_`/|//   /_//_// //_)/_`\n"
//		+ "/_, |//_,/ |/   /  / \\/_//_)/_, \n"
//		+ "                                \n");	
//	}
}
