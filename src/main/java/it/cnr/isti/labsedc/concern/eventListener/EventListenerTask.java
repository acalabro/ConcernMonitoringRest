package it.cnr.isti.labsedc.concern.eventListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;
import it.cnr.isti.labsedc.concern.event.ConcernProbeEvent;
import it.cnr.isti.labsedc.concern.register.ChannelsManagementRegistry;
import it.cnr.isti.labsedc.concern.register.TopicAndProperties;
import it.cnr.isti.labsedc.concern.storage.StorageController;
import it.cnr.isti.labsedc.concern.utils.RoutingUtilities;

public class EventListenerTask implements Runnable, MessageListener {


	private String eventChannelName;
	private TopicConnection receiverConnection;
	private String username;
	private String password;
	private StorageController storageManager;
    private static final Logger logger = LogManager.getLogger(EventListenerTask.class);
    private static MessageConsumer consumer;
    private static Session receiverSession;

	public EventListenerTask(String eventChannelName, String connectionUsername, String connectionPassword, StorageController storageManager) {
		this.eventChannelName = eventChannelName;
		this.username = connectionUsername;
		this.password = connectionPassword;
		this.storageManager = storageManager;
	}

	public String getEventChannelName() {
		return this.eventChannelName;
	}

	public void run() {

		logger.info("...within the executor named " + this.getEventChannelName());
		try {
			receiverConnection = ChannelsManagementRegistry.GetNewTopicConnection(username, password);
			receiverSession = ChannelsManagementRegistry.GetNewSession(receiverConnection);

			Topic queue = ChannelsManagementRegistry.GetNewSessionTopic(this.toString(), receiverSession,eventChannelName, ChannelProperties.EVENTS);
			consumer = receiverSession.createConsumer(queue);
			//RegisterForCommunicationChannels.ServiceListeningOnWhichChannel.put(key, value)
			logger.info("...eventListener named " + consumer.toString() + " created within the executor named " + this.getEventChannelName());
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
				if (casted.getObject() != null) {
					if(casted.getObject() instanceof ConcernAbstractEvent<?>) {
						ConcernAbstractEvent<?> incomingRequest = (ConcernAbstractEvent<?>)casted.getObject();
						TopicAndProperties topicWhereToForward= RoutingUtilities.BestCepSelectionForEvents(incomingRequest);
						if (topicWhereToForward != null) {
							forwardEventToCEP(topicWhereToForward, message);
							storageManager.saveMessage(incomingRequest);
						}
					}
					if (casted.getObject() instanceof ConcernProbeEvent<?>) {
						ConcernProbeEvent<?> incomingRequest = (ConcernProbeEvent<?>)casted.getObject();
						TopicAndProperties topicWhereToForward= RoutingUtilities.BestCepSelectionForEvents(incomingRequest);
						if (topicWhereToForward != null) {
							forwardEventToCEP(topicWhereToForward, message);
							storageManager.saveMessage(incomingRequest);
						}
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		if (message instanceof TextMessage) {
			TextMessage msg = (TextMessage) message;
			try {
				logger.info("EventListenerTask " + this.eventChannelName + " receives TextMessage: " + msg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	private void forwardEventToCEP(TopicAndProperties topicWhereToForward, Message message) {
		try {
			receiverConnection = ChannelsManagementRegistry.GetNewTopicConnection(username, password);
            Session session = receiverConnection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicWhereToForward.getTopicAddress());
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
