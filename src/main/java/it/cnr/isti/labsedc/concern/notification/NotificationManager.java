package it.cnr.isti.labsedc.concern.notification;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.ConcernApp;

public class NotificationManager extends Thread {

	private static Logger logger;
	
	public NotificationManager() {
		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
		logger = LogManager.getLogger(NotificationManager.class);

	}
	
	public static void NotifyToConsumer(String consumerName, String violationMessage) {
		logger.info(violationMessage);
//		try {
//			logger.info("Creating response topic");
//			topic = session.createTopic(ChannelsManagementRegistry.getConsumerChannel(consumerName));
//
//        MessageProducer producer = session.createProducer(topic);
//		TextMessage msg = session.createTextMessage(violationMessage);
//		producer.send(msg);
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void NotifyToGroundStation(String port, String notificationMessage) {
		LoRaOnSerialWriter writer = new LoRaOnSerialWriter();
		writer.connect(port);
		writer.write(notificationMessage);
		writer.closePort();
	}

	@Override
	public void run() {

	//TODO:
		
	}
}
