package it.cnr.isti.labsedc.concern.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.Session;
import javax.jms.TopicConnection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;

public class ChannelsManagementRegistry {

	private static Logger logger ;
	public static HashMap<String, TopicAndProperties> ActiveTopics;
	public static HashMap<TopicAndProperties, String> ActiveCep;
	public static HashMap<ChannelProperties, String> ActiveServicesChannel;
	public static HashMap<Session, TopicConnection> ActiveSessions;
	public static HashMap<String, String> ConsumersChannels;
	public static HashMap<String, String> ProbesChannels;
	public static ActiveMQConnectionFactory connectionFactory;
	public static MqttClient mqttClient;
	//the broker connection factory
	private static String mqttChannel;

	public ChannelsManagementRegistry() {
		logger = LogManager.getLogger(ChannelsManagementRegistry.class);

    	logger.debug("into " + this.getClass().getSimpleName());
		ActiveTopics = new HashMap<String, TopicAndProperties>();
		//creator and topic name

		ActiveCep = new HashMap<TopicAndProperties, String>();
		//cep available on the infrastructure- the string is the topicName

		ActiveServicesChannel = new HashMap<ChannelProperties, String>();
		//channel on which the system will listen for incoming messages organized by service (requests to forward to a specific cep)

		ActiveSessions = new HashMap<Session, TopicConnection>();
		//map the session associated to a connection

		ConsumersChannels = new HashMap<String, String>();
		//map the consumer that requests an evaluation and are waiting for a response on that channel dynamically created

		ProbesChannels = new HashMap<String, String>();
		//channels available for probes
    	logger.debug(this.getClass().getSimpleName() + " started");

		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);

    	logger.debug(this.getClass().getSimpleName() + " loaded in registry.");
	}

	public void setConnectionFactory(ActiveMQConnectionFactory factory) {
		ChannelsManagementRegistry.connectionFactory = factory;
	}

	public static ActiveMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	
	public static void setMqttClient(MqttClient client) {
		ChannelsManagementRegistry.mqttClient = client;
	}

	public static MqttClient getMqttClient() {
		return ChannelsManagementRegistry.mqttClient;
	}
	
	public static void setMqttChannel(String mqttChannel) {
		ChannelsManagementRegistry.mqttChannel = mqttChannel;
	}
	
	public static String getMqttChannel() {
		return ChannelsManagementRegistry.mqttChannel;
	}

	public static TopicConnection GetNewTopicConnection(String username, String password) throws JMSException {
		ChannelsManagementRegistry.connectionFactory.setTrustedPackages(new ArrayList<String>(Arrays.asList("it.cnr.isti.labsedc.concern.event,it.cnr.isti.labsedc.concern.cep,it.cnr.isti.labsedc.concern.eventListener,it.cnr.isti.labsedc.concern.requestListener".split(","))));
		ChannelsManagementRegistry.connectionFactory.setUserName(username);
		ChannelsManagementRegistry.connectionFactory.setPassword(password);
		return  ChannelsManagementRegistry.connectionFactory.createTopicConnection();
	}

	public static Session GetNewSession(TopicConnection receiverConnection) throws JMSException {
		receiverConnection.createSession(true, Session.SESSION_TRANSACTED);
		Session session = receiverConnection.createSession(true, Session.SESSION_TRANSACTED);
		ChannelsManagementRegistry.ActiveSessions.put(session, receiverConnection);
		return session;
	}

	public static Topic GetNewSessionTopic(String creator, Session receiverSession, String topicName, ChannelProperties property) throws JMSException {
		Topic topic = receiverSession.createTopic(topicName);
		//ChannelsManagementRegistry.ActiveTopics.put(creator,new TopicAndProperties(topicName,property));
		return topic;
	}

	public static void LogDrop() {
		for (int i = 0; i<ActiveTopics.size();i++) {
			logger.info(ActiveTopics.values().toArray()[i].toString());
		}
	}

	public static Topic RegisterNewCepTopic(String CepIdentifier, Session receiverSession, String topicName,
			ChannelProperties channelProperties, CepType cepType) throws JMSException {
		Topic topic = receiverSession.createTopic(topicName);
		ChannelsManagementRegistry.ActiveCep.put(new TopicAndProperties(CepIdentifier,channelProperties, cepType, topicName), topicName);
		return topic;
	}
	
	public static String getConsumerChannel(String consumerName) {
		return ConsumersChannels.get(consumerName);
	}
}
