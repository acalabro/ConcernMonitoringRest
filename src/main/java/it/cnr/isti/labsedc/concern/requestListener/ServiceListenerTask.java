package it.cnr.isti.labsedc.concern.requestListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;
import it.cnr.isti.labsedc.concern.register.ChannelsManagementRegistry;
import it.cnr.isti.labsedc.concern.register.TopicAndProperties;
import it.cnr.isti.labsedc.concern.utils.RoutingUtilities;

public class ServiceListenerTask implements Runnable, MessageListener {


	private String serviceChannelName;
	private TopicConnection receiverConnection;
	private String username;
	private String password;
    private static final Logger logger = LogManager.getLogger(ServiceListenerTask.class);
    private static MessageConsumer consumer;
    private static Session receiverSession;

	public ServiceListenerTask(String channelTaskName, String connectionUsername, String connectionPassword) {
		this.serviceChannelName = channelTaskName;
		this.username = connectionUsername;
		this.password = connectionPassword;
	}

	public String getChannelTaskName() {
		return this.serviceChannelName;
	}

	public void run() {

		logger.info("...within the serviceListener named " + this.getChannelTaskName());
		try {
			receiverConnection = ChannelsManagementRegistry.GetNewTopicConnection(username, password);
			receiverSession = ChannelsManagementRegistry.GetNewSession(receiverConnection);

			Topic topic = ChannelsManagementRegistry.GetNewSessionTopic(this.toString(), receiverSession,serviceChannelName, ChannelProperties.GENERICREQUESTS);
			consumer = receiverSession.createConsumer(topic);
			//RegisterForCommunicationChannels.ServiceListeningOnWhichChannel.put(key, value)
			logger.info("...service listener named " + consumer.toString() + " created within the executor named " + this.getChannelTaskName());
			consumer.setMessageListener(this);
			receiverConnection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onMessage(Message message) {

		if (message instanceof ObjectMessage) {
			ObjectMessage casted = (ObjectMessage)message;
			try {
				if (casted.getObject() != null && (casted.getObject() instanceof ConcernEvaluationRequestEvent<?>)) {
					ConcernEvaluationRequestEvent<?> incomingRequest = (ConcernEvaluationRequestEvent<?>)casted.getObject();
					TopicAndProperties topicWhereToForward= RoutingUtilities.BestCepSelectionForRules(incomingRequest);
					if (topicWhereToForward != null) {
						forwardToCep(topicWhereToForward, message);
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		if (message instanceof TextMessage) {
			TextMessage msg = (TextMessage) message;
			try {
				logger.info("EventListenerTask " + this.serviceChannelName + " receives a TextMessage: " + msg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	private void forwardToCep(TopicAndProperties queueWhereToForward, Message message) {
		try {
			receiverConnection = ChannelsManagementRegistry.GetNewTopicConnection(username, password);
            Session session = receiverConnection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(queueWhereToForward.getTopicAddress());
            MessageProducer producer = session.createProducer(topic);
            ObjectMessage forwarded = (ObjectMessage) message;
			forwarded.setJMSDestination(topic);
            producer.send(forwarded);
            producer.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
